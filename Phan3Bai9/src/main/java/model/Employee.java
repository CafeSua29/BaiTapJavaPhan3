package model;

import java.util.List;

public class Employee {
    private String empId;
    private Gender gender;
    private List<Integer> workDays;

    public Employee() {

    }

    public Employee(String empId, Gender gender, List<Integer> workDays) {
        this.empId = empId;
        this.gender = gender;
        this.workDays = workDays;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public List<Integer> getWorkDays() {
        return workDays;
    }

    public void setWorkDays(List<Integer> workDays) {
        this.workDays = workDays;
    }
}
