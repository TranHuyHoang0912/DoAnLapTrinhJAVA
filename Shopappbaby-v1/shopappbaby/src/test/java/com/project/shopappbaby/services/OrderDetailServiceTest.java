package com.project.shopappbaby.services;

import com.project.shopappbaby.services.OrderDetailService;
import com.project.shopappbaby.dtos.OrderDetailDTO;
import com.project.shopappbaby.exceptions.DataNotFoundException;
import com.project.shopappbaby.models.Order;
import com.project.shopappbaby.models.OrderDetail;
import com.project.shopappbaby.models.Product;
import com.project.shopappbaby.repositories.OrderDetailRepository;
import com.project.shopappbaby.repositories.OrderRepository;
import com.project.shopappbaby.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class OrderDetailServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderDetailService orderDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrderDetail() throws Exception {
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setOrderId(1L);
        orderDetailDTO.setProductId(1L);
        orderDetailDTO.setNumberOfProducts(5);
        orderDetailDTO.setPrice(100.0F);
        orderDetailDTO.setTotalMoney(500.0F);
        orderDetailDTO.setColor("Red");

        Order order = Order.builder().id(1L).build();
        Product product = Product.builder().id(1L).build();
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .price(orderDetailDTO.getPrice())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .build();

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(orderDetailRepository.save(any(OrderDetail.class))).thenReturn(orderDetail);

        OrderDetail createdOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);

        assertNotNull(createdOrderDetail);
        assertEquals(orderDetailDTO.getNumberOfProducts(), createdOrderDetail.getNumberOfProducts());
        assertEquals(orderDetailDTO.getPrice(), createdOrderDetail.getPrice());
        assertEquals(orderDetailDTO.getTotalMoney(), createdOrderDetail.getTotalMoney());
        assertEquals(orderDetailDTO.getColor(), createdOrderDetail.getColor());
        verify(orderRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).findById(anyLong());
        verify(orderDetailRepository, times(1)).save(any(OrderDetail.class));
    }

    @Test
    void getOrderDetail() throws DataNotFoundException {
        OrderDetail orderDetail = OrderDetail.builder().id(1L).build();

        when(orderDetailRepository.findById(anyLong())).thenReturn(Optional.of(orderDetail));

        OrderDetail foundOrderDetail = orderDetailService.getOrderDetail(1L);

        assertNotNull(foundOrderDetail);
        assertEquals(1L, foundOrderDetail.getId());
        verify(orderDetailRepository, times(1)).findById(anyLong());
    }

    @Test
    void getOrderDetailNotFound() {
        when(orderDetailRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> orderDetailService.getOrderDetail(1L));
        verify(orderDetailRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateOrderDetail() throws DataNotFoundException {
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setOrderId(1L);
        orderDetailDTO.setProductId(1L);
        orderDetailDTO.setNumberOfProducts(10);
        orderDetailDTO.setPrice(200.0F);
        orderDetailDTO.setTotalMoney(2000.0F);
        orderDetailDTO.setColor("Blue");

        Order existingOrder = Order.builder().id(1L).build();
        Product existingProduct = Product.builder().id(1L).build();
        OrderDetail existingOrderDetail = OrderDetail.builder().id(1L)
                .order(existingOrder)
                .product(existingProduct)
                .numberOfProducts(5)
                .price(100.0F)
                .totalMoney(500.0F)
                .color("Red")
                .build();

        when(orderDetailRepository.findById(anyLong())).thenReturn(Optional.of(existingOrderDetail));
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(existingOrder));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(existingProduct));
        when(orderDetailRepository.save(any(OrderDetail.class))).thenReturn(existingOrderDetail);

        OrderDetail updatedOrderDetail = orderDetailService.updateOrderDetail(1L, orderDetailDTO);

        assertNotNull(updatedOrderDetail);
        assertEquals(orderDetailDTO.getNumberOfProducts(), updatedOrderDetail.getNumberOfProducts());
        assertEquals(orderDetailDTO.getPrice(), updatedOrderDetail.getPrice());
        assertEquals(orderDetailDTO.getTotalMoney(), updatedOrderDetail.getTotalMoney());
        assertEquals(orderDetailDTO.getColor(), updatedOrderDetail.getColor());
        verify(orderDetailRepository, times(1)).findById(anyLong());
        verify(orderRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).findById(anyLong());
        verify(orderDetailRepository, times(1)).save(any(OrderDetail.class));
    }

    @Test
    void deleteById() {
        doNothing().when(orderDetailRepository).deleteById(anyLong());

        orderDetailService.deleteById(1L);

        verify(orderDetailRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void findByOrderId() {
        orderDetailService.findByOrderId(1L);

        verify(orderDetailRepository, times(1)).findByOrderId(anyLong());
    }
}

