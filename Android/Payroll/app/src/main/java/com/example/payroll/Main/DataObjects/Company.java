package com.example.payroll.Main.DataObjects;

import com.example.payroll.Main.DataObjects.Address;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Company implements Serializable {

    private String name;
    private Address address;
    private List<Employee> employeeList = new ArrayList<>();

    public Company(String name,Address address){
        this.name = name;
        this.address = address;
    }

    public void addEmployee(Employee employee) {


        employeeList.add(employee);

    }

    public void removeEmployee(Employee employee) {
        employeeList.indexOf(employee);

    }

    public void getEmployeesList() {
        for(Employee emp: employeeList) {
            System.out.println(emp.getFirstName());
        }
    }

    public Employee getEmployee(String name) {
        Employee emp = null;
        for(Employee e: employeeList) {
            if(name.equals(e.getFirstName() + " " + e.getLastName())) {
                emp = e;
            }
        }
        return emp;
    }

    public Employee getEmployee(int num) {

        return employeeList.get(num);
    }

    public int getNumEmployees() {
        return employeeList.size();
    }

    //gets setters
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public List<Employee> getEmployeeList() {
        return employeeList;
    }


    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

}
