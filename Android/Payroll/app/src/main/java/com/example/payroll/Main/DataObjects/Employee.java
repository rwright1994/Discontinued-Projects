package com.example.payroll.Main.DataObjects;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Employee implements Serializable {


    private String firstName, middlename, lastName;
    private int age;
    public double salary;
    private double hourlyWage;
    private Address address;
    private int claimCode;
    private int SIN;
    private Date dateOfBirth;
    private Date startDate;
    private ArrayList<Payroll> payrollList;
    private Payroll lastPayroll;

    public Employee(String firstName, String lastName, Address address, double hourlyWage, int SIN, Date dateOfBirth, Date startDate) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.hourlyWage = hourlyWage;
        this.claimCode = 0;
        this.SIN = SIN;
        this.dateOfBirth = dateOfBirth;
        this.startDate = startDate;
        this.payrollList = new ArrayList<Payroll>();
    }

    public Employee(String firstName, String lastName, Address address, double salary,double hourlyWage, int SIN) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.hourlyWage = hourlyWage;
        this.salary = salary;
        this.claimCode = 0;
        this.SIN = SIN;
        this.payrollList = new ArrayList<Payroll>();
    }


    public Employee(String firstName, String middleName, String lastName, Address address, double hourlyWage, int SIN, Date dateOfBirth, Date startDate) {

        this.firstName = firstName;
        this.middlename = middleName;
        this.lastName = lastName;
        this.address = address;
        this.hourlyWage = hourlyWage;
        this.claimCode = 0;
        this.SIN = SIN;
        this.dateOfBirth = dateOfBirth;
        this.startDate = startDate;
        this.payrollList = new ArrayList<Payroll>();
    }



    public double getPay(double hours) {

        return this.getHourlyWage() * hours;
    }

    public void addPayroll(Payroll payroll) {
        // Check array size.
        if(payrollList.size() == 0){
            payrollList.add(payroll);
            lastPayroll = payroll;
        }else{
            payrollList.add(payroll);
            lastPayroll = payroll;
        }
    }


    public ArrayList<Payroll> getPayroll(){
        return this.payrollList;
    }

    public Payroll getLastPayroll(){
        return lastPayroll;
    }

    public String getLastPayrollDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yyyy");
        String fDate = formatter.format(lastPayroll.getEndDate());
       return fDate;
    }


    //Getter's and Setters

    public String getName() {
        return firstName + " "  + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName(){ return middlename;}

    public void setMiddlename(String middleName){this.middlename=middleName;}

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHourlyWage() {
        return hourlyWage;
    }

    public void setHourlyWage(double hourlyWage) {
        this.hourlyWage = hourlyWage;
    }

    public double getSalary(){return salary;}


    public Address getAddressObj() {
        return address;
    }

    public void getAddress() {

    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getSIN() {
        return SIN;
    }

    public void setSIN(int sIN) {
        SIN = sIN;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setClaimCode(int num) {
        this.claimCode = num;
    }

    public int getClaimCode() {
        return claimCode;
    }






}
