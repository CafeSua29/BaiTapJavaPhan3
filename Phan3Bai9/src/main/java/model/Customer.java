package model;

public class Customer {
    private String cusId;
    private Gender gender;
    private int age;

    public Customer() {

    }

    public Customer(String cusId, Gender gender, int age) {
        this.cusId = cusId;
        this.gender = gender;
        this.age = age;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
