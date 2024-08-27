package com.marketplace.demo.service.OrderService;

import com.marketplace.demo.domain.Order;
import com.marketplace.demo.domain.OrderProduct;
import com.marketplace.demo.domain.Payment;
import com.marketplace.demo.domain.State;
import com.marketplace.demo.persistance.*;
import com.marketplace.demo.service.CrudServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class OrderService extends CrudServiceImpl<Order, Long> implements OrderServiceInterface{

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private OrderProductRepository orderProductRepository;
    private PaymentRepository paymentRepository;
    private OrderRepository orderRepository;

    public Order changeState(Order order, State state){
        if (!orderRepository.existsById(order.getID())){
            throw new IllegalArgumentException("There is no order with id " + order.getID());
        }

        order.setState(state);

        return orderRepository.save(order);
    }

    @Override
    public void deleteById(Long id) throws IllegalArgumentException {
        if (!orderRepository.existsById(id)){
            throw new IllegalArgumentException("There is no order with id " + id);
        }

        Order order = orderRepository.findById(id).get();

        for (OrderProduct oP : order.getProducts()){
            oP.getProduct().getOrders().remove(oP);
            productRepository.save(oP.getProduct());

            orderProductRepository.deleteById(oP.getId());
        }

        order.getUser().getOrders().remove(order);
        userRepository.save(order.getUser());

        paymentRepository.deleteById(order.getPayment().getID());

        orderRepository.deleteById(id);
    }

    @Override
    protected CrudRepository<Order, Long> getRepository() {
        return orderRepository;
    }
}
