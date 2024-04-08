package com.marketplace.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
public class User implements EntityWithId<Long>{

    public User (
        String username,
        String email,
        String password
    ) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

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

    @OneToMany(targetEntity = Likes.class, mappedBy = "author", fetch = FetchType.LAZY)
    private List<Likes> likes;
    @OneToMany(targetEntity = Payment.class, mappedBy = "user", fetch = FetchType.LAZY)
    private List<Payment> payments;
    @OneToMany(targetEntity = Post.class, mappedBy = "user", fetch = FetchType.LAZY)
    private List<Post> posts;
    @OneToMany(targetEntity = Subscription.class, mappedBy = "subscriber", fetch = FetchType.LAZY)
    private List<Subscription> subscribers;
    @OneToMany(targetEntity = Subscription.class, mappedBy = "user", fetch = FetchType.LAZY)
    private List<Subscription> subscriptions;

    @Override
    public Long getID() {
        return id;
    }
}
