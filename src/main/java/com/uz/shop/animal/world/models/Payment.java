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
@Entity(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "order_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonProperty("orderId")
    private Order order;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String callback_data;

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
