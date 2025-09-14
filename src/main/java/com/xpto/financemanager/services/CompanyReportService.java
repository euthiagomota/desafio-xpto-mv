package com.xpto.financemanager.services;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CompanyReportService {

    @PersistenceContext
    private EntityManager entityManager;

    public void getCompanyReport(LocalDate startDate, LocalDate endDate) {
        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("relatorio_receita_clientes");

            query.registerStoredProcedureParameter("p_data_inicio", LocalDate.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_data_fim", LocalDate.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_resultado", void.class, ParameterMode.REF_CURSOR);

            query.setParameter("p_data_inicio", startDate);
            query.setParameter("p_data_fim", endDate);

            query.execute();

            List<Object[]> results = query.getResultList();

            BigDecimal totalReceita = BigDecimal.ZERO;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            System.out.println("Relatório de receita da empresa (XPTO) por período:");
            System.out.println("Período: " + startDate.format(formatter) + " a " + endDate.format(formatter));

            for (Object[] row : results) {
                String cliente = (String) row[1];
                BigDecimal valorMov = row[3] instanceof BigDecimal ? (BigDecimal) row[3] : new BigDecimal(((Number) row[3]).doubleValue());
                Number qtdMov = (Number) row[2];

                totalReceita = totalReceita.add(valorMov);

                System.out.println("Cliente " + cliente + " - Quantidade de movimentações: " + qtdMov +
                        " - Valor das movimentações: R$ " + String.format("%,.2f", valorMov));
            }

            System.out.println("Total de receitas: R$ " + String.format("%,.2f", totalReceita));
        } catch (Error error) {
            System.out.println(error);
            throw error;
        }

    }
}
