package com.marketplace.demo.controller;

import com.marketplace.demo.controller.converter.DTOConverter;
import com.marketplace.demo.controller.dto.OrderDTO;
import com.marketplace.demo.domain.Order;
import com.marketplace.demo.domain.State;
import com.marketplace.demo.service.OrderService.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    private OrderService orderService;
    private DTOConverter<OrderDTO, Order> orderDTOConverter;

    @GetMapping
    public List<OrderDTO> getAllOrders() {

        List<OrderDTO> orderDTOList = new ArrayList<>();
        orderService.readAll().forEach(order -> orderDTOList.add(orderDTOConverter.toDTO(order)));

        return orderDTOList;
    }

    @GetMapping(path = "/{id}")
    public OrderDTO getOrderById(@PathVariable("id") Long id) {

        return orderDTOConverter.toDTO(orderService.readById(id).get());
    }

    @PostMapping(path = "/{id}")
    public OrderDTO changeState(@PathVariable("id") Long id, @RequestBody String state) {

        Order order = orderService.readById(id).get();

        order = orderService.changeState(order, State.valueOf(state));

        return orderDTOConverter.toDTO(order);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteOrder(@PathVariable("id") Long id) {

        orderService.deleteById(id);
    }
}
