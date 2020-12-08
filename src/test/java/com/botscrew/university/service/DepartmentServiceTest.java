package com.botscrew.university.service;

import com.botscrew.university.dao.DepartmentDao;
import com.botscrew.university.domain.Department;
import com.botscrew.university.domain.Lector;
import com.botscrew.university.exception.DepartmentNameNotUniqueException;
import com.botscrew.university.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private LectorService lectorService;

    @Mock
    private DepartmentDao departmentDao;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    void givenIdOfTheFirstDepartment_whenGetById_thenCalledDepartmentDaoGetByIdReturnedDepartmentWithGivenId() {
        Optional<Department> expectedDepartment = Optional.of(new Department());
        when(departmentDao.getById(1)).thenReturn(expectedDepartment);

        Optional<Department> actualDepartment = departmentService.getById(1);

        verify(departmentDao, times(1)).getById(1);
        assertEquals(expectedDepartment, actualDepartment);
    }

    @Test
    void givenNothing_whenGetAll_thenCalledDepartmentDaoGetAllAndReturnedAllDepartments() {
        List<Department> expectedDepartments = singletonList(new Department());
        when(departmentDao.getAll()).thenReturn(expectedDepartments);

        List<Department> actualDepartments = departmentService.getAll();

        verify(departmentDao, times(1)).getAll();
        assertEquals(expectedDepartments, actualDepartments);
    }

    @Test
    void givenCorrectDepartment_whenSave_thenCalledDepartmentDaoSave() {
        Department department = new Department(1, "Applied math", 1);
        when(lectorService.getById(1)).thenReturn(Optional.of(new Lector()));

        departmentService.save(department);

        verify(departmentDao, times(1)).save(department);
    }

    @Test
    void givenNonExistentDepartmentId_whenSave_thenDepartmentNameNotUniqueExceptionThrown() {
        when(departmentDao.getByName("Biology")).thenReturn(Optional.of(new Department()));
        Throwable exception = assertThrows(DepartmentNameNotUniqueException.class, () -> departmentService.update(new Department(1, "Biology",1)));
        assertEquals("Department with name Biology already exist", exception.getMessage());
        verify(departmentDao, never()).update(new Department());
    }

    @Test
    void givenCorrectDepartment_whenUpdate_thenCalledDepartmentDaoUpdate() {
        Department department = new Department(1, "Applied math", 1);
        when(lectorService.getById(1)).thenReturn(Optional.of(new Lector()));
        when(departmentDao.getById(1)).thenReturn(Optional.of(new Department()));

        departmentService.update(department);

        verify(departmentDao, times(1)).update(department);
    }

    @Test
    void givenNonExistentDepartmentId_whenUpdate_thenEntityNotFoundExceptionThrown() {
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> departmentService.update(new Department()));
        assertEquals("Department with id 0 is not present", exception.getMessage());
        verify(departmentDao, never()).update(new Department());
    }

    @Test
    void givenId_whenDelete_thenCalledDepartmentDaoDelete() {
        departmentService.delete(1);

        verify(departmentDao, times(1)).delete(1);
    }

    @Test
    void givenAppliedMath_whenGetQuantityOfEmployeeByName_thenCalledDepartmentDaoGetQuantityOfEmployeeByName() {
        when(departmentDao.getByName("Applied math")).thenReturn(Optional.of(new Department()));
        when(departmentDao.getQuantityOfEmployeeByName("Applied math")).thenReturn(1000);

        int actualResult = departmentService.getQuantityOfEmployeeByName("Applied math");

        verify(departmentDao, times(1)).getQuantityOfEmployeeByName("Applied math");
        assertEquals(1000, actualResult);
    }

    @Test
    void givenAppliedMath_whenGetAverageSalaryByName_thenCalledDepartmentDaoGetAverageSalaryByName() {
        when(departmentDao.getByName("Applied math")).thenReturn(Optional.of(new Department()));
        when(departmentDao.getAverageSalaryByName("Applied math")).thenReturn(1000.0);

        double actualResult = departmentService.getAverageSalaryByName("Applied math");

        verify(departmentDao, times(1)).getAverageSalaryByName("Applied math");
        assertEquals(1000.0, actualResult);
    }

    @Test
    void givenAppliedMath_whenGetDegreeStatisticByName_thenCalledDepartmentDaoGetDegreeStatisticByName() {
        Map<String, Integer> expectedResult = new HashMap<>();
        expectedResult.put("professor",1);
        when(departmentDao.getByName("Applied math")).thenReturn(Optional.of(new Department()));
        when(departmentDao.getDegreeStatisticByName("Applied math")).thenReturn(expectedResult);

        Map<String, Integer> actualResult = departmentService.getDegreeStatisticByName("Applied math");

        verify(departmentDao, times(1)).getDegreeStatisticByName("Applied math");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void givenString_whenSearchIfContains_thenCalledDeparmentDaoSearchIfContainsAndReturnedListOfDepartments() {
        when(departmentDao.searchIfNameContains("some")).thenReturn(singletonList(new Department(1, "some", 1)));

        List<Department> actualResult = departmentService.searchIfNameContains("some");

        verify(departmentDao, times(1)).searchIfNameContains("some");
        assertEquals(singletonList(new Department(1, "some", 1)), actualResult);
    }
}