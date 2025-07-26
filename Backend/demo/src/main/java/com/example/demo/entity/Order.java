package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String status;
    private String gender;
    private OffsetDateTime createdAt;
    private OffsetDateTime returnedAt;
    private OffsetDateTime shippedAt;
    private OffsetDateTime deliveredAt;
    private Integer numOfItem;
}

