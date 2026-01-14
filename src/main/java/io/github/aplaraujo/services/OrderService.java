package io.github.aplaraujo.services;

import io.github.aplaraujo.dto.OrderDTO;
import io.github.aplaraujo.entities.Order;
import io.github.aplaraujo.entities.Product;
import io.github.aplaraujo.mappers.OrderMapper;
import io.github.aplaraujo.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final OrderMapper mapper;

    public Page<Order> search(Product product, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return repository.findAllWithProducts(pageable);
    }

    public List<OrderDTO> searchAllByClient(Long clientId) {
        List<Order> orders = clientId != null ? repository.findByClientId(clientId) : repository.findAll();
        return orders.stream().map(mapper::toDTO).collect(Collectors.toList());
    }

}
