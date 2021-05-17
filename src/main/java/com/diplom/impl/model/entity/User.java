package com.diplom.impl.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@Entity
@NoArgsConstructor
@ToString
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Exclude
    private long id;

    @Column(unique = true)
    @ToString.Exclude
    private String email;

    @Column(unique = true)
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<Role> roles = new HashSet<>();

    private LocalDate date;

    private boolean isLocked;

    public User(String email, String username, String password, boolean isLocked, Role... roles) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles.addAll(Arrays.asList(roles));
        this.isLocked = isLocked;
        this.date = LocalDate.now();
    }

    public User(String email, String username, String password, boolean isLocked, Set<Role> roles) {
        this.email = email;
        this.username = username;
        this.password = password;
        if (roles != null) this.roles = roles;
        this.isLocked = isLocked;
        this.date = LocalDate.now();
    }
}
