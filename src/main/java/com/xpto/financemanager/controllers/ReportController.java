package com.xpto.financemanager.controllers;

import com.xpto.financemanager.services.CompanyReportService;
import com.xpto.financemanager.services.CustomerReportPerPeriodService;
import com.xpto.financemanager.services.CustomerReportService;
import com.xpto.financemanager.services.CustomersReport;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/{customerId}")
    public ResponseEntity<String> getCustomerReport(@PathVariable Long customerId) {
        String report = customerReportService.generateCustomerReport(customerId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/{customerId}/period")
    public ResponseEntity<String> getCustomerReport(
            @PathVariable Long customerId,
            @RequestParam("initialDate") LocalDate initialDate,
            @RequestParam("finalDate") LocalDate finalDate

    ) {
        String report = customerReportPerPeriodService.generateCustomerReportPerPeriod(customerId, initialDate, finalDate);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/customers")
    public List<String> customersReport() {
        return this.customersReport.execute();
    }

    //    @GetMapping("/company")
//    public void companyReport(
//            @RequestParam("initialDate") LocalDate startDate,
//            @RequestParam("finalDate") LocalDate endDAte
//
//    ) {
//        this.companyReportService.getCompanyReport(startDate, endDAte);
//    }


}
