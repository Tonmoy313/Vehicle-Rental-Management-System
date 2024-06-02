/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clss;

/**
 *
 * @author Asus
 */
public class customerList {

    private String Customer_name, phone, nid, License, Driver_hire;
    private byte[] picture;

    public customerList(String Customer_name, String phone, String nid, String License, String dhire, byte[] pic) {
        this.Customer_name = Customer_name;
        this.phone = phone;
        this.nid = nid;
        this.License = License;
        this.Driver_hire = dhire;
        this.picture = pic;
    }

    public customerList(String Customer_name, String phone, String nid, String License, String dhire) {
        this.Customer_name = Customer_name;
        this.phone = phone;
        this.nid = nid;
        this.License = License;
        this.Driver_hire = dhire;
    }

    public customerList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getCustomer_name() {
        return Customer_name;
    }

    public void setCustomer_name(String Customer_name) {
        this.Customer_name = Customer_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getLicense() {
        return License;
    }

    public void setLicense(String License) {
        this.License = License;
    }

    public String getDriver_hire() {
        return Driver_hire;
    }

    public void setDriver_hire(String Driver_hire) {
        this.Driver_hire = Driver_hire;
    }

}
