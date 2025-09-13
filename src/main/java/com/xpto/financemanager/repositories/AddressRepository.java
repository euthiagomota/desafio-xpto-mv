package com.xpto.financemanager.repositories;

import com.xpto.financemanager.entities.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    Optional<AddressEntity> findByCustomerId(Long customerId);
}
