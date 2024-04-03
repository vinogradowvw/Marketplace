package com.marketplace.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Subscription implements EntityWithId<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_subscription")
    private Long id;
    private Timestamp timestamp;

    //TODO: add user relationship

    @Override
    public Long getID() {
        return id;
    }
}
