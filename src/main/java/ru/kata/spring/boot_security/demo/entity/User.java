package ru.kata.spring.boot_security.demo.entity;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "firstname")
    private String firstName;
    @Column(name = "secondname")
    private String secondName;
    @Column(name = "age")
    private Integer age;

    private String username;
    private String password;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Fetch(FetchMode.JOIN)
    private Set<Role> userRoles;

    public User() {
        this.userRoles = new HashSet<>();
    }

    public User(String firstName, String secondName, Integer age, String username, String password) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.age = age;
        this.password = password;
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRoles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getShortRole() {
        return userRoles.toString().substring(1, userRoles.toString().length() - 1);
    }
}
