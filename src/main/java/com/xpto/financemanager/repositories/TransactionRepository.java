package com.xpto.financemanager.repositories;

import com.xpto.financemanager.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    @Procedure(procedureName = "calcular_valor_cliente_proc")
    BigDecimal calculateCustomerValue(@Param("customerId") Long customerId);

    List<TransactionEntity> findByAccountIdIn(List<Long> accountIds);
}