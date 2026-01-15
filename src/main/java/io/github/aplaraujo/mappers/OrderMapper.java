package io.github.aplaraujo.mappers;

import io.github.aplaraujo.dto.CreateOrderDTO;
import io.github.aplaraujo.dto.OrderDTO;
import io.github.aplaraujo.dto.ProductDTO;
import io.github.aplaraujo.entities.Client;
import io.github.aplaraujo.entities.Order;
import io.github.aplaraujo.entities.Product;
import io.github.aplaraujo.repositories.ClientRepository;
import io.github.aplaraujo.repositories.ProductRepository;
import io.github.aplaraujo.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;

    public Order toEntity(CreateOrderDTO dto) {
        Order order = new Order();
        order.setOrderStatus(dto.orderStatus());

        for (ProductDTO prod: dto.products()) {
            Product product = productRepository.findById(prod.id()).orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
            order.getProducts().add(product);
        }
        return order;
    }

    public OrderDTO toDTO(Order order) {
        Long clientId = order.getClient() != null ? order.getClient().getId() : null;
        List<ProductDTO> list = order.getProducts().stream().map(prod -> new ProductDTO(prod.getId(), prod.getName(), prod.getPrice())).toList();
        return new OrderDTO(order.getId(), clientId, list, order.getOrderStatus());
    }

    public void updateEntity(Order order, CreateOrderDTO dto) {
        order.setOrderStatus(dto.orderStatus());
//        if (dto.clientId() != null) {
//            Client client = clientRepository.findById(dto.clientId()).orElseThrow(() -> new ResourceNotFoundException("Client not found!"));
//            order.setClient(client);
//        }
        order.getProducts().clear();

        for (ProductDTO product: dto.products()) {
            Product product1 = productRepository.findById(product.id()).orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
            order.getProducts().add(product1);
        }
    }
}
