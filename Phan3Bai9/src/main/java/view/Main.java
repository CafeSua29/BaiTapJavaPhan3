package view;

import model.Employee;
import model.Gender;
import model.ImportEmployee;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ImportEmployee ie = new ImportEmployee("1", Gender.M, new ArrayList<>(), 1300);
        Employee ie2 = new ImportEmployee("2", Gender.F, new ArrayList<>(), 20);

        System.out.println(ie.getEmpId());
        System.out.println(ie2.getEmpId());
        System.out.println(ie.getGender());
        System.out.println(ie2.getGender());
        System.out.println(ie.getWorkDays());
        System.out.println(ie2.getWorkDays());
        System.out.println(ie.getSeniority());
        //System.out.println(ie2.getSeniority());
    }
}
