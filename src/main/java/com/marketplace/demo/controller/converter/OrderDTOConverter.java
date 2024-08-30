package com.marketplace.demo.controller.converter;

import com.marketplace.demo.controller.dto.OrderDTO;
import com.marketplace.demo.domain.*;
import com.marketplace.demo.persistance.OrderProductRepository;
import com.marketplace.demo.service.PaymentService.PaymentService;
import com.marketplace.demo.service.ProductService.ProductService;
import com.marketplace.demo.service.UserService.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class OrderDTOConverter implements DTOConverter<OrderDTO, Order> {

    private ProductService productService;
    private UserService userService;
    private PaymentService paymentService;
    private OrderProductRepository orderProductRepository;

    @Override
    public OrderDTO toDTO(Order order) {
        Map<Long, Long> products = new HashMap<>();

        order.getProducts().forEach(p -> products.put(p.getProduct().getID(), p.getQuantity()));

        Long paymentID = Optional.ofNullable(order.getPayment()).map(Payment::getID).orElse(null);
        Long userID = Optional.ofNullable(order.getUser()).map(User::getID).orElse(null);

        return new OrderDTO(order.getID(), order.getTimestamp(), order.getState().name(),
                            products, paymentID, userID);
    }

    @Override
    public Order toEntity(OrderDTO orderDTO) {
        Order order = new Order();
        order.setId(order.getID());
        order.setTimestamp(order.getTimestamp());
        order.setState(State.valueOf(orderDTO.state()));

        List<OrderProduct> products = new ArrayList<>();
        for (var pId : orderDTO.products().keySet()) {
            Optional<Product> productOpt = productService.readById(pId);

            Product product = productOpt.orElse(null);

            if (orderProductRepository.existsByOrderAndProduct(order, product)){
                OrderProduct.OrderProductId orderProductId = new OrderProduct.OrderProductId(order.getID(), pId);
                OrderProduct orderProduct = orderProductRepository.findById(orderProductId).get();

                products.add(orderProduct);
            }
        }
        order.setProducts(products);

        User user = null;
        Optional<Long> userId = Optional.ofNullable(orderDTO.userId());
        if (userId.isPresent()) {
            user = userService.readById(userId.get()).orElse(null);
        }
        order.setUser(user);

        Payment payment = null;
        Optional<Long> paymentId = Optional.ofNullable(orderDTO.paymentId());
        if (paymentId.isPresent()) {
            payment = paymentService.readById(paymentId.get()).orElse(null);
        }
        order.setPayment(payment);

        return order;
    }
}
