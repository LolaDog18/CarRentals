package com.example.CarRentals.repository;

import com.example.CarRentals.io.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    UserEntity findByEmail(String email);
    Optional<UserEntity> findByUserId(String userId);
}
