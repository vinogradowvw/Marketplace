package com.marketplace.demo.domain;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Image implements EntityWithId<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_image")
    private Long id;

    @NotBlank(message="Path can not be empty")
    private String path;

    @ManyToMany(targetEntity = Post.class)
    @JoinTable(
            name = "post_image",
            joinColumns = @JoinColumn(name="id_image"),
            inverseJoinColumns = @JoinColumn(name="id_post")
    )
    private List<Post> postsWithImg;

    @Override
    public Long getID() {
        return id;
    }

}
