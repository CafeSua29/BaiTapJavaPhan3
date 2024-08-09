package model;

import java.util.List;

public class SalesEmployee extends Employee{
    private Shift shift;

    public SalesEmployee() {

    }

    public SalesEmployee(String empId, Gender gender, List<Integer> workDays, Shift shift) {
        super(empId, gender, workDays);
        this.shift = shift;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }
}
