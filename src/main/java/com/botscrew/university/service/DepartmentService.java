package com.botscrew.university.service;

import com.botscrew.university.dao.DepartmentDao;
import com.botscrew.university.domain.Department;
import com.botscrew.university.exception.DepartmentNameNotUniqueException;
import com.botscrew.university.exception.EntityNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DepartmentService {

    private final LectorService lectorService;
    private final DepartmentDao departmentDao;

    public DepartmentService(LectorService lectorService, DepartmentDao departmentDao) {
        this.lectorService = lectorService;
        this.departmentDao = departmentDao;
    }

    public Optional<Department> getById(int id) {
        return departmentDao.getById(id);
    }

    public List<Department> getAll() {
        return departmentDao.getAll();
    }

    public void save(Department department) {
        verifyDepartmentUnique(department);
        verifyHeadPresent(department.getId());
        departmentDao.save(department);
    }

    public void update(Department department) {
        verifyDepartmentUnique(department);
        verifyDepartmentPresentById(department.getId());
        verifyHeadPresent(department.getId());
        departmentDao.update(department);
    }

    public void delete(int id) {
        departmentDao.delete(id);
    }

    public int getQuantityOfEmployeeByName(String nameOfDepartment) {
        verifyDepartmentPresentByName(nameOfDepartment);
        return departmentDao.getQuantityOfEmployeeByName(nameOfDepartment);
    }

    public double getAverageSalaryByName(String nameOfDepartment) {
        verifyDepartmentPresentByName(nameOfDepartment);
        return departmentDao.getAverageSalaryByName(nameOfDepartment);
    }

    public Map<String, Integer> getDegreeStatisticByName(String nameOfDepartment) {
        verifyDepartmentPresentByName(nameOfDepartment);
        return departmentDao.getDegreeStatisticByName(nameOfDepartment);
    }

    public List<Department> searchIfNameContains(String line) {
        return departmentDao.searchIfNameContains(line);
    }

    private void verifyDepartmentPresentByName(String nameOfDepartment) {
        departmentDao.getByName(nameOfDepartment).orElseThrow(() -> new EntityNotFoundException(
                String.format("Department with name %s is not present", nameOfDepartment)));
    }

    private void verifyDepartmentPresentById(int id) {
        departmentDao.getById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format("Department with id %d is not present", id)));
    }

    private void verifyDepartmentUnique(Department department) {
        departmentDao.getByName(department.getName()).ifPresent(departmentWithSameName -> {
            if (department.getId() != departmentWithSameName.getId()) {
                throw new DepartmentNameNotUniqueException(String.format("Department with name %s already exist", department.getName()));
            }
        });
    }

    private void verifyHeadPresent(int id) {
        lectorService.getById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Lector with id %d is not present", id)));
    }
}
