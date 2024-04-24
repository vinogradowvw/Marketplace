package com.marketplace.demo.controller.converter;

import com.marketplace.demo.controller.dto.ProductDTO;
import com.marketplace.demo.domain.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductDTOConverter implements DTOConverter<ProductDTO, Product> {



    @Override
    public ProductDTO toDTO(Product product) {
        return null;
    }

    @Override
    public Product toEntity(ProductDTO productDTO) {
        return null;
    }
}
