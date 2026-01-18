package io.github.aplaraujo.services;

import io.github.aplaraujo.dto.CreateOrderDTO;
import io.github.aplaraujo.dto.OrderDTO;
import io.github.aplaraujo.entities.Client;
import io.github.aplaraujo.entities.Order;
import io.github.aplaraujo.entities.Product;
import io.github.aplaraujo.mappers.OrderMapper;
import io.github.aplaraujo.repositories.ClientRepository;
import io.github.aplaraujo.repositories.OrderRepository;
import io.github.aplaraujo.services.exceptions.OperationNotAllowedException;
import io.github.aplaraujo.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final ClientRepository clientRepository;
    private final OrderMapper mapper;

    public Page<Order> search(Product product, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return repository.findAllWithProducts(pageable);
    }

    public List<OrderDTO> searchAllByClient(Long clientId) {
        List<Order> orders = clientId != null ? repository.findByClientId(clientId) : repository.findAll();
        return orders.stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public Order searchOrderByIdAndUser(Long id, Long clientId) {
        if (id == null || clientId == null) {
            throw new IllegalArgumentException("Order ID or Client ID must not be null");
        }

        return repository.findByIdAndClientId(id, clientId).orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id + " for client: " + clientId));
    }

    @Transactional
    public OrderDTO insert(CreateOrderDTO dto) {
        Order order = mapper.toEntity(dto);
        Client client = clientRepository.findById(dto.clientId()).orElseThrow(() -> new ResourceNotFoundException("Client not found!"));;
        order.setClient(client);
        order = repository.save(order);
        return mapper.toDTO(order);
    }

    @Transactional
    public void update(Long orderId, Long clientId, CreateOrderDTO dto) {
        Order order = repository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (order.getClient() == null || !order.getClient().getId().equals(clientId)) {
            throw new OperationNotAllowedException("You don't have permission to update this order");
        }
        mapper.updateEntity(order, dto);
        repository.save(order);
    }

    @Transactional
    public void delete(Long id, Long clientId) {
        Order order = searchOrderByIdAndUser(id, clientId);
        repository.delete(order);
    }

}
