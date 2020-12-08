package com.botscrew.university;

import com.botscrew.university.dao.DepartmentDao;
import com.botscrew.university.dao.LectorDao;
import com.botscrew.university.service.DepartmentService;
import com.botscrew.university.service.LectorService;
import com.botscrew.university.ui.Menu;
import com.botscrew.university.utils.ConnectionProvider;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        ConnectionProvider connectionProvider = new ConnectionProvider();
        LectorDao lectorDao = new LectorDao(connectionProvider);
        DepartmentDao departmentDao = new DepartmentDao(connectionProvider);
        LectorService lectorService = new LectorService(lectorDao);
        DepartmentService departmentService = new DepartmentService(lectorService, departmentDao);
        Menu menu = new Menu(departmentService, lectorService);
        Scanner scanner = new Scanner(System.in);
        String exit;
        do {
            menu.printMenu();
            System.out.println("Enter exit to exit or whatever to continue");
            exit = scanner.next();
        } while (!exit.equals("exit"));
    }
}
