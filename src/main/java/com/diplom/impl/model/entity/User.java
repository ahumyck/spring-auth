package com.diplom.impl.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
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

    @ManyToOne
    @ToString.Exclude
    private Role role;

    private boolean isLocked;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private Set<Attribute> attributeSet = new HashSet<>();

    public User(String email, String username, String password, Role role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.isLocked = true;
    }

    public User(String email, String username, String password, Role role, boolean isLocked) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.isLocked = isLocked;
    }

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.isLocked = true;
    }
}
