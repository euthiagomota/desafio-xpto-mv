package com.xpto.financemanager.repositories;

import com.xpto.financemanager.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    boolean existsByCpf(String cpf);
    boolean existsByCnpj(String cnpj);
}
