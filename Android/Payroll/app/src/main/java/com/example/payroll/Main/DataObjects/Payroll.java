package com.example.payroll.Main.DataObjects;

import java.io.Serializable;
import java.util.Date;

public class Payroll implements Serializable {

    private double gross;
    private double net;
    private double EI;
    private double cpp;
    private double federal;
    private double provincial;

    private Date startDate;
    private Date endDate;
    
    public Payroll(double gross, double net, double EI, double CPP,double federal,double provincial, Date startDate, Date endDate) {
        this.gross = gross;
        this.net = net;
        this.EI = EI;
        this.cpp = CPP;
        this.federal = federal;
        this.provincial = provincial;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public Payroll(Date startDate, Date endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }


    //Getters & Setters
    public double getBaseWage() {
        return gross;
    }

    public void setBaseWage(double baseWage) {
        this.gross = baseWage;
    }

    public double getGross() {
        return gross;
    }

    public void setGross(double gross) {
        this.gross = gross;
    }

    public double getNet() {
        return net;
    }

    public void setNet(double net) {
        this.net = net;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getEI() {
        return EI;
    }

    public void setEI(double eI) {
        EI = eI;
    }

    public double getCpp() {
        return cpp;
    }

    public void setCpp(double cpp) {
        this.cpp = cpp;
    }

    public double getFederal() {
        return federal;
    }

    public void setFederal(double federal) {
        this.federal = federal;
    }

    public double getProvincial() {
        return provincial;
    }

    public void setProvincial(double provincial) {
        this.provincial = provincial;
    }
}
