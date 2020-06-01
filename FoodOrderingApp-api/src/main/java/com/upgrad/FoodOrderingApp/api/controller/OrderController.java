package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CouponBusinessService;
import com.upgrad.FoodOrderingApp.service.businness.OrderBusinessService;
import com.upgrad.FoodOrderingApp.service.businness.PaymentBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/")
public class OrderController {
    @Autowired
    CouponBusinessService couponBusinessService;
    @Autowired
    PaymentBusinessService paymentBusinessService;
    @Autowired
    OrderBusinessService orderBusinessService;


    @RequestMapping(method = RequestMethod.GET, path = "/order/coupon/{coupon_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<CouponDetailsResponse> getCouponByName(@PathVariable("coupon_name") String couponName, @RequestHeader("access_token") String access_token) throws AuthorizationFailedException, CouponNotFoundException {
        CustomerAuthEntity customerAuthEntity = couponBusinessService.getCustomerByAccessToken(access_token);
        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in");
        }
        if (customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint");
        }
        if (customerAuthEntity.getExpiresAt() != null) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint");
        }
        if (couponName == "" || couponName == null) {
            throw new CouponNotFoundException("CPF-002", "Coupon name field should not be empty");
        }
        CouponEntity couponEntity = couponBusinessService.getCouponByName(couponName);
        if (couponEntity == null) {
            throw new CouponNotFoundException("CPF-001", "No coupon by this name");
        }
        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse().id(UUID.fromString(couponEntity.getUuid())).couponName(couponEntity.getCouponName()).percent(couponEntity.getPercent());

        return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<OrderList>> getOrderEntity(@RequestHeader("access-token") String accessToken) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = couponBusinessService.getCustomerByAccessToken(accessToken);
        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in");
        }
        if (customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint");
        }
        if (customerAuthEntity.getExpiresAt() != null) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint");
        }
        List<OrdersEntity> orders = orderBusinessService.getOrders(customerAuthEntity.getCustomer().getId());
        ArrayList<Long> orderids = new ArrayList<>();
        for (OrdersEntity order : orders) {
            orderids.add(order.getId());
        }
        List<OrderItemEntity> orderItemEntity = orderBusinessService.getPastOrders(orderids);

        List<OrderList> orderList = new ArrayList<OrderList>();


        OrderListCustomer orderListCustomer = new OrderListCustomer().firstName(customerAuthEntity.getCustomer().getFirstName()).id(UUID.fromString(customerAuthEntity.getCustomer().getUuid())).lastName(customerAuthEntity.getCustomer().getLastName()).emailAddress(customerAuthEntity.getCustomer().getEmail()).contactNumber(customerAuthEntity.getCustomer()
                .getContactNumber());

        for (OrdersEntity order : orders) {

            OrderListCoupon coupon = new OrderListCoupon().id(UUID.fromString(order.getCoupon().getUuid())).couponName(
                    order.getCoupon().getCouponName()).percent(order.getCoupon().getPercent());

            OrderListPayment payment = new OrderListPayment().id(UUID.fromString(order.getPayment().getUuid())).paymentName(order.getPayment().getPaymentName());

            OrderListAddressState addressState = new OrderListAddressState().id(UUID.fromString(order.getAddress().getState().getUuid())).stateName(order.getAddress().getState().getStateName());

            OrderListAddress orderListAddress = new OrderListAddress().id(UUID.fromString(order.getAddress().getUuid())).flatBuildingName(order.getAddress().getFlatBuildingNumber()).locality(order.getAddress().getLocality()).city(order.getAddress().getCity()).pincode(order.getAddress().getPincode()).state(addressState);


            List<ItemQuantityResponse> itemQuantityResponsesList = new ArrayList<ItemQuantityResponse>();
            for (OrderItemEntity item : orderItemEntity) {
                if (order.getId() == item.getOrders().getId()) {
                    ItemQuantityResponseItem itemQuantityResponseItem = new ItemQuantityResponseItem().id(UUID.fromString(item.getItem().getUuid())).itemName(item.getItem().getItemName()).itemPrice(item.getItem().getPrice());
                    ItemQuantityResponse itemQuantityResponse = new ItemQuantityResponse().item(itemQuantityResponseItem).quantity(item.getQuantity()).price(item.getPrice());
                    itemQuantityResponsesList.add(itemQuantityResponse);
                }
            }

            OrderList orderList1 = new OrderList().customer(orderListCustomer).address(orderListAddress).itemQuantities(itemQuantityResponsesList).bill(order.getBill()).coupon(coupon).payment(payment).id(UUID.fromString(order.getUuid())).date(order.getDate().toString()).discount(order.getDiscount());

            orderList.add(orderList1);


        }
        System.out.println(orderList.size());
        return new ResponseEntity<List<OrderList>>(orderList, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.POST, path = "/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveOrderResponse> saveOrder(@RequestHeader("access-token") String access_token, SaveOrderRequest saveOrderRequest) throws AuthorizationFailedException, CouponNotFoundException, AddressNotFoundException, PaymentMethodNotFoundException, RestaurantNotFoundException, ItemNotFoundException {


        CustomerAuthEntity customerAuthEntity = couponBusinessService.getCustomerByAccessToken(access_token);

        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in");
        }
        if (customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint");
        }
        if (customerAuthEntity.getExpiresAt() != null) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint");
        }
        CouponEntity couponEntity = null;
        if (saveOrderRequest.getCouponId() != null) {

            couponEntity = couponBusinessService.getCouponByUUID(saveOrderRequest.getCouponId().toString());
            if (couponEntity == null) {
                throw new CouponNotFoundException("CPF-002", "No coupon by this id");
            }
        }
        AddressEntity addressEntity = null;
        if (saveOrderRequest.getAddressId() != null) {
            addressEntity = orderBusinessService.getAddressByUUID(saveOrderRequest.getAddressId().toString());
            if (addressEntity == null) {
                throw new AddressNotFoundException("ANF-003", "No address by this id");
            }
        }
        PaymentEntity paymentEntity = null;
        if (saveOrderRequest.getPaymentId() != null) {
            paymentEntity = orderBusinessService.getPaymentByUUID(saveOrderRequest.getPaymentId().toString());
            if (paymentEntity == null) {
                throw new PaymentMethodNotFoundException("PNF-002", "No payment method found by this id");
            }
        }
        RestaurantEntity restaurantEntity = null;
        if (saveOrderRequest.getPaymentId() != null) {
            restaurantEntity = orderBusinessService.getRestaurantByUUID(saveOrderRequest.getRestaurantId().toString());
            if (restaurantEntity == null) {
                throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
            }
        }


        OrdersEntity ordersEntity = new OrdersEntity();
        ordersEntity.setAddress(addressEntity);
        ordersEntity.setCoupon(couponEntity);
        ordersEntity.setBill(saveOrderRequest.getBill());
        ordersEntity.setCustomer(customerAuthEntity.getCustomer());
        ordersEntity.setDiscount(saveOrderRequest.getDiscount());
        ordersEntity.setPayment(paymentEntity);
        ordersEntity.setRestaurant(restaurantEntity);
        ordersEntity.setDate(ZonedDateTime.now());
        ordersEntity.setUuid(UUID.randomUUID().toString());
        ordersEntity = orderBusinessService.saveOrder(ordersEntity);
        List<OrderItemEntity> orderItemEntities = new ArrayList<>();

        if (saveOrderRequest.getItemQuantities() != null) {
            List<ItemQuantity> itemQuantityList = saveOrderRequest.getItemQuantities();
            for (ItemQuantity item : itemQuantityList) {
                ItemEntity itemEntity = orderBusinessService.getItemByuuid(item.getItemId().toString());
                if (itemEntity == null) {
                    throw new ItemNotFoundException("INF-003", "No item by this id exist");
                }
                OrderItemEntity orderItemEntity = new OrderItemEntity();
                orderItemEntity.setItem(itemEntity);
                orderItemEntity.setOrders(ordersEntity);
                orderItemEntity.setQuantity(item.getQuantity());
                orderItemEntity.setPrice(item.getPrice());
                orderItemEntities.add(orderItemEntity);
                

            }
        }



        Boolean isSaved = orderBusinessService.saveOrderItem(orderItemEntities);
        if (isSaved) {
            SaveOrderResponse saveOrderResponse = new SaveOrderResponse().id(ordersEntity.getUuid()).status("ORDER SUCCESSFULLY PLACED");
            return new ResponseEntity<SaveOrderResponse>(saveOrderResponse, HttpStatus.OK);
        }

        return null;
    }

}
