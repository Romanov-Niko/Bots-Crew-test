package com.botscrew.university.exception;

public class DepartmentNameNotUniqueException extends RuntimeException {

    public DepartmentNameNotUniqueException(String errorMessage) {
        super(errorMessage);
    }
}
