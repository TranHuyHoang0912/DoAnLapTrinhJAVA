package com.project.shopappbaby.servicesTest;
import com.project.shopappbaby.services.OrderService;
import com.project.shopappbaby.dtos.*;
import com.project.shopappbaby.exceptions.DataNotFoundException;
import com.project.shopappbaby.models.*;
import com.project.shopappbaby.repositories.OrderDetailRepository;
import com.project.shopappbaby.repositories.OrderRepository;
import com.project.shopappbaby.repositories.ProductRepository;
import com.project.shopappbaby.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderDetailRepository orderDetailRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OrderService orderService;

    public OrderServiceTest() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testFindByUserId() {
        Long userId = 1L;
        List<Order> orders = new ArrayList<>();
        orders.add(new Order());

        when(orderRepository.findByUserId(userId)).thenReturn(orders);

        List<Order> result = orderService.findByUserId(userId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(orders.size(), result.size());
    }

    @Test
    void testGetOrdersByKeyword() {
        String keyword = "search";
        Pageable pageable = Pageable.unpaged();
        Page<Order> orders = Page.empty();

        when(orderRepository.findByKeyword(keyword, pageable)).thenReturn(orders);

        Page<Order> result = orderService.getOrdersByKeyword(keyword, pageable);

        assertNotNull(result);
        assertEquals(orders, result);
    }
}
