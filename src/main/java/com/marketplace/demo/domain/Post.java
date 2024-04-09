package com.marketplace.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
    @NotBlank
    private User user;
    @OneToMany(targetEntity = Likes.class, mappedBy = "post", fetch = FetchType.LAZY)
    private List<Likes> likes;
    @ManyToMany(targetEntity = Tag.class)
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name="id_tag"),
            inverseJoinColumns = @JoinColumn(name="id_post")
    )
    private List<Tag> tags;

    @ManyToMany(mappedBy = "postsWithImg")
    private List<Image> images;

    @Override
    public Long getID() {
        return id;
    }
}
