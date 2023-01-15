package com.uz.shop.animal.world.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Model Adresu
 * Lombok pomaga nam utworzyć automatycznie gettery, settery oraz bezargumentowy konstruktor
 * Tag Entity powoduje utworzenie elementu w bazie danych
 */
@Getter
@Setter
@NoArgsConstructor
@Entity(name="addresses")
public class Address {
    // Tag ID oraz GeneratedValue oznacza, że kolumna jest Primary Key oraz wartość jest generowana
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Tagi poniżej pozwalają utworzyć relacje w bazie danych
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonProperty("userId")
    private User user;

    //Tagi oznaczają, że kolumna nie może być pusta w bazie danych oraz odpowiednik klucza w REST jest "firstname"
    @Column(nullable = false)
    @JsonProperty("firstname")
    private String firstname;

    @Column(nullable = false)
    @JsonProperty("lastname")
    private String lastname;

    @Column(nullable = true)
    @JsonProperty("company")
    private String company;

    @Column(nullable = true)
    @JsonProperty("nip")
    private String nip;

    @Column(nullable = false)
    @JsonProperty("mobileNumber")
    private String mobileNumber;

    @Column(nullable = false)
    @JsonProperty("street")
    private String street;

    @Column(nullable = false)
    @JsonProperty("postalCode")
    private String postalCode;

    @Column(nullable = false)
    @JsonProperty("city")
    private String city;

    //Flagi odpowiadają za automatyczne tworzenie czasu utworzenia oraz aktualizacji
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Address(User user, String firstname, String lastname, String company, String nip, String mobileNumber, String street, String postalCode, String city) {
        this.user = user;
        this.firstname = firstname;
        this.lastname = lastname;
        this.company = company;
        this.nip = nip;
        this.mobileNumber = mobileNumber;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
    }
}
