package com.botscrew.university.domain;

import java.util.Objects;

public class Department {

    private int id;
    private String name;
    private int head;

    public Department() {
    }

    public Department(String name, int head) {
        this.name = name;
        this.head = head;
    }

    public Department(int id, String name, int head) {
        this(name, head);
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

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;
        Department that = (Department) o;
        return id == that.id &&
                head == that.head &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, head);
    }
}
