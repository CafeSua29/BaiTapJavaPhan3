package model;

import java.time.LocalDate;

public class Title {
    private int empNo;
    private String titleName;
    private LocalDate fromDate;
    private LocalDate toDate;

    public Title() {

    }

    public Title(int empNo, String titleName, LocalDate fromDate, LocalDate toDate) {
        this.empNo = empNo;
        this.titleName = titleName;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public int getEmpNo() {
        return empNo;
    }

    public void setEmpNo(int empNo) {
        this.empNo = empNo;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
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
