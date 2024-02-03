package com.hg.bethunger.model;

import com.hg.bethunger.model.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private UserRole role = UserRole.USER;

    @Embedded
    private Account account;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "manager")
    private List<Game> games;

    @OneToMany(mappedBy = "user")
    private List<Bet> bets;

    @OneToMany(mappedBy = "user")
    private List<Supply> supplies;

    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions;

    public User(UserRole role, String username, String password, String firstName, String lastName) {
        this.role = role;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}