package io.github.aplaraujo.repositories;

import io.github.aplaraujo.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByClientId(Long id);

    Optional<Order> findByIdAndClientId(Long id, Long clientId);

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.products")
    Page<Order> findAllWithProducts(Pageable pageable);
}
