package com.project.shopappbaby.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

public abstract class PaymentDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class VNPayResponse {
        public String code;
        public String message;
        public String paymentUrl;
    }

}
