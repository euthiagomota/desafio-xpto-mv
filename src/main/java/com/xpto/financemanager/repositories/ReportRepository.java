package com.xpto.financemanager.repositories;

import com.xpto.financemanager.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface ReportRepository extends JpaRepository<CustomerEntity, Long> {

    @Procedure(procedureName = "calcular_valor_cliente_proc")
    BigDecimal calculateCustomerValue(@Param("customerId") Long customerId);

    @Query(value = "SELECT calcular_valor_cliente_por_periodo(:customerId, :initialDate, :finalDate) FROM dual", nativeQuery = true)
    BigDecimal calculateCustomerValuePerPeriod(
            @Param("customerId") Long customerId,
            @Param("initialDate")LocalDate initialDate,
            @Param("finalDate") LocalDate finalDate
    );
}
