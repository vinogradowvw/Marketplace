package com.marketplace.demo.domain;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Image implements EntityWithId<Long> {

    Image() {}
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_image")
    private Long id;

    @NotBlank(message="Path can not be empty")
    private String path;

    @ManyToMany(targetEntity = Product.class)
    @JoinTable(
            name = "product_image",
            joinColumns = @JoinColumn(name="id_product"),
            inverseJoinColumns = @JoinColumn(name="id_image")
    )
    List<Product> productsWithImg;

    @Override
    public Long getID() {
        return id;
    }

}
