package com.marketplace.demo.service.OrderService;

import com.marketplace.demo.domain.Order;
import com.marketplace.demo.domain.State;
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

    public Order changeState(Order order, State state){
        if (!orderRepository.existsById(order.getID())){
            throw new IllegalArgumentException("There is no order with id " + order.getID());
        }

        order.setState(state);

        return orderRepository.save(order);
    }

    @Override
    protected CrudRepository<Order, Long> getRepository() {
        return orderRepository;
    }
}
