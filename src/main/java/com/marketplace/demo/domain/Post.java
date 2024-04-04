package com.marketplace.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Post implements EntityWithId<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_product")
    private Long id;

    @ManyToMany
    Set<Product> productsInPost;

    @OneToMany
    @JoinColumn(name = "id_user")
    User user;

    private Set<String> tags;

    private String description;

    @Override
    public Long getID() {
        return id;
    }
}
