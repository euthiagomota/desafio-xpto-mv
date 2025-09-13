package com.xpto.financemanager.entities;

import com.xpto.financemanager.enums.ECustomerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "customer")
public class CustomerEntity {

    public CustomerEntity(
            String name,
            String cpf,
            String cnpj,
            String phone,
            ECustomerType customerType
    ) {
        this.name = name;
        this.cpf = cpf;
        this.cnpj = cnpj;
        this.phone = phone;
        this.customerType = customerType;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = true)
    private String cpf;

    @Column(unique = true, nullable = true)
    private String cnpj;

    private String phone;

    @Enumerated(EnumType.STRING)
    ECustomerType customerType;

    private LocalDateTime registerAt;

    @PrePersist
    protected void onCreate() {
        this.registerAt = LocalDateTime.now();
    }
}
