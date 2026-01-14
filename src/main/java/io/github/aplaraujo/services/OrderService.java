package io.github.aplaraujo.services;

import io.github.aplaraujo.entities.Order;
import io.github.aplaraujo.entities.Product;
import io.github.aplaraujo.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;

    public Page<Order> search(Product product, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return repository.findAllWithProducts(pageable);
    }
}
