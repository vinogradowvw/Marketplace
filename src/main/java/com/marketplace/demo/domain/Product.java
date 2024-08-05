package com.marketplace.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product implements EntityWithId<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_product")
    private Long id;
    //@NotBlank(message = "Price can not be empty")
    private Integer price;
    //@NotBlank(message = "Name can not be empty")
    private String name;
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_post", referencedColumnName = "id_post")
    private Post post;

    @OneToMany(mappedBy = "product")
    private List<OrderProduct> orders = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<CartProduct> carts = new ArrayList<>();

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}