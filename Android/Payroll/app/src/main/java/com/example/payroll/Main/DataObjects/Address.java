package com.example.payroll.Main.DataObjects;

import java.io.Serializable;

public class Address implements Serializable {

    private String civicAddress, mailingAddress, city, province, postalCode, country, unitNumber, streetNumber;


    public Address(String unitNumber, String streetNumber,String civic, String city, String province, String postalCode, String country) {
        this.unitNumber = unitNumber;
        this.streetNumber = streetNumber;
        this.civicAddress = civic;
        this.mailingAddress = civic;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.country = country;
    }

    //getters & setters
    public String getCivicAddress() {
        return civicAddress;
    }

    public void setCivicAddress(String civicAddress) {
        this.civicAddress = civicAddress;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUnitNumber(){
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber){
        this.unitNumber = unitNumber;
    }

    public String getStreetNumber(){
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber){
        this.streetNumber = streetNumber;
    }



}
