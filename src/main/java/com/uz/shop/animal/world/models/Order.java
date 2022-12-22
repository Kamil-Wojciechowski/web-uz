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

@Getter
@Setter
@NoArgsConstructor
@Entity(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "address_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonProperty("addressId")
    private Address address;

    @Column(nullable = false)
    @JsonProperty("orderStatus")
    private String orderStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Order(Address address, String orderStatus) {
        this.address = address;
        this.orderStatus = orderStatus;
    }
}
