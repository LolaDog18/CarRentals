package com.woozy.carrentals.repository;

import com.woozy.carrentals.io.entity.CustomerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<CustomerEntity, Integer> {
    Optional<CustomerEntity> findByEmail(String email);
    Optional<CustomerEntity> findByUserId(String userId);

    Optional<CustomerEntity> findByMobileNumber(String mobileNumber);
}
