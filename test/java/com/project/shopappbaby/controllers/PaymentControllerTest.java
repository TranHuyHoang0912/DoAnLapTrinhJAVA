package com.project.shopappbaby.controllers;

import com.project.shopappbaby.core.response.ResponseObject;
import com.project.shopappbaby.dtos.PaymentDTO;
import com.project.shopappbaby.services.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPay_ValidRequest() {
        PaymentDTO.VNPayResponse vnPayResponse = new PaymentDTO.VNPayResponse("00", "Success", "");
        when(paymentService.createVnPayPayment(any(HttpServletRequest.class))).thenReturn(vnPayResponse);

        ResponseObject<PaymentDTO.VNPayResponse> response = paymentController.pay(request);

        // Sử dụng Payload
        ResponseObject.Payload<PaymentDTO.VNPayResponse> payload = response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue()); // Kiểm tra mã trạng thái HTTP
        assertEquals("Success", payload.getMessage()); // Kiểm tra thông điệp từ Payload
        assertEquals(vnPayResponse, payload.getData()); // Kiểm tra dữ liệu từ Payload
    }

    @Test
    void testPayCallbackHandler_SuccessStatus() {
        when(request.getParameter("vnp_ResponseCode")).thenReturn("00");

        ResponseObject<PaymentDTO.VNPayResponse> response = paymentController.payCallbackHandler(request);

        // Sử dụng Payload
        ResponseObject.Payload<PaymentDTO.VNPayResponse> payload = response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue()); // Kiểm tra mã trạng thái HTTP
        assertEquals("Success", payload.getMessage()); // Kiểm tra thông điệp từ Payload
        assertEquals(new PaymentDTO.VNPayResponse("00", "Success", ""), payload.getData()); // Kiểm tra dữ liệu từ Payload
    }

    @Test
    void testPayCallbackHandler_FailureStatus() {
        when(request.getParameter("vnp_ResponseCode")).thenReturn("01");

        ResponseObject<PaymentDTO.VNPayResponse> response = paymentController.payCallbackHandler(request);

        // Sử dụng Payload
        ResponseObject.Payload<PaymentDTO.VNPayResponse> payload = response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue()); // Kiểm tra mã trạng thái HTTP
        assertEquals("Failed", payload.getMessage()); // Kiểm tra thông điệp từ Payload
        assertEquals(null, payload.getData()); // Kiểm tra dữ liệu từ Payload
    }
}
