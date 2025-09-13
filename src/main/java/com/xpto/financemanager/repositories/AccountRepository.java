package com.xpto.financemanager.repositories;

import com.xpto.financemanager.entities.AccountEntity;
import com.xpto.financemanager.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByAccountNumberAndBankAndAgency(
            String accountNumber,
            String bank,
            String agency
    );

    List<AccountEntity> findByCustomerId(Long customerId);
}
