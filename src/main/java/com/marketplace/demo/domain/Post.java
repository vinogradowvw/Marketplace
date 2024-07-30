package com.marketplace.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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
    @NotBlank(message = "Name can not be empty")
    private String name;
    private String description;
    private Long views = 0L;

    @OneToOne(targetEntity = Product.class, mappedBy = "post")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "id_user")
    @NotBlank
    private User user;
    @ManyToMany
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name="id_user"),
            inverseJoinColumns = @JoinColumn(name="id_post")
    )
    private List<User> likedUsers = new ArrayList<>();
    @ManyToMany(targetEntity = Tag.class)
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name="id_tag"),
            inverseJoinColumns = @JoinColumn(name="id_post")
    )
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(targetEntity = Review.class, mappedBy = "post")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(targetEntity = Image.class, mappedBy = "post", fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

    @Override
    public Long getID() {
        return id;
    }
}
