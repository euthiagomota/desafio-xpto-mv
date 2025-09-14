package com.xpto.financemanager.controllers;

import com.xpto.financemanager.services.CompanyReportService;
import com.xpto.financemanager.services.CustomerReportPerPeriodService;
import com.xpto.financemanager.services.CustomerReportService;
import com.xpto.financemanager.services.CustomersReport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/reports")
@Tag(name = "Report", description = "Relatórios de clientes")
public class ReportController {

    private final CustomerReportService customerReportService;
    private final CustomerReportPerPeriodService customerReportPerPeriodService;
    private final CompanyReportService companyReportService;
    private final CustomersReport customersReport;

    public ReportController(
            CustomerReportService customerReportService,
            CustomerReportPerPeriodService customerReportPerPeriodService,
            CompanyReportService companyReportService,
            CustomersReport customersReport
    ) {
        this.customerReportService = customerReportService;
        this.customerReportPerPeriodService = customerReportPerPeriodService;
        this.companyReportService = companyReportService;
        this.customersReport = customersReport;
    }

    @Operation(summary = "Relatório de um cliente específico",
            description = "Gera um relatório completo de um cliente, contendo informações gerais e saldo atual.")
    @GetMapping("/{customerId}")
    public void getCustomerReport(@PathVariable Long customerId) {
        customerReportService.generateCustomerReport(customerId);
    }

    @Operation(summary = "Relatório de um cliente por período",
            description = "Gera um relatório de movimentações de um cliente dentro do período informado.")
    @GetMapping("/{customerId}/period")
    public void getCustomerReport(
            @PathVariable Long customerId,
            @RequestParam("initialDate") LocalDate initialDate,
            @RequestParam("finalDate") LocalDate finalDate

    ) {
        customerReportPerPeriodService.generateCustomerReportPerPeriod(customerId, initialDate, finalDate);
    }

    @Operation(summary = "Relatório de todos os clientes",
            description = "Gera uma lista de relatórios resumidos de todos os clientes.")
    @GetMapping("/customers")
    public void customersReport() {
        this.customersReport.execute();
    }

    @Operation(summary = "Relatório de receita da empresa",
            description = "Gera o relatório de receita da empresa no período informado, mostrando cada cliente e o total de receitas.")
    @GetMapping("/company")
    public void companyReport(
            @RequestParam("initialDate") LocalDate startDate,
            @RequestParam("finalDate") LocalDate endDAte

    ) {
        this.companyReportService.getCompanyReport(startDate, endDAte);
    }


}
