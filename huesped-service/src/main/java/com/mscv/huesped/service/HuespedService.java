package com.mscv.huesped.service;

import com.mscv.huesped.entity.Huesped;

import java.util.List;

public interface HuespedService {
    Huesped saveHuesped(Huesped huesped);

    List<Huesped> getAllHuespedes();

    Huesped getHuesped(String huespedId);
}
