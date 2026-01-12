package io.github.aplaraujo.entities;

import io.github.aplaraujo.entities.enums.OrderStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long clientId;
    private Long productId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
