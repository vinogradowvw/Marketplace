package com.marketplace.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Entity
@Setter
@Getter
@NoArgsConstructor
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
    private Map<Product, Long> products = new HashMap<>();

    @Override
    public Long getID() {
        return id;
    }
}
