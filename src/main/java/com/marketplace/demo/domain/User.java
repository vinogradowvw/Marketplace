package com.marketplace.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = { "id" })
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

    @ManyToMany
    @JoinTable(
            name = "user_subscribers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriber_id")
    )
    private ArrayList<User> subscribers;

    @ManyToMany
    @JoinTable(
            name = "user_subscribers",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private ArrayList<User> subscriptions;

    @Override
    public Long getID() {
        return id;
    }
}
