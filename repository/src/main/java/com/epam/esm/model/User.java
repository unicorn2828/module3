package com.epam.esm.model;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User extends BaseModel<User> {

    private String login;
    private String password;
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> role = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @Fetch(value = FetchMode.SELECT)
    private List<Order> orders;
}
