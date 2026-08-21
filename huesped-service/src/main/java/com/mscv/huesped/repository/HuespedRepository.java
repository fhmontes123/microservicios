package com.mscv.huesped.repository;

import com.mscv.huesped.entity.Huesped;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HuespedRepository extends JpaRepository<Huesped, String> {
}
