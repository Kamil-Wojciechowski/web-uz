package com.uz.shop.animal.world.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Model Linia Zamówienia
 * Lombok pomaga nam utworzyć automatycznie gettery, settery oraz bezargumentowy konstruktor
 * Tag Entity powoduje utworzenie elementu w bazie danych
 */
@Getter
@Setter
@NoArgsConstructor
@Entity(name="order_units")
public class OrderUnit {
    // Tag ID oraz GeneratedValue oznacza, że kolumna jest Primary Key oraz wartość jest generowana
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Tagi poniżej pozwalają utworzyć relacje w bazie danych
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "product_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonProperty("productId")
    private Product product;

    //Tagi poniżej pozwalają utworzyć relacje w bazie danych
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "order_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonProperty("orderId")
    private Order order;

    //Tagi oznaczają, że kolumna nie może być pusta w bazie danych oraz odpowiednik klucza w REST jest "amount"
    @Column(nullable = false)
    @JsonProperty("amount")
    private Integer amount;

    //Flagi odpowiadają za automatyczne tworzenie czasu utworzenia
    @CreationTimestamp
    private LocalDateTime createdAt;

    public OrderUnit(Product product, Order order, Integer amount) {
        this.product = product;
        this.order = order;
        this.amount = amount;
    }
}
