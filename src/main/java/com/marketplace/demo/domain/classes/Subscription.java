package com.marketplace.demo.domain.classes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Subscription implements EntityWithId<Long> {

    Subscription() {}

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_subscription")
    private Long id;
    private Timestamp timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_subscriber")
    private User subscriber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_user")
    private User user;

    @Override
    public Long getID() {
        return id;
    }
}
