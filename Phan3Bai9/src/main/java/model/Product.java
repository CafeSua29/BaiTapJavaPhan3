package model;

public class Product {
    private String prodId;
    private String prodName;
    private ProductType type;
    private double price;

    public Product() {

    }

    public Product(String prodId, String prodName, ProductType type, double price) {
        this.prodId = prodId;
        this.prodName = prodName;
        this.type = type;
        this.price = price;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
