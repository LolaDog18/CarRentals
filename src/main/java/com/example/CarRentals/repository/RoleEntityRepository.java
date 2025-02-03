package com.example.CarRentals.repository;

import com.example.CarRentals.io.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleEntityRepository extends JpaRepository<RoleEntity, Integer> {
}