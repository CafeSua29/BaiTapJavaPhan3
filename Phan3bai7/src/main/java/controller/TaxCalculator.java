package controller;

public class TaxCalculator {
    public static int calculateTax(int salary) {
        int tax = 0;

        if (salary <= 40000) {
            return tax;
        } else if (salary <= 60000) {
            tax = (salary - 40000) * 5 / 100;
        } else if (salary <= 90000) {
            tax = 20000 * 5 / 100 + (salary - 60000) * 10 / 100;
        } else {
            tax = 20000 * 5 / 100 + 30000 * 10 / 100 + (salary - 90000) * 15 / 100;
        }

        return tax;
    }
}
