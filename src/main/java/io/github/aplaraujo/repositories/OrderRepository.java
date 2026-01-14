package io.github.aplaraujo.repositories;

import io.github.aplaraujo.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.products WHERE o.client.id = :clientId")
    List<Order> findByClientId(@Param("clientId") Long clientId);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.products WHERE o.id = :id AND o.client.id = :clientId")
    Optional<Order> findByIdAndClientId(@Param("id") Long id, @Param("clientId") Long clientId);

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.products")
    Page<Order> findAllWithProducts(Pageable pageable);
}
