package io.github.aplaraujo.entities;

import io.github.aplaraujo.entities.enums.OrderStatus;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToMany
    @JoinTable(name = "tb_order_product", joinColumns = @JoinColumn(name = "product_id"),  inverseJoinColumns = @JoinColumn(name = "order_id"))
    private Set<Product> products = new HashSet<>();
}
