package model;

import java.time.LocalDate;

public class Salary {
    private int empNo;
    private int salaryAmount;
    private LocalDate fromDate;
    private LocalDate toDate;

    public Salary() {

    }

    public Salary(int empNo, int salaryAmount, LocalDate fromDate, LocalDate toDate) {
        this.empNo = empNo;
        this.salaryAmount = salaryAmount;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public int getEmpNo() {
        return empNo;
    }

    public void setEmpNo(int empNo) {
        this.empNo = empNo;
    }

    public int getSalaryAmount() {
        return salaryAmount;
    }

    public void setSalaryAmount(int salaryAmount) {
        this.salaryAmount = salaryAmount;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }
}
