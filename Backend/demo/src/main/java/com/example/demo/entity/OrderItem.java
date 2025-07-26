package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
public class OrderItem {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "inventory_item_id")
    private InventoryItem inventoryItem;

    private String status;
    private OffsetDateTime createdAt;
    private OffsetDateTime shippedAt;
    private OffsetDateTime deliveredAt;
    private OffsetDateTime returnedAt;
    private Double salePrice;

    public OrderItem(Long id, Order order, User user, Product product, InventoryItem inventoryItem,
                     String status, OffsetDateTime createdAt, OffsetDateTime shippedAt,
                     OffsetDateTime deliveredAt, OffsetDateTime returnedAt, Double salePrice) {
        this.id = id;
        this.order = order;
        this.user = user;
        this.product = product;
        this.inventoryItem = inventoryItem;
        this.status = status;
        this.createdAt = createdAt;
        this.shippedAt = shippedAt;
        this.deliveredAt = deliveredAt;
        this.returnedAt = returnedAt;
        this.salePrice = salePrice;
    }
}
