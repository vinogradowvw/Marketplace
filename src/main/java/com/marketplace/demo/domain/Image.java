package com.marketplace.demo.domain;

import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Image implements EntityWithId {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_like")
    private Long id;

    @NotBlank(message="Path can not be empty")
    private String path;

    @ManyToMany
    Set<Product> productsWithImg;

    @Override
    public Long getID() {
        return id;
    }

}
