package ecommerce.system.api.entities;

import ecommerce.system.api.models.AddressModel;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "AddressEntity")
@Table(name = "tb_address")
public class AddressEntity {

    @Id
    @Column(name = "pk_addressId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int addressId;

    @Column(name = "country")
    private String country;

    @Column(name = "postalCode")
    private String postalCode;

    @Column(name = "address")
    private String address;

    @Column(name = "number")
    private int number;

    @Column(name = "stateCode")
    private String stateCode;

    @Column(name = "city")
    private String city;

    @Column(name = "district")
    private String district;

    @Column(name = "complement")
    private String complement;

    @Column(name = "creationDate")
    private LocalDateTime creationDate;

    @Column(name = "lastUpdate")
    private LocalDateTime lastUpdate;

    @Column(name = "isActive")
    private boolean active;

    public AddressEntity() {
    }

    public AddressEntity(AddressModel address) {
        this.addressId = address.getAddressId();
        this.country = address.getCountry();
        this.postalCode = address.getPostalCode();
        this.address = address.getAddress();
        this.number = address.getNumber();
        this.stateCode = address.getStateCode();
        this.city = address.getCity();
        this.district = address.getDistrict();
        this.complement = address.getComplement();
        this.creationDate = address.getCreationDate();
        this.lastUpdate = address.getLastUpdate();
        this.active = address.isActive();
    }

    public AddressModel toModel() {
        return new AddressModel(
                this.addressId,
                this.country,
                this.postalCode,
                this.address,
                this.number,
                this.stateCode,
                this.city,
                this.district,
                this.complement,
                this.creationDate,
                this.lastUpdate,
                this.active
        );
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
