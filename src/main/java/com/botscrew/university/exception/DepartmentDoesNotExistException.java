package com.botscrew.university.exception;

public class DepartmentDoesNotExistException extends RuntimeException {

    public DepartmentDoesNotExistException(String errorMessage) {
        super(errorMessage);
    }
}
