package io.github.aplaraujo.services;

import io.github.aplaraujo.dto.ProductDTO;
import io.github.aplaraujo.entities.Product;
import io.github.aplaraujo.mappers.ProductMapper;
import io.github.aplaraujo.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;

    public Page<Product> search(String name, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name").ascending());

        if (name != null) {
            return repository.findByName(name, pageable);
        }

        return repository.findAll(pageable);
    }

    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }

    public ProductDTO insert(ProductDTO dto) {
        Product product = mapper.toEntity(dto);
        product = repository.save(product);
        return mapper.toDTO(product);
    }

    public void update(Product product) {
        if (product.getId() == null) {
            throw new IllegalArgumentException("Product not found!");
        }
        repository.save(product);
    }

    public void delete(Product product) {
        repository.delete(product);
    }
}
