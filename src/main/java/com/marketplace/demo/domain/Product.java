package com.marketplace.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Entity
@Getter
@Setter
public class Product implements EntityWithId<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_product")
    private Long id;

    @ManyToMany
    Set<Image> productImages;

    @ManyToMany
    Set<Post> postsWithProduct;

    @NotBlank(message = "Price can not be empty")
    private int price;

    @NotBlank(message = "Name can not be empty")
    private String name;

    private String description;

    @Override
    public Long getID() {
        return id;
    }
}