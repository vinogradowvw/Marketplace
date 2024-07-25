package com.marketplace.demo.controller.converter;

import com.marketplace.demo.controller.dto.OrderDTO;
import com.marketplace.demo.domain.*;
import com.marketplace.demo.service.PaymentService.PaymentService;
import com.marketplace.demo.service.ProductService.ProductService;
import com.marketplace.demo.service.UserService.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
public class OrderDTOConverter implements DTOConverter<OrderDTO, Order> {

    private ProductService productService;
    private UserService userService;
    private PaymentService paymentService;

    @Override
    public OrderDTO toDTO(Order order) {
        Map<Long, Long> products = new HashMap<>();

        order.getProducts().keySet().forEach(p -> products.put(p.getID(), order.getProducts().get(p)));

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

        Map<Product, Long> products = new HashMap<>();
        for (var pId : orderDTO.products().keySet()) {
            Optional<Product> productOpt = productService.readById(pId);

            Product product = productOpt.orElse(null);
            products.put(product, orderDTO.products().get(pId));
        }
        order.setProducts(products);

        User user = userService.readById(orderDTO.userId()).orElse(null);
        order.setUser(user);

        Payment payment = paymentService.readById(orderDTO.paymentId()).orElse(null);
        order.setPayment(payment);

        return order;
    }
}
