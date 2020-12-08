package com.botscrew.university.service;

import com.botscrew.university.dao.LectorDao;
import com.botscrew.university.domain.Department;
import com.botscrew.university.domain.Lector;
import com.botscrew.university.exception.DepartmentDoesNotExistException;
import com.botscrew.university.exception.EntityNotFoundException;
import com.botscrew.university.exception.SalaryIsNegativeException;

import java.util.List;
import java.util.Optional;

public class LectorService {

    private final LectorDao lectorDao;

    public LectorService(LectorDao lectorDao) {
        this.lectorDao = lectorDao;
    }

    public Optional<Lector> getById(int id) {
        return lectorDao.getById(id);
    }

    public List<Lector> getAll() {
        return lectorDao.getAll();
    }

    public void save(Lector lector) {
        verifySalaryIsNotNegative(lector.getSalary());
        lectorDao.save(lector);
    }

    public void update(Lector lector) {
        verifySalaryIsNotNegative(lector.getSalary());
        verifyLectorPresent(lector.getId());
        lectorDao.update(lector);
    }

    public void delete(int id) {
        lectorDao.delete(id);
    }

    public Lector getHeadOfDepartment(String nameOfDepartment) {
        return lectorDao.getHeadOfDepartment(nameOfDepartment).orElseThrow(() -> new DepartmentDoesNotExistException(
                String.format("Department with name %s is not exist", nameOfDepartment)
        ));
    }

    public List<Lector> searchIfNameContains(String line) {
        return lectorDao.searchIfNameContains(line);
    }

    private void verifySalaryIsNotNegative(int salary) {
        if (salary < 0) {
            throw new SalaryIsNegativeException("Salary can not be negative");
        }
    }

    private void verifyLectorPresent(int id) {
        lectorDao.getById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format("Lector with id %d is not present", id)));
    }
}
