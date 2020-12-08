package com.botscrew.university.exception;

public class SalaryIsNegativeException extends RuntimeException {

    public SalaryIsNegativeException(String errorMessage) {
        super(errorMessage);
    }
}
