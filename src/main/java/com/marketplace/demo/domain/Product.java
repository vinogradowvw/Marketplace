package com.marketplace.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product implements EntityWithId<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_product")
    private Long id;
    @NotBlank(message = "Price can not be empty")
    private int price;
    @NotBlank(message = "Name can not be empty")
    private String name;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_post")
    private Post post;

    @OneToMany(targetEntity = Payment.class, mappedBy = "product", fetch = FetchType.LAZY)
    private ArrayList<Payment> payments;

    @Override
    public Long getID() {
        return id;
    }
}