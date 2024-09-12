package com.project.shopappbaby.services;

public interface ICouponService {
    double calculateCouponValue(String couponCode, double totalAmount);
}
