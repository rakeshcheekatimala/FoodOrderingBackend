package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.ToStringExclude;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "customer")
@NamedQueries(value = {
        @NamedQuery(name = "CustomerEntity.customerByContactNumber", query = "select ce from CustomerEntity ce where ce.contactNumber = :contactNumber"),
        @NamedQuery(name = "CustomerEntity.customerByUuid", query = "SELECT c from CustomerEntity c where c.uuid = :uuid")
})
public class CustomerEntity implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "CONTACT_NUMBER")
    @Size(max = 30)
    private String contactNumber;

    @Column(name = "EMAIL")
    @Size(max = 50)
    @NotNull
    private String email;

    @Column(name = "FIRSTNAME")
    @Size(max = 30)
    private String firstName;

    @Column(name = "LASTNAME")
    @Size(max = 30)
    private String lastName;

    @Column(name = "PASSWORD")
    @NotNull
    @Size(max = 255)
    @ToStringExclude
    private String password;

    @Column(name = "UUID")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "SALT")
    @NotNull
    @Size(max = 255)
    @ToStringExclude
    private String salt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }


}
