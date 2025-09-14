package com.xpto.financemanager.repositories;

import com.xpto.financemanager.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumberAndBankAndAgency(
            String accountNumber,
            String bank,
            String agency
    );

    List<Account> findByCustomerId(Long customerId);
}
