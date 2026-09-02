package com.mscv.huesped.service.impl;

import com.mscv.huesped.entity.Calificacion;
import com.mscv.huesped.entity.Hotel;
import com.mscv.huesped.entity.Huesped;
import com.mscv.huesped.exception.ResourceNotFoundException;
import com.mscv.huesped.external.service.CalificacionService;
import com.mscv.huesped.external.service.HotelService;
import com.mscv.huesped.repository.HuespedRepository;
import com.mscv.huesped.service.HuespedService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class HuespedServiceImpl implements HuespedService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private CalificacionService calificacionService;

    @Autowired
    private HuespedRepository huespedRepository;

    @Override
    public Huesped saveHuesped(Huesped huesped) {
        String randomHuespedId = UUID.randomUUID().toString();
        huesped.setHuespedId(randomHuespedId);
        return huespedRepository.save(huesped);
    }

    @Override
    public List<Huesped> getAllHuespedes() {
        return huespedRepository.findAll();
    }

    @CircuitBreaker(name = "huespedServiceBreaker", fallbackMethod = "fallbackHuesped")
    public Huesped getHuesped(String huespedId) {
        Huesped huesped = huespedRepository
                .findById(huespedId)
                .orElseThrow(()->new ResourceNotFoundException("Huesped no encontrado con ID : " + huespedId));

//        REST TEMPLATE
//        Calificacion[]  calificacionesDelHuesped = restTemplate
//                // .getForObject("http://localhost:8083/calificaciones/huespedes/" + huesped.getHuespedId(), Calificacion[].class);
//                .getForObject("http://CALIFICACION-SERVICE/calificaciones/huespedes/" + huesped.getHuespedId(), Calificacion[].class);
//        List<Calificacion> calificaciones = Arrays.asList(calificacionesDelHuesped);
//      OPEN FEIGN
        List<Calificacion> calificaciones = calificacionService.calificacionesHuesped(huesped.getHuespedId());

        List<Calificacion> listaCalificaciones = calificaciones.stream().map(calificacion -> {
            System.out.println("Hotel ID: " + calificacion.getHotelId());

//            REST TEMPLATE
//            ResponseEntity<Hotel> forEntity = restTemplate
//                    // .getForEntity("http://localhost:8082/hoteles/" + calificacion.getHotelId(), Hotel.class);
//                    .getForEntity("http://HOTEL-SERVICE/hoteles/" + calificacion.getHotelId(), Hotel.class);
//            Hotel hotel = forEntity.getBody();
//            log.info("Respuesta con codigo de estado: {}", forEntity.getStatusCode());

//            OPEN FEIGN
            Hotel hotel =hotelService.getHotel(calificacion.getHotelId());

            calificacion.setHotel(hotel);
            return calificacion;
        }).toList();
        log.info("Calificaciones del Huesped : {}", calificaciones);
        // huesped.setCalificaciones(calificaciones);   // Contiene solo las calificaciones
        huesped.setCalificaciones(listaCalificaciones); // Contiene las calificaciones y hoteles
        return huesped;
    }

    public Huesped fallbackHuesped(String huespedId, Exception exception){
        Huesped huesped = huespedRepository.findById(huespedId).orElseThrow();
        huesped.setInformacionAdicional("Algunos servicios no estan disponibles");
        huesped.setCalificaciones(new ArrayList<>());
        return huesped;
    }
}
