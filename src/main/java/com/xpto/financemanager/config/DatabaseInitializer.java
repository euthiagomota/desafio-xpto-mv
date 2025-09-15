package com.xpto.financemanager.config;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DatabaseInitializer {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    private static final String USER = "admin";
    private static final String PASSWORD = "app123";

    @PostConstruct
    public void init() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Função calcular_valor_cliente
            stmt.execute(
                    "CREATE OR REPLACE FUNCTION calcular_valor_cliente(p_customer_id IN NUMBER) " +
                            "RETURN NUMBER IS " +
                            "v_qtd_mov NUMBER; " +
                            "v_valor_total NUMBER := 0; " +
                            "BEGIN " +
                            "SELECT COUNT(*) INTO v_qtd_mov FROM transactions t " +
                            "INNER JOIN account a ON t.account_id = a.id " +
                            "WHERE a.customer_id = p_customer_id; " +
                            "IF v_qtd_mov <= 10 THEN " +
                            "v_valor_total := v_qtd_mov * 1; " +
                            "ELSIF v_qtd_mov <= 20 THEN " +
                            "v_valor_total := v_qtd_mov * 0.75; " +
                            "ELSE " +
                            "v_valor_total := v_qtd_mov * 0.5; " +
                            "END IF; " +
                            "RETURN v_valor_total; " +
                            "END calcular_valor_cliente;"
            );

            // Procedure calcular_valor_cliente_proc
            stmt.execute(
                    "CREATE OR REPLACE PROCEDURE calcular_valor_cliente_proc(customerId IN NUMBER, valor_total OUT NUMBER) AS " +
                            "BEGIN " +
                            "valor_total := calcular_valor_cliente(customerId); " +
                            "END calcular_valor_cliente_proc;"
            );

            // Procedure relatorio_receita_clientes
            stmt.execute(
                    "CREATE OR REPLACE PROCEDURE relatorio_receita_clientes(p_data_inicio IN DATE, p_data_fim IN DATE, p_resultado OUT SYS_REFCURSOR) AS " +
                            "BEGIN " +
                            "OPEN p_resultado FOR " +
                            "SELECT c.id AS customer_id, c.name AS customer_name, COUNT(t.id) AS qtd_movimentacoes, NVL(SUM(t.amount),0) AS valor_total " +
                            "FROM customer c " +
                            "JOIN account a ON a.customer_id = c.id " +
                            "JOIN transactions t ON t.account_id = a.id " +
                            "WHERE TRUNC(t.transaction_date) BETWEEN TRUNC(p_data_inicio) AND TRUNC(p_data_fim) " +
                            "GROUP BY c.id, c.name " +
                            "ORDER BY c.name; " +
                            "END relatorio_receita_clientes;"
            );

            // Função calcular_valor_cliente_por_periodo
            stmt.execute(
                    "CREATE OR REPLACE FUNCTION calcular_valor_cliente_por_periodo(p_customer_id IN NUMBER, p_initial_date IN DATE, p_final_date IN DATE) " +
                            "RETURN NUMBER IS " +
                            "v_qtd_mov NUMBER; " +
                            "v_valor_total NUMBER := 0; " +
                            "BEGIN " +
                            "SELECT COUNT(*) INTO v_qtd_mov FROM transactions t " +
                            "INNER JOIN account a ON t.account_id = a.id " +
                            "WHERE a.customer_id = p_customer_id " +
                            "AND TRUNC(t.transaction_date) BETWEEN TRUNC(p_initial_date) AND TRUNC(p_final_date); " +
                            "IF v_qtd_mov <= 10 THEN " +
                            "v_valor_total := v_qtd_mov * 1; " +
                            "ELSIF v_qtd_mov <= 20 THEN " +
                            "v_valor_total := v_qtd_mov * 0.75; " +
                            "ELSE " +
                            "v_valor_total := v_qtd_mov * 0.5; " +
                            "END IF; " +
                            "RETURN v_valor_total; " +
                            "END calcular_valor_cliente_por_periodo;"
            );

            System.out.println("Funções e procedures criadas com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
