package com.marketplace.demo.domain.classes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Likes implements EntityWithId<Long>{

    Likes() {}
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_like")
    private Long id;
    private Timestamp timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    User author;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_post")
    Post post;

    @Override
    public Long getID() {
        return id;
    }
}
