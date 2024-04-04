package com.marketplace.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Post implements EntityWithId<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_post")
    private Long id;
    private String description;

    @ManyToMany(targetEntity = Product.class)
    @JoinTable(
            name = "post_product",
            joinColumns = @JoinColumn(name="id_product"),
            inverseJoinColumns = @JoinColumn(name="id_post")
    )
    List<Product> productsInPost;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
    @OneToMany(targetEntity = Like.class, mappedBy = "post", fetch = FetchType.LAZY)
    private List<Like> likes;
    @ManyToMany(targetEntity = Tag.class)
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name="id_tag"),
            inverseJoinColumns = @JoinColumn(name="id_post")
    )
    private List<Tag> tags;


    @Override
    public Long getID() {
        return id;
    }
}
