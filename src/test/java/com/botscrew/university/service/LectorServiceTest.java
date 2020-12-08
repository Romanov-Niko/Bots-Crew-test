package com.botscrew.university.service;

import com.botscrew.university.dao.LectorDao;
import com.botscrew.university.domain.Department;
import com.botscrew.university.domain.Lector;
import com.botscrew.university.domain.Lector;
import com.botscrew.university.exception.DepartmentDoesNotExistException;
import com.botscrew.university.exception.DepartmentNameNotUniqueException;
import com.botscrew.university.exception.EntityNotFoundException;
import com.botscrew.university.exception.SalaryIsNegativeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class LectorServiceTest {

    @Mock
    private LectorDao lectorDao;

    @InjectMocks
    private LectorService lectorService;

    @Test
    void givenIdOfTheFirstLector_whenGetById_thenCalledLectorDaoGetByIdReturnedLectorWithGivenId() {
        Optional<Lector> expectedLector = Optional.of(new Lector());
        when(lectorDao.getById(1)).thenReturn(expectedLector);

        Optional<Lector> actualLector = lectorService.getById(1);

        verify(lectorDao, times(1)).getById(1);
        assertEquals(expectedLector, actualLector);
    }

    @Test
    void givenNothing_whenGetAll_thenCalledLectorDaoGetAllAndReturnedAllLectors() {
        List<Lector> expectedLectors = singletonList(new Lector());
        when(lectorDao.getAll()).thenReturn(expectedLectors);

        List<Lector> actualLectors = lectorService.getAll();

        verify(lectorDao, times(1)).getAll();
        assertEquals(expectedLectors, actualLectors);
    }

    @Test
    void givenCorrectLector_whenSave_thenCalledLectorDaoSave() {
        Lector expectedLector = new Lector(1, "some", "great", "lector", 1);

        lectorService.save(expectedLector);

        verify(lectorDao, times(1)).save(expectedLector);
    }

    @Test
    void givenNegativeSalary_whenSave_thenSalaryIsNegativeExceptionThrown() {
        Throwable exception = assertThrows(SalaryIsNegativeException.class, () -> lectorService.update(new Lector(1, "","","",-20)));
        assertEquals("Salary can not be negative", exception.getMessage());
        verify(lectorDao, never()).save(new Lector(1, "","","",-20));
    }

    @Test
    void givenCorrectLector_whenUpdate_thenCalledLectorDaoUpdate() {
        Lector expectedLector = new Lector(1, "some", "great", "lector", 1);
        when(lectorDao.getById(1)).thenReturn(Optional.of(expectedLector));

        lectorService.update(expectedLector);

        verify(lectorDao, times(1)).update(expectedLector);
    }

    @Test
    void givenNonExistentLectorId_whenUpdate_thenEntityNotFoundExceptionThrown() {
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> lectorService.update(new Lector()));
        assertEquals("Lector with id 0 is not present", exception.getMessage());
        verify(lectorDao, never()).update(new Lector());
    }

    @Test
    void givenId_whenDelete_thenCalledLectorDaoDelete() {
        lectorService.delete(1);

        verify(lectorDao, times(1)).delete(1);
    }

    @Test
    void givenBiology_whenGetHeadOfDepartment_thenCalledLectorDaoGetHeadOfDepartment() {
        Lector expectedLector = new Lector(1, "some", "great", "lector", 1);
        when(lectorDao.getHeadOfDepartment("Biology")).thenReturn(Optional.of(expectedLector));

        lectorService.getHeadOfDepartment("Biology");

        verify(lectorDao, times(1)).getHeadOfDepartment("Biology");
    }

    @Test
    void givenNonExistentDepartment_whenGetHeadOfDepartment_thenDepartmentDoesNotExistExceptionThrown() {
        Throwable exception = assertThrows(DepartmentDoesNotExistException.class, () -> lectorService.getHeadOfDepartment("some"));
        assertEquals("Department with name some is not exist", exception.getMessage());
    }

    @Test
    void givenString_whenSearchIfContains_thenCalledLectorDaoSearchIfContainsAndReturnedListOfLectors() {
        when(lectorService.searchIfNameContains("some")).thenReturn(singletonList(new Lector(1, "some", "test", "lector", 1)));

        List<Lector> actualResult = lectorService.searchIfNameContains("some");

        verify(lectorDao, times(1)).searchIfNameContains("some");
        assertEquals(singletonList(new Lector(1, "some", "test", "lector", 1)), actualResult);
    }
}