package com.botscrew.university.domain;

import java.util.Objects;

public class Lector {

    private int id;
    private String name;
    private String surname;
    private String degree;
    private int salary;

    public Lector() {
    }

    public Lector(String name, String surname, String degree, int salary) {
        this.name = name;
        this.surname = surname;
        this.degree = degree;
        this.salary = salary;
    }

    public Lector(int id, String name, String surname, String degree, int salary) {
        this(name, surname, degree, salary);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lector)) return false;
        Lector lector = (Lector) o;
        return id == lector.id &&
                salary == lector.salary &&
                name.equals(lector.name) &&
                surname.equals(lector.surname) &&
                degree.equals(lector.degree);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, degree, salary);
    }
}
