package com.project.shopappbaby.services;

import com.project.shopappbaby.dtos.OrderDTO;
import com.project.shopappbaby.models.Order;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws Exception;
    Order getOrder(Long id);
    Order updateOrder(Long id, OrderDTO orderDTO);
    void deleteOrder(Long id);
    List<Order> getAllOrders(Long userId);
}
