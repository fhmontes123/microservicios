package com.mscv.calificacion.repository;

import com.mscv.calificacion.entity.Calificacion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CalificacionRepository extends MongoRepository<Calificacion, String> {

    List<Calificacion> findByHuespedId(String huespedId);

    List<Calificacion> findByHotelId(String hotelId);
}
