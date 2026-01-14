package io.github.aplaraujo.mappers;

import io.github.aplaraujo.dto.ProductDTO;
import io.github.aplaraujo.entities.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product toEntity(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.name());
        product.setPrice(dto.price());
        return product;
    }

    public ProductDTO toDTO(Product product) {
        return new ProductDTO(product.getId(), product.getName(), product.getPrice());
    }

}
