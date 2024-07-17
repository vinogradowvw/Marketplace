package com.marketplace.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.security.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Entity
@Setter
@Getter
public class Cart implements EntityWithId<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_cart")
    private Long id;
    private Timestamp timestamp;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    private User user;

    @ElementCollection
    @CollectionTable(name = "cart_products", joinColumns = @JoinColumn(name = "id_cart"))
    @MapKeyJoinColumn(name = "id_product")
    @Column(name = "quantity")
    private Map<Product, Integer> products = new HashMap<>();

    @Override
    public Long getID() {
        return id;
    }
}
