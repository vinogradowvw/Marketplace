package com.marketplace.demo.service.OrderService;

import com.marketplace.demo.domain.Order;
import com.marketplace.demo.domain.State;
import com.marketplace.demo.service.CrudService;

public interface OrderServiceInterface extends CrudService<Order, Long> {

    public Order changeState(Order order, State state);
}
