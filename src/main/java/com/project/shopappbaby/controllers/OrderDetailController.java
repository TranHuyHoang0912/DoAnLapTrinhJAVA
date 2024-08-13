package com.project.shopappbaby.controllers;

import com.project.shopappbaby.dtos.*;
import com.project.shopappbaby.exceptions.DataNotFoundException;
import com.project.shopappbaby.models.Order;
import com.project.shopappbaby.models.OrderDetail;
import com.project.shopappbaby.responses.OrderDetailResponse;
import com.project.shopappbaby.services.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    //Thêm mới 1 order detail
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @Valid  @RequestBody OrderDetailDTO orderDetailDTO) {
        try {
            OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(newOrderDetail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(
            @Valid @PathVariable("id") Long id) throws DataNotFoundException {
        OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
        return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(orderDetail));
        //return ResponseEntity.ok("getOrderDetail with id = "+id);
    }
    //lấy ra danh sách các order_details của 1 order nào đó
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(
            @Valid @PathVariable("orderId") Long orderId
    ) {
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponses = orderDetails
                .stream()
                .map(OrderDetailResponse::fromOrderDetail)
                .toList();
        return ResponseEntity.ok(orderDetailResponses);
        //return ResponseEntity.ok("getOrderDetails with orderId = "+orderId);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable("id") Long id,
            @RequestBody OrderDetailDTO orderDetailDTO) {
        try {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
            return ResponseEntity.ok().body(orderDetail);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(
            @Valid @PathVariable("id") Long id) {
        orderDetailService.deleteById(id);
        return ResponseEntity.ok().body("Delete Order detail with id : "+id+" successfully");
        //return ResponseEntity.noContent().build();
    }
//    {
//            "order_id": 2,
//            "product_id": 1,
//            "email":"nvxy@ahoo.com",
//            "price": 12.33,
//            "number_of_products": 2,
//            "total_money": 123.45,
//            "color":"#ff00ff"
//
//    }
}