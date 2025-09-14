package com.xpto.financemanager.repositories;

import com.xpto.financemanager.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByCustomerId(Long customerId);
}
