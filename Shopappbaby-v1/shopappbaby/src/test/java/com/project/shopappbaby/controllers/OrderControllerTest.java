package com.project.shopappbaby.controllers;

import com.project.shopappbaby.components.LocalizationUtils;
import com.project.shopappbaby.dtos.OrderDTO;
import com.project.shopappbaby.exceptions.DataNotFoundException;
import com.project.shopappbaby.models.Order;
import com.project.shopappbaby.responses.OrderListResponse;
import com.project.shopappbaby.responses.OrderResponse;
import com.project.shopappbaby.services.IOrderService;
import com.project.shopappbaby.utils.MessageKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Mock
    private IOrderService orderService;

    @Mock
    private LocalizationUtils localizationUtils;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder_ValidData() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        Order mockOrder = new Order();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(mockOrder);

        ResponseEntity<?> response = orderController.createOrder(orderDTO, bindingResult);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockOrder, response.getBody());
        verify(orderService, times(1)).createOrder(any(OrderDTO.class));
    }

    @Test
    void testCreateOrder_InvalidData() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        when(bindingResult.hasErrors()).thenReturn(true);
        List<String> errors = List.of("Name is required");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("orderDTO", "name", "Name is required")));

        ResponseEntity<?> response = orderController.createOrder(orderDTO, bindingResult);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errors, response.getBody());
        verify(orderService, never()).createOrder(any(OrderDTO.class));
    }

    @Test
    void testCreateOrder_Exception() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(orderService.createOrder(any(OrderDTO.class))).thenThrow(new Exception("Order creation failed"));

        ResponseEntity<?> response = orderController.createOrder(orderDTO, bindingResult);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Order creation failed", response.getBody());
    }

    @Test
    void testGetOrdersByUserId() {
        Long userId = 4L;
        List<Order> mockOrders = List.of(new Order(), new Order());
        when(orderService.findByUserId(userId)).thenReturn(mockOrders);

        ResponseEntity<?> response = orderController.getOrders(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockOrders, response.getBody());
        verify(orderService, times(1)).findByUserId(userId);
    }

//    @Test
//    void testGetOrderById() {
//        Long orderId = 2L;
//        Order mockOrder = new Order();
//        OrderResponse mockResponse = OrderResponse.fromOrder(mockOrder);
//        when(orderService.getOrder(orderId)).thenReturn(mockOrder);
//
//        ResponseEntity<?> response = orderController.getOrder(orderId);
//
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals(mockResponse, response.getBody());
//        verify(orderService, times(1)).getOrder(orderId);
//    }

    @Test
    void testUpdateOrder() throws DataNotFoundException {
        Long orderId = 2L;
        OrderDTO orderDTO = new OrderDTO();
        Order updatedOrder = new Order();
        when(orderService.updateOrder(orderId, orderDTO)).thenReturn(updatedOrder);

        ResponseEntity<?> response = orderController.updateOrder(orderId, orderDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedOrder, response.getBody());
        verify(orderService, times(1)).updateOrder(orderId, orderDTO);
    }

    @Test
    void testUpdateOrder_DataNotFoundException() throws DataNotFoundException {
        Long orderId = 2L;
        OrderDTO orderDTO = new OrderDTO();
        when(orderService.updateOrder(orderId, orderDTO)).thenThrow(new DataNotFoundException("Order not found"));

        ResponseEntity<?> response = orderController.updateOrder(orderId, orderDTO);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Order not found", response.getBody());
    }

    @Test
    void testDeleteOrder() {
        Long orderId = 2L;
        String localizedMessage = "Order deleted successfully";
        when(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_ORDER_SUCCESSFULLY, orderId)).thenReturn(localizedMessage);

        ResponseEntity<?> response = orderController.deleteOrder(orderId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(localizedMessage, response.getBody());
        verify(orderService, times(1)).deleteOrder(orderId);
    }

//    @Test
//    void testGetOrdersByKeyword() {
//        String keyword = "sample";
//        int page = 0;
//        int limit = 10;
//        PageRequest pageRequest = PageRequest.of(page, limit);
//
//        // Giả lập hành vi của orderService.getOrdersByKeyword
//        Order mockOrder = new Order();
//        Page<Order> mockOrderPage = new PageImpl<>(List.of(mockOrder));
//        when(orderService.getOrdersByKeyword(keyword, pageRequest)).thenReturn(mockOrderPage);
//
//        // Chuyển đổi Page<Order> thành Page<OrderResponse>
//        Page<OrderResponse> mockResponsePage = mockOrderPage.map(orderEntity -> OrderResponse.fromOrder(orderEntity));
//
//        ResponseEntity<OrderListResponse> response = orderController.getOrdersByKeyword(keyword, page, limit);
//
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals(1, response.getBody().getTotalPages());
//        assertEquals(1, response.getBody().getOrders().size());
//        verify(orderService, times(1)).getOrdersByKeyword(keyword, pageRequest);
//    }

}
