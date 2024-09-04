package com.marketplace.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Role implements EntityWithId<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name  = "id_role")
    private Long id;
    private String name;

    @ManyToMany
    @JoinTable(
            name="roles",
            joinColumns = @JoinColumn(name="id_role"),
            inverseJoinColumns = @JoinColumn(name="id_user")
    )
    private List<User> users = new ArrayList<>();

    @Override
    public Long getID() {
        return id;
    }
}
