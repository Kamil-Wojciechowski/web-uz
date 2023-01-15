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
 * Model Płatności
 * Lombok pomaga nam utworzyć automatycznie gettery, settery oraz bezargumentowy konstruktor
 * Tag Entity powoduje utworzenie elementu w bazie danych
 */
@Getter
@Setter
@NoArgsConstructor
@Entity(name = "payments")
public class Payment {
    // Tag ID oraz GeneratedValue oznacza, że kolumna jest Primary Key oraz wartość jest generowana
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Tagi poniżej pozwalają utworzyć relacje w bazie danych
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "order_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonProperty("orderId")
    private Order order;

    //Tag oznacza, że kolumna nie może być pusta w bazie danych
    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String callback_data;

    //Flagi odpowiadają za automatyczne tworzenie czasu utworzenia oraz aktualizacji
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Payment(Order order, String status, String callback_data) {
        this.order = order;
        this.status = status;
        this.callback_data = callback_data;
    }
}
