package com.marketplace.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Order implements EntityWithId<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="id_order")
    private Long id;
    private Timestamp timestamp;
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @ElementCollection
    @CollectionTable(name = "order_products", joinColumns = @JoinColumn(name = "id_order"))
    @MapKeyJoinColumn(name = "id_product")
    @Column(name = "quantity")
    private Map<Product, Long> products = new HashMap<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_payment", referencedColumnName = "id_payment")
    private Payment payment;

    @Override
    public Long getID() {
        return id;
    }
}
