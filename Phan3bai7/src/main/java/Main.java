import controller.EmployeeService;
import controller.MonthlyTaxTask;

public class Main {
    public static void main(String[] args) {
        EmployeeService.start();

        MonthlyTaxTask.createTaxTableIfNotExists();
        MonthlyTaxTask.calculateTax();
    }
}
