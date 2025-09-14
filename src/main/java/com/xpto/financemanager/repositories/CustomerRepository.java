package com.xpto.financemanager.repositories;

import com.xpto.financemanager.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByCpf(String cpf);
    boolean existsByCnpj(String cnpj);
}
