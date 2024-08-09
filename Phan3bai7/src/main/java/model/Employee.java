package model;

import java.sql.Date;
import java.time.LocalDate;

public class Employee {
    private int empNo;
    private String fullName;
    private Gender gender;
    private String title;
    private String deptName;
    private int salary;
    private Date hireDate;

    public Employee() {

    }

    public Employee(int empNo, String fullName, Gender gender, String title, String deptName, int salary, Date hireDate) {
        this.empNo = empNo;
        this.fullName = fullName;
        this.gender = gender;
        this.title = title;
        this.deptName = deptName;
        this.salary = salary;
        this.hireDate = hireDate;
    }

    public int getEmpNo() {
        return empNo;
    }

    public void setEmpNo(int empNo) {
        this.empNo = empNo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }
}
