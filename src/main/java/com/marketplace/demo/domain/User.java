package com.marketplace.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User implements EntityWithId<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_user")
    private Long id;
    @NotBlank(message = "Username can not be empty.")
    private String username;
    @NotBlank(message = "Email can not be empty.")
    @Email(message = "Incorrect email address.")
    private String email;
    @NotBlank(message = "Password can not be empty.")
    private String password;

    @OneToMany(targetEntity = Like.class, mappedBy = "author", fetch = FetchType.LAZY)
    private List<Like> likes;
    @OneToMany(targetEntity = Payment.class, mappedBy = "user", fetch = FetchType.LAZY)
    private List<Payment> payments;
    @OneToMany(targetEntity = Post.class, mappedBy = "user", fetch = FetchType.LAZY)
    private List<Post> posts;
    private List<Subscription> subscribers;
    private List<Subscription> subscriptions;

    @Override
    public Long getID() {
        return id;
    }
}
