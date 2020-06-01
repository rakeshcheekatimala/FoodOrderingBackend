package com.upgrad.FoodOrderingApp.service.businness;


import com.upgrad.FoodOrderingApp.service.dao.PaymentDao;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentBusinessService {
    @Autowired
    PaymentDao paymentDao;

    public List<PaymentEntity> getAllPaymentMethods(){
        return paymentDao.getAllPaymentMethods();
    }
}
