package com.xpto.financemanager.controllers;

import com.xpto.financemanager.services.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/reports")
@Tag(name = "Report", description = "Relatórios de clientes")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<String> getCustomerReport(@PathVariable Long customerId) {
        String report = reportService.generateCustomerReport(customerId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/{customerId}/period")
    public ResponseEntity<String> getCustomerReport(
            @PathVariable Long customerId,
            @RequestParam("initialDate") LocalDate initialDate,
            @RequestParam("finalDate") LocalDate finalDate

    ) {
        String report = reportService.generateCustomerReportPerPeriod(customerId, initialDate, finalDate);
        return ResponseEntity.ok(report);
    }
}
