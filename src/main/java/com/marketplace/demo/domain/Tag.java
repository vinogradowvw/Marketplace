package com.marketplace.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Tag implements EntityWithId<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_tag")
    Long id;
    String name;

    @ManyToMany(mappedBy = "tags")
    List<Post> posts;

    @Override
    public Long getID() {
        return id;
    }
}
