package com.xpto.financemanager.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "addresses")
public class Address {

    public Address(
            String street,
            String homeNumber,
            String city,
            String complement,
            String state,
            String zipCode,
            String uf,
            Customer customer

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String street;
    private String homeNumber;
    private String complement;
    private String city;
    private String uf;
    private String state;
    private String zipCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
