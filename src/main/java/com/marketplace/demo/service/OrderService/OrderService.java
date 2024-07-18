package com.marketplace.demo.service.OrderService;

import com.marketplace.demo.domain.Order;
import com.marketplace.demo.persistance.OrderRepository;
import com.marketplace.demo.service.CrudServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class OrderService extends CrudServiceImpl<Order, Long> implements OrderServiceInterface{

    private OrderRepository orderRepository;

    @Override
    protected CrudRepository<Order, Long> getRepository() {
        return orderRepository;
    }
}
