/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clss;

import java.math.BigDecimal;

/**
 *
 * @author HP
 */
public class CarList {

    private String CATEGORY;
    
    private int SEAT;
    private String MODEL;
    private String ENGINE_NO;
    private String VEHICLE_REG;
    private BigDecimal PRICE;
    private String AVAILABILITY;
    private byte[] picture;

    public CarList(String CATEGORY, int SEAT, String MODEL, String ENGINE_NO, String VEHICLE_REG, BigDecimal PRICE, String AVAILABILITY) {
        this.CATEGORY = CATEGORY;
        this.SEAT = SEAT;
        this.MODEL = MODEL;
        this.ENGINE_NO = ENGINE_NO;
        this.VEHICLE_REG = VEHICLE_REG;
        this.PRICE = PRICE;
        this.AVAILABILITY = AVAILABILITY;
    }

    public CarList(String CATEGORY, int SEAT, String MODEL, String ENGINE_NO, String VEHICLE_REG, BigDecimal PRICE, String AVAILABILITY, byte[] picture) {
        this.CATEGORY = CATEGORY;
        this.SEAT = SEAT;
        this.MODEL = MODEL;
        this.ENGINE_NO = ENGINE_NO;
        this.VEHICLE_REG = VEHICLE_REG;
        this.PRICE = PRICE;
        this.AVAILABILITY = AVAILABILITY;
        this.picture = picture;
    }

    
    public String getCATEGORY() {
        return CATEGORY;
    }

    public void setCATEGORY(String CATEGORY) {
        this.CATEGORY = CATEGORY;
    }

    public int getSEAT() {
        return SEAT;
    }

    public void setSEAT(int SEAT) {
        this.SEAT = SEAT;
    }

    public String getMODEL() {
        return MODEL;
    }

    public void setMODEL(String MODEL) {
        this.MODEL = MODEL;
    }

    public String getENGINE_NO() {
        return ENGINE_NO;
    }

    public void setENGINE_NO(String ENGINE_NO) {
        this.ENGINE_NO = ENGINE_NO;
    }

    public String getVEHICLE_REG() {
        return VEHICLE_REG;
    }

    public void setVEHICLE_REG(String VEHICLE_REG) {
        this.VEHICLE_REG = VEHICLE_REG;
    }

    public BigDecimal getPRICE() {
        return PRICE;
    }

    public void setPRICE(BigDecimal PRICE) {
        this.PRICE = PRICE;
    }

    public String getAVAILABILITY() {
        return AVAILABILITY;
    }

    public void setAVAILABILITY(String AVAILABILITY) {
        this.AVAILABILITY = AVAILABILITY;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    

    
}
