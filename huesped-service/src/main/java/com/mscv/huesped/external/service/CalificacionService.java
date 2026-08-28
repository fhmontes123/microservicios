package com.mscv.huesped.external.service;

import com.mscv.huesped.entity.Calificacion;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("CALIFICACION-SERVICE")
public interface CalificacionService {
    @GetMapping("/calificaciones/huespedes/{huespedId}")
    List<Calificacion> calificacionesHuesped(@PathVariable String huespedId);
}
