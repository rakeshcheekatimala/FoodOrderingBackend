package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name="payment")
@NamedQueries({
        @NamedQuery(name = "GetPaymentMethods", query = "select u from PaymentEntity  u "),
        @NamedQuery(name="getPaymentByUUID",query = "select u from PaymentEntity  u where u.uuid=:uuid")
})
public class PaymentEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "UUID")
    private String uuid;

    @Column(name = "PAYMENT_NAME")
    @Size(max = 255)
    @NotNull
    private String paymentName;
public PaymentEntity(){}
    public PaymentEntity(String paymentId, String spmePayment) {
    }

    public Long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }
}
