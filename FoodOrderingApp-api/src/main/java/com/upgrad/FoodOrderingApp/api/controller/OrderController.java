package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.controller.provider.BearerAuthDecoder;
import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/")
@CrossOrigin
public class OrderController {
    @Autowired
    CouponBusinessService couponBusinessService;
    @Autowired
    PaymentService paymentService;
    @Autowired
    OrderService orderService;

    @Autowired
    AddressService addressService;
    @Autowired
    CustomerAuthService customerAuthService;


    @RequestMapping(method = RequestMethod.GET, path = "/order/coupon/{coupon_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<CouponDetailsResponse> getCouponByCouponName(@PathVariable("coupon_name") String couponName, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, CouponNotFoundException, AuthenticationFailedException {
        // conditions to check all the values are not empty
        final BearerAuthDecoder authDecoder = new BearerAuthDecoder(authorization);
        String accessToken = authDecoder.getAccessToken();
        CustomerAuthEntity customerAuthEntity = customerAuthService.getCustomerByToken(accessToken);
        Boolean isAuthorizedUser = customerAuthService.isAuthorizedUser(accessToken,customerAuthEntity);



        if (!isAuthorizedUser) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in");
        }
        if (customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint");
        }
        if (customerAuthEntity.getExpiresAt().isBefore(ZonedDateTime.now())) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint");
        }
        if (couponName == "" || couponName == null) {
            throw new CouponNotFoundException("CPF-002", "Coupon name field should not be empty");
        }
        CouponEntity couponEntity = couponBusinessService.getCouponByCouponName(couponName);
        if (couponEntity == null) {
            throw new CouponNotFoundException("CPF-001", "No coupon by this name");
        }
        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse().id(UUID.fromString(couponEntity.getUuid())).couponName(couponEntity.getCouponName()).percent(couponEntity.getPercent());

        return new ResponseEntity<>(couponDetailsResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getOrderEntity(@RequestHeader("authorization") final String authorization ) throws AuthorizationFailedException, AuthenticationFailedException {
        final BearerAuthDecoder authDecoder = new BearerAuthDecoder(authorization);
        String accessToken = authDecoder.getAccessToken();
        CustomerAuthEntity customerAuthEntity = customerAuthService.getCustomerByToken(accessToken);
        Boolean isAuthorizedUser = customerAuthService.isAuthorizedUser(accessToken,customerAuthEntity);


        if (!isAuthorizedUser) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in");
        }
        if (customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint");
        }
        if (customerAuthEntity.getExpiresAt().isBefore(ZonedDateTime.now())) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint");
        }
        List<OrdersEntity> orders = orderService.getOrdersByCustomers(customerAuthEntity.getCustomer().getUuid());
        ArrayList<Long> orderids = new ArrayList<>();
        for (OrdersEntity order : orders) {
            orderids.add(order.getId());
        }
        List<OrderItemEntity> orderItemEntity = orderService.getPastOrders(orderids);

        List<OrderList> orderList = new ArrayList<OrderList>();


        OrderListCustomer orderListCustomer = new OrderListCustomer().firstName(customerAuthEntity.getCustomer().getFirstName()).id(UUID.fromString(customerAuthEntity.getCustomer().getUuid())).lastName(customerAuthEntity.getCustomer().getLastName()).emailAddress(customerAuthEntity.getCustomer().getEmail()).contactNumber(customerAuthEntity.getCustomer()
                .getContactNumber());

        for (OrdersEntity order : orders) {

            OrderListCoupon coupon = new OrderListCoupon();
            if(order.getCoupon()!=null){
                coupon.id(UUID.fromString(order.getCoupon().getUuid())).couponName(
                        order.getCoupon().getCouponName()).percent(order.getCoupon().getPercent());
            }

            OrderListPayment payment = new OrderListPayment().id(UUID.fromString(order.getPayment().getUuid())).paymentName(order.getPayment().getPaymentName());

            OrderListAddressState addressState = new OrderListAddressState().id(UUID.fromString(order.getAddress().getState().getUuid())).stateName(order.getAddress().getState().getStateName());

            OrderListAddress orderListAddress = new OrderListAddress().id(UUID.fromString(order.getAddress().getUuid())).flatBuildingName(order.getAddress().getFlatBuildingNumber()).locality(order.getAddress().getLocality()).city(order.getAddress().getCity()).pincode(order.getAddress().getPincode()).state(addressState);


            List<ItemQuantityResponse> itemQuantityResponsesList = new ArrayList<ItemQuantityResponse>();
            for (OrderItemEntity orderItemEntity1 : orderItemEntity) {

                if (order.getId() == orderItemEntity1.getOrders().getId()) {

                    ItemQuantityResponseItem itemQuantityResponseItem = new ItemQuantityResponseItem().id(UUID.fromString(orderItemEntity1.getItem().getUuid())).itemName(orderItemEntity1.getItem().getItemName()).itemPrice(orderItemEntity1.getItem().getPrice());
                    ItemQuantityResponse itemQuantityResponse = new ItemQuantityResponse().item(itemQuantityResponseItem).quantity(orderItemEntity1.getQuantity()).price(orderItemEntity1.getPrice());
                    String item = orderItemEntity1.getItem().getType().getValue();

                    itemQuantityResponse.getItem().setType(ItemQuantityResponseItem.TypeEnum.valueOf(item));
                    itemQuantityResponsesList.add(itemQuantityResponse);
                }
            }

            OrderList orderList1 = new OrderList().customer(orderListCustomer).address(orderListAddress).itemQuantities(itemQuantityResponsesList).bill(order.getBill()).coupon(coupon).payment(payment).id(UUID.fromString(order.getUuid())).date(order.getDate().toString()).discount(order.getDiscount());

            orderList.add(orderList1);
        }
        return new ResponseEntity<>(orderList, HttpStatus.OK);


    }

    @RequestMapping(method = RequestMethod.POST, path = "/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveOrderResponse> saveOrder(@RequestHeader("authorization") final String authorization, SaveOrderRequest saveOrderRequest) throws AuthorizationFailedException, CouponNotFoundException, AddressNotFoundException, PaymentMethodNotFoundException, RestaurantNotFoundException, ItemNotFoundException, AuthenticationFailedException {

        final BearerAuthDecoder authDecoder = new BearerAuthDecoder(authorization);
        String accessToken = authDecoder.getAccessToken();
        CustomerAuthEntity customerAuthEntity = customerAuthService.getCustomerByToken(accessToken);
        Boolean isAuthorizedUser = customerAuthService.isAuthorizedUser(accessToken,customerAuthEntity);


        if (!isAuthorizedUser) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in");
        }
        if (customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint");
        }
        if (customerAuthEntity.getExpiresAt().isBefore(ZonedDateTime.now())) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint");
        }
        CouponEntity couponEntity = null;
        if (saveOrderRequest.getCouponId() != null) {
            String CouponId = saveOrderRequest.getCouponId().toString();

            couponEntity = orderService.getCouponByCouponId(CouponId);

            if (couponEntity == null) {
                throw new CouponNotFoundException("CPF-002", "No coupon by this id");
            }

        }

        AddressEntity addressEntity = addressService.getAddressByUUID(saveOrderRequest.getAddressId());

        if (addressEntity == null) {
            throw new AddressNotFoundException("ANF-003", "No address by this id.");
        }


        CustomerAddressEntity customerAddressEntity = addressService.getCustomerAddress(customerAuthEntity.getCustomer(), addressEntity);

        if (customerAddressEntity == null) {
            throw new AuthorizationFailedException("ATHR-004", "You are not authorized to view/update/delete any one else's address");
        }

        PaymentEntity paymentEntity = null;
        if (saveOrderRequest.getPaymentId() != null) {
            paymentEntity = paymentService.getPaymentByUUID(saveOrderRequest.getPaymentId().toString());
            if (paymentEntity == null) {
                throw new PaymentMethodNotFoundException("PNF-002", "No payment method found by this id");
            }
        }
        RestaurantEntity restaurantEntity = null;
        if (saveOrderRequest.getPaymentId() != null) {
            restaurantEntity = orderService.getRestaurantByUUID(saveOrderRequest.getRestaurantId().toString());
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
        ordersEntity = orderService.saveOrder(ordersEntity);

        Boolean isSaved = false;
        if (saveOrderRequest.getItemQuantities() != null) {
            List<ItemQuantity> itemQuantityList = saveOrderRequest.getItemQuantities();

            ItemEntity itemEntity = orderService.getItemByuuid(itemQuantityList.get(0).getItemId().toString());
            if (itemEntity == null) {
                throw new ItemNotFoundException("INF-003", "No item by this id exist");
            }

            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setItem(itemEntity);
            orderItemEntity.setOrders(ordersEntity);
            orderItemEntity.setQuantity(itemQuantityList.get(0).getQuantity());
            orderItemEntity.setPrice(itemQuantityList.get(0).getPrice());
            orderItemEntity = orderService.saveOrderItem(orderItemEntity);

        }


        if (ordersEntity != null) {
            SaveOrderResponse saveOrderResponse = new SaveOrderResponse().id(ordersEntity.getUuid()).status("ORDER SUCCESSFULLY PLACED");
            return new ResponseEntity<>(saveOrderResponse, HttpStatus.OK);
        }

        return null;
    }

}
