package com.uz.shop.animal.world.models;

import com.uz.shop.animal.world.models.enums.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import static com.uz.shop.animal.world.utils.Dictionary.INVALID_INPUT;
import static com.uz.shop.animal.world.utils.Dictionary.WRONG_FORMAT_EMAIL;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(min=2, message = INVALID_INPUT)
    private String firstname;

    @NotEmpty
    @Size(min=2, message = INVALID_INPUT)
    private String lastname;

    @NotEmpty
    @Email(message = WRONG_FORMAT_EMAIL)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private Boolean enabled;

    @NotEmpty
    @Size(min=8)
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    public User(String firstname, String lastname, String email, UserType userType, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.userType = userType;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userType.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return this.enabled;
    }
}
