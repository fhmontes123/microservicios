package com.mscv.huesped.service.impl;

import com.mscv.huesped.entity.Huesped;
import com.mscv.huesped.exception.ResourceNotFoundException;
import com.mscv.huesped.repository.HuespedRepository;
import com.mscv.huesped.service.HuespedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HuespedServiceImpl implements HuespedService {

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

    @Override
    public Huesped getHuesped(String huespedId) {
        return huespedRepository.findById(huespedId)
                .orElseThrow(()->new ResourceNotFoundException("Huesped no encontrado con ID : " + huespedId));
    }
}
