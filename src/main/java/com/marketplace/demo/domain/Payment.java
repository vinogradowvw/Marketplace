package com.marketplace.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

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
    private Timestamp timestamp;

    @OneToOne(mappedBy = "payment")
    private Order order;

    @Override
    public Long getID() {
        return id;
    }
}
