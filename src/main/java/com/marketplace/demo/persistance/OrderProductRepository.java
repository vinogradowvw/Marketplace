package com.marketplace.demo.persistance;

import com.marketplace.demo.domain.Order;
import com.marketplace.demo.domain.OrderProduct;
import com.marketplace.demo.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface OrderProductRepository extends CrudRepository<OrderProduct, OrderProduct.OrderProductId> {

    public boolean existsByOrderAndProduct(Order order, Product product);
    public boolean existsById(OrderProduct.OrderProductId id);
}
