package com.mscv.huesped.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Calificacion {
    private String calificacionId;
    private String huespedId;
    private String hotelId;
    private int puntuacion;
    private String observaciones;
    private Hotel hotel;
}
