/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clss;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author HP
 */
public class rentCar {

    int RENTID;
    String CARREG;
    String CUSTOMER_NAME;
    Date RENTDATE;
    Date RETURNDATE;
    BigDecimal RENTFEE;

    public rentCar(int RENTID, String CARREG, String CUSTOMER_NAME, Date RENTDATE, Date RETURNDATE, BigDecimal RENTFEE) {
        this.RENTID = RENTID;
        this.CARREG = CARREG;
        this.CUSTOMER_NAME = CUSTOMER_NAME;
        this.RENTDATE = RENTDATE;
        this.RETURNDATE = RETURNDATE;
        this.RENTFEE = RENTFEE;
    }

    public int getRENTID() {
        return RENTID;
    }

    public void setRENTID(int RENTID) {
        this.RENTID = RENTID;
    }

    public String getCARREG() {
        return CARREG;
    }

    public void setCARREG(String CARREG) {
        this.CARREG = CARREG;
    }

    public String getCUSTOMER_NAME() {
        return CUSTOMER_NAME;
    }

    public void setCUSTOMER_NAME(String CUSTOMER_NAME) {
        this.CUSTOMER_NAME = CUSTOMER_NAME;
    }

    public Date getRENTDATE() {
        return RENTDATE;
    }

    public void setRENTDATE(Date RENTDATE) {
        this.RENTDATE = RENTDATE;
    }

    public Date getRETURNDATE() {
        return RETURNDATE;
    }

    public void setRETURNDATE(Date RETURNDATE) {
        this.RETURNDATE = RETURNDATE;
    }

    public BigDecimal getRENTFEE() {
        return RENTFEE;
    }

    public void setRENTFEE(BigDecimal RENTFEE) {
        this.RENTFEE = RENTFEE;
    }

}
