package com.project.shopappbaby.controllers;

import com.project.shopappbaby.components.LocalizationUtils;
import com.project.shopappbaby.dtos.OrderDetailDTO;
import com.project.shopappbaby.exceptions.DataNotFoundException;
import com.project.shopappbaby.models.Order;
import com.project.shopappbaby.models.OrderDetail;
import com.project.shopappbaby.models.Product;
import com.project.shopappbaby.responses.OrderDetailResponse;
import com.project.shopappbaby.services.OrderDetailService;
import com.project.shopappbaby.utils.MessageKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderDetailControllerTest {

    @Mock
    private OrderDetailService orderDetailService;

    @Mock
    private LocalizationUtils localizationUtils;

    @InjectMocks
    private OrderDetailController orderDetailController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrderDetail_ValidData() throws Exception {
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        OrderDetail newOrderDetail = new OrderDetail();
        newOrderDetail.setId(1L);
        newOrderDetail.setPrice(12.34F);
        newOrderDetail.setNumberOfProducts(2);
        newOrderDetail.setTotalMoney(24.68F);
        newOrderDetail.setColor("#ff00ff");

        Product product = new Product();
        product.setId(1L);
        newOrderDetail.setProduct(product);

        Order order = new Order();
        order.setId(1L);
        newOrderDetail.setOrder(order);

        OrderDetailResponse orderDetailResponse = OrderDetailResponse.fromOrderDetail(newOrderDetail);

        when(orderDetailService.createOrderDetail(any(OrderDetailDTO.class))).thenReturn(newOrderDetail);

        ResponseEntity<?> response = orderDetailController.createOrderDetail(orderDetailDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(orderDetailResponse, response.getBody());
        verify(orderDetailService, times(1)).createOrderDetail(any(OrderDetailDTO.class));
    }

    @Test
    void testGetOrderDetail_ValidId() throws Exception {
        Long id = 1L;
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(id);
        orderDetail.setPrice(12.34F);
        orderDetail.setNumberOfProducts(2);
        orderDetail.setTotalMoney(24.68F);
        orderDetail.setColor("#ff00ff");

        Product product = new Product();
        product.setId(1L);
        orderDetail.setProduct(product);

        Order order = new Order();
        order.setId(1L);
        orderDetail.setOrder(order);

        OrderDetailResponse orderDetailResponse = OrderDetailResponse.fromOrderDetail(orderDetail);

        when(orderDetailService.getOrderDetail(id)).thenReturn(orderDetail);

        ResponseEntity<?> response = orderDetailController.getOrderDetail(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(orderDetailResponse, response.getBody());
        verify(orderDetailService, times(1)).getOrderDetail(id);
    }

    @Test
    void testGetOrderDetail_DataNotFoundException() throws DataNotFoundException {
        Long id = 1L;
        when(orderDetailService.getOrderDetail(id)).thenThrow(new DataNotFoundException("Order detail not found"));

        ResponseEntity<?> response = orderDetailController.getOrderDetail(id);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Order detail not found", response.getBody());
    }

    @Test
    void testGetOrderDetails() {
        Long orderId = 2L;

        Product product = new Product();
        product.setId(1L);

        Order order = new Order();
        order.setId(1L);

        OrderDetail orderDetail1 = new OrderDetail();
        orderDetail1.setId(1L);
        orderDetail1.setProduct(product);
        orderDetail1.setOrder(order);
        orderDetail1.setPrice(12.34F);
        orderDetail1.setNumberOfProducts(2);
        orderDetail1.setTotalMoney(24.68F);
        orderDetail1.setColor("#ff00ff");

        OrderDetail orderDetail2 = new OrderDetail();
        orderDetail2.setId(2L);
        orderDetail2.setProduct(product);
        orderDetail2.setOrder(order);
        orderDetail2.setPrice(56.78F);
        orderDetail2.setNumberOfProducts(1);
        orderDetail2.setTotalMoney(56.78F);
        orderDetail2.setColor("#00ff00");

        List<OrderDetail> orderDetails = List.of(orderDetail1, orderDetail2);
        List<OrderDetailResponse> orderDetailResponses = orderDetails
                .stream()
                .map(OrderDetailResponse::fromOrderDetail)
                .toList();

        when(orderDetailService.findByOrderId(orderId)).thenReturn(orderDetails);

        ResponseEntity<?> response = orderDetailController.getOrderDetails(orderId);

        assertEquals(200, response.getStatusCodeValue());

        List<OrderDetailResponse> responseBody = (List<OrderDetailResponse>) response.getBody();

        assertNotNull(responseBody);
        assertEquals(orderDetailResponses.size(), responseBody.size());
        for (int i = 0; i < orderDetailResponses.size(); i++) {
            OrderDetailResponse expected = orderDetailResponses.get(i);
            OrderDetailResponse actual = responseBody.get(i);

            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getOrderId(), actual.getOrderId());
            assertEquals(expected.getProductId(), actual.getProductId());
            assertEquals(expected.getPrice(), actual.getPrice());
            assertEquals(expected.getNumberOfProducts(), actual.getNumberOfProducts());
            assertEquals(expected.getTotalMoney(), actual.getTotalMoney());
            assertEquals(expected.getColor(), actual.getColor());
        }

        verify(orderDetailService, times(1)).findByOrderId(orderId);
    }

    @Test
    void testUpdateOrderDetail() throws DataNotFoundException {
        Long id = 1L;
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        OrderDetail updatedOrderDetail = new OrderDetail();
        updatedOrderDetail.setId(id);
        updatedOrderDetail.setPrice(12.34F);
        updatedOrderDetail.setNumberOfProducts(2);
        updatedOrderDetail.setTotalMoney(24.68F);
        updatedOrderDetail.setColor("#ff00ff");

        Product product = new Product();
        product.setId(1L);
        updatedOrderDetail.setProduct(product);

        Order order = new Order();
        order.setId(1L);
        updatedOrderDetail.setOrder(order);

        when(orderDetailService.updateOrderDetail(id, orderDetailDTO)).thenReturn(updatedOrderDetail);

        ResponseEntity<?> response = orderDetailController.updateOrderDetail(id, orderDetailDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedOrderDetail, response.getBody());
        verify(orderDetailService, times(1)).updateOrderDetail(id, orderDetailDTO);
    }

    @Test
    void testDeleteOrderDetail() {
        Long id = 1L;

        when(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_ORDER_DETAIL_SUCCESSFULLY))
                .thenReturn("Delete Order detail with id : " + id + " successfully");

        ResponseEntity<?> response = orderDetailController.deleteOrderDetail(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Delete Order detail with id : " + id + " successfully", response.getBody());
        verify(orderDetailService, times(1)).deleteById(id);
    }
}
