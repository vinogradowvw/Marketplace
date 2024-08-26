package com.marketplace.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Review implements EntityWithId<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotBlank
    private String title;
    //@NotBlank
    private String description;
    @Min(1)
    @Max(5)
    @NotNull
    private Integer rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_post", referencedColumnName = "id_post")
    private Post post;

    @Override
    public Long getID() {
        return id;
    }
}
