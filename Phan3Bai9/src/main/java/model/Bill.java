package model;

import java.time.LocalDate;
import java.util.List;

public class Bill {
    private String billId;
    private String salesEmployeeId;
    private String cusId;
    private List<Product> productList;
    private double totalPrice;
    private LocalDate date;

    public Bill() {

    }

    public Bill(String billId, String salesEmployeeId, String cusId, List<Product> productList, double totalPrice, LocalDate date) {
        this.billId = billId;
        this.salesEmployeeId = salesEmployeeId;
        this.cusId = cusId;
        this.productList = productList;
        this.totalPrice = totalPrice;
        this.date = date;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getSalesEmployeeId() {
        return salesEmployeeId;
    }

    public void setSalesEmployeeId(String salesEmployeeId) {
        this.salesEmployeeId = salesEmployeeId;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
