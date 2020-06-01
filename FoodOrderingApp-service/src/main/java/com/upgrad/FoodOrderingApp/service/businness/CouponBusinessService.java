package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CouponDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CouponBusinessService {
    @Autowired
    CouponDao couponDao;

    public CouponEntity getCouponByCouponName(String couponName) {
        return couponDao.getCouponByName(couponName);
    }

    public CustomerAuthEntity getCustomerByAccessToken(String access_token) {
        return couponDao.getCustomerByAccessToken(access_token);
    }

    public CouponEntity getCouponByUUID(String uuid) {
        return couponDao.getCouponByUUID(uuid);
    }
}
