package com.uz.shop.animal.world.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

/**
 * Model Użytkownika
 * Lombok pomaga nam utworzyć automatycznie gettery, settery oraz bezargumentowy konstruktor
 * Tag Entity powoduje utworzenie elementu w bazie danych
 * Klasa implementuje UserDetails na potrzeby Spring Security
 */
@Getter
@Setter
@NoArgsConstructor
@Entity(name="users")
public class User implements UserDetails {
    // Tag ID oraz GeneratedValue oznacza, że kolumna jest Primary Key oraz wartość jest generowana
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //NotEmpty oraz Size waliduje nam pole
    @NotEmpty
    @Size(min=2, message = INVALID_INPUT)
    private String firstname;

    @NotEmpty
    @Size(min=2, message = INVALID_INPUT)
    private String lastname;

    @NotEmpty
    @Email(message = WRONG_FORMAT_EMAIL)
    private String email;

    //Enumerated Oznacza, że dane pole jest Enumeratorem oraz wartość, którą chcemy przechowywać jest String nie indeks
    @Enumerated(EnumType.STRING)
    private UserType userType;

    private Boolean enabled;


    //Json Ingore powoduje iż gdy zapytamy o ten obiekt element nie zostanie wyświetlony
    @NotEmpty
    @Size(min=8)
    @JsonIgnore
    private String password;

    //Flagi odpowiadają za automatyczne tworzenie czasu utworzenia oraz aktualizacji
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

    //Poniżej klasy, które musiały byc nadpisane ze względu na UserDetails
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
