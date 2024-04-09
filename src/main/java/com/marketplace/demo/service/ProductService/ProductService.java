package com.marketplace.demo.service.ProductService;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.marketplace.demo.domain.Image;
import com.marketplace.demo.domain.Product;
import com.marketplace.demo.persistance.ImageRepository;
import com.marketplace.demo.persistance.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ProductService implements ProductServiceInterface {

    private ProductRepository productRepository;
    private ImageRepository imageRepository;

    @Override
    public Product getProductById(Long id) throws EntityNotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No product with id " + id + " found."));
    }

    @Override
    public Product createProduct(Product product) throws IllegalArgumentException {
        
        if (product.getDescription().length() <= 300) {
            return productRepository.save(product);
        }

        throw new IllegalArgumentException("Description is too long must be less than 300 characters");

    }

    @Override
    public Product updateProduct(Product product) {

        if (productRepository.existsById(product.getID())) {
            
            if (product.getDescription().length() <= 300) {
                return productRepository.save(product);
            }

            throw new IllegalArgumentException("Description is too long must be less than 300 characters");

        }
        
        throw new IllegalArgumentException("Product with this ID does not exists");

    }

    @Override
    public void deleteProduct(Product product) {
    
        if (productRepository.existsById(product.getID())) {
            productRepository.delete(product);
        }

        throw new IllegalArgumentException("Product with this ID does not exists");
    }

    @Override
    public Product addProductImages(Product product, List<Image> images) throws IllegalArgumentException {
        
        if (productRepository.existsById(product.getID())) {
            
            for (Image image:images){
                if (!imageRepository.existsById(image.getID())) {
                    throw new IllegalArgumentException("Image with ID " + image.getID() +" does not exists");
                }
                else {
                    image.getProductsWithImg().add(product);
                }
            }

            product.setProductImages(images);

            imageRepository.saveAll(images);

            return productRepository.save(product);
            
        }

        throw new IllegalArgumentException("Product with this ID does not exists");

    }

}
