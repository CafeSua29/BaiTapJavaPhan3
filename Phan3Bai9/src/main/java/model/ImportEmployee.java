package model;

import java.util.List;

public class ImportEmployee extends Employee{
    private int seniority;

    public ImportEmployee() {

    }

    public ImportEmployee(String empId, Gender gender, List<Integer> workDays, int seniority) {
        super(empId, gender, workDays);
        this.seniority = seniority;
    }

    public int getSeniority() {
        return seniority;
    }

    public void setSeniority(int seniority) {
        this.seniority = seniority;
    }
}
