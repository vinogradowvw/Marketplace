package com.marketplace.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Payment implements EntityWithId<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_payment")
    private Long id;
    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product")
    Product product;

    @Override
    public Long getID() {
        return id;
    }
}
