package com.srots.application.usecase.product;

import com.srots.application.dto.ProductDTO;
import com.srots.domain.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

public class GetProductsUseCase {

    private final ProductRepository productRepository;

    public GetProductsUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> execute() {
        return productRepository.findAll().stream()
                .map(p -> new ProductDTO(
                        p.getId().getValue(),
                        p.getName(),
                        p.getCode(),
                        p.getDescription(),
                        p.getCurrentVersion().getValue(),
                        p.getNextVersion().getValue(),
                        p.getOwner()
                ))
                .collect(Collectors.toList());
    }
}
