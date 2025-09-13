package com.xpto.financemanager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "addresses")
public class AddressEntity {

    public AddressEntity(
            String street,
            String homeNumber,
            String city,
            String complement,
            String state,
            String zipCode,
            String uf,
            CustomerEntity customer

    ) {
        this.street = street;
        this.city = city;
        this.complement = complement;
        this.state = state;
        this.zipCode = zipCode;
        this.customer = customer;
        this.uf = uf;
        this.homeNumber = homeNumber;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String homeNumber;
    private String complement;
    private String city;
    private String uf;
    private String state;
    private String zipCode;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;
}
