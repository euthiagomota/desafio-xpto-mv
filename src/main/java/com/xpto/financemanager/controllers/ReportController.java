package com.xpto.financemanager.controllers;

import com.xpto.financemanager.services.ReportService;
import com.xpto.financemanager.services.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
