package com.botscrew.university.ui;

import com.botscrew.university.domain.Department;
import com.botscrew.university.domain.Lector;
import com.botscrew.university.service.DepartmentService;
import com.botscrew.university.service.LectorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Math.round;
import static java.lang.System.lineSeparator;


public class Menu {

    private final Scanner scanner = new Scanner(System.in);
    private final DepartmentService departmentService;
    private final LectorService lectorService;

    public Menu(DepartmentService departmentService, LectorService lectorService) {
        this.departmentService = departmentService;
        this.lectorService = lectorService;
    }

    public void printMenu() {
        System.out.println("a. Get head of department" + lineSeparator() +
                "b. Get department statistic" + lineSeparator() +
                "c. Get average salary by department" + lineSeparator() +
                "d. Get number of employees in the department" + lineSeparator() +
                "e. Global search" + lineSeparator());
        System.out.print("CHOOSE ACTION: ");
        String action = scanner.next("[a-e]");
        switch (action) {
            case "a":
                getHeadOfDepartment();
                break;
            case "b": {
                getDepartmentStatistic();
                break;
            }
            case "c": {
                getAverageSalary();
                break;
            }
            case "d": {
                getNumberOfEmployees();
                break;
            }
            case "e": {
                globalSearch();
                break;
            }
        }
    }

    private void getHeadOfDepartment() {
        String nameOfDepartment = getName();
        Lector head = lectorService.getHeadOfDepartment(nameOfDepartment);
        System.out.printf("Head of department is %s %s%n", head.getName(), head.getSurname());
    }

    private void getDepartmentStatistic() {
        String nameOfDepartment = getName();
        Map<String, Integer> statistic = departmentService.getDegreeStatisticByName(nameOfDepartment);
        if (statistic.isEmpty()) {
            System.out.println("There is no employees on faculty");
        } else {
            statistic.forEach((key, value) -> System.out.println(key + ":" + value));
        }
    }

    private void getAverageSalary() {
        String nameOfDepartment = getName();
        System.out.println("Average salary on faculty is "+round(departmentService.getAverageSalaryByName(nameOfDepartment)*100.0)/100.0);
    }

    private void getNumberOfEmployees() {
        String nameOfDepartment = getName();
        System.out.println("Number  of employees in the department is "+departmentService.getQuantityOfEmployeeByName(nameOfDepartment));
    }

    private void globalSearch() {
        System.out.print("Enter template: ");
        String line = scanner.next();
        List<Lector> lectors = new ArrayList<>(lectorService.searchIfNameContains(line));
        List<Department> departments = new ArrayList<>(departmentService.searchIfNameContains(line));
        lectors.forEach(lector -> System.out.println(lector.getName()+" "+lector.getSurname()));
        departments.forEach(department -> System.out.println(department.getName()));
    }

    private String getName() {
        System.out.print("Enter department name: ");
        return scanner.next();
    }
}
