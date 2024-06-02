/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clss;

import java.util.Date;

/**
 *
 * @author HP
 */
public class ReturnedCar {

    String csutomerName;
    String carRegNo;
    Date bookedDate, arrivalDate, retrnedDate;
    int late;

    public ReturnedCar(String csutomerName, String carRegNo, Date bookedDate, Date arrivalDate) {
        this.csutomerName = csutomerName;
        this.carRegNo = carRegNo;
        this.bookedDate = bookedDate;
        this.arrivalDate = arrivalDate;
    }

    public ReturnedCar(String csutomerName, String carRegNo, Date bookedDate, Date arrivalDate, Date retrnedDate, int late) {
        this.csutomerName = csutomerName;
        this.carRegNo = carRegNo;
        this.bookedDate = bookedDate;
        this.arrivalDate = arrivalDate;
        this.retrnedDate = retrnedDate;
        this.late = late;
    }

    public String getCsutomerName() {
        return csutomerName;
    }

    public void setCsutomerName(String csutomerName) {
        this.csutomerName = csutomerName;
    }

    public String getCarRegNo() {
        return carRegNo;
    }

    public void setCarRegNo(String carRegNo) {
        this.carRegNo = carRegNo;
    }

    public Date getRetrnedDate() {
        return retrnedDate;
    }

    public void setRetrnedDate(Date retrnedDate) {
        this.retrnedDate = retrnedDate;
    }

    public Date getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(Date bookedDate) {
        this.bookedDate = bookedDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public int getLate() {
        return late;
    }

    public void setLate(int late) {
        this.late = late;
    }

}
