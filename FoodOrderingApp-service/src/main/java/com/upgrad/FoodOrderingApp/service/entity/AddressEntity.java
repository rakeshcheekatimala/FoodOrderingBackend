package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "address")
@NamedQueries(
        {
                @NamedQuery(name = "AddressEntity.findAddressByUuid", query = "select ad from AddressEntity ad where ad.uuid=:uuid")
        }
)
public class AddressEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "uuid")
    @Size(max = 200)
    private String uuid;

    @Column(name = "FLAT_BUIL_NUMBER")
    @Size(max = 255)
    private String flatBuildingNumber;

    @Column(name = "LOCALITY")
    @Size(max = 255)
    private String locality;

    @Column(name = "CITY")
    @Size(max = 30)
    private String city;

    @Column(name = "PINCODE")
    @Size(max = 30)
    private String pincode;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private StateEntity state;

    @Column(name = "active")
    private Integer active;
    public AddressEntity(){

    }
    public AddressEntity(String addressId, String s, String someLocality, String someCity, String s1, StateEntity stateEntity) {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFlatBuildingNumber() {
        return flatBuildingNumber;
    }

    public void setFlatBuildingNumber(String flatBuildingNumber) {
        this.flatBuildingNumber = flatBuildingNumber;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public StateEntity getState() {
        return state;
    }

    public void setState(StateEntity state) {
        this.state = state;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }
}
