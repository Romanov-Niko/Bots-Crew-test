package com.botscrew.university.dao;

import com.botscrew.university.DBUnitConfig;
import com.botscrew.university.DBUnitConfigParameterResolver;
import com.botscrew.university.domain.Department;
import com.botscrew.university.domain.Lector;
import org.apache.commons.collections.list.AbstractLinkedList;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.util.*;

@ExtendWith(DBUnitConfigParameterResolver.class)
class DepartmentDaoTest extends DBUnitConfig {

    public DepartmentDaoTest(String name) throws Exception {
        super(name);
    }

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        String file = getClass().getClassLoader().getResource("preparedDataset.xml").getFile();
        beforeData = new FlatXmlDataSetBuilder().build(new File(file));
        tester.setDataSet(beforeData);
        tester.onSetup();
    }

    @Test
    void givenId_whenGetById_thenReturnedDepartmentWithGivenId() {
        Department expectedDepartment = new Department(1, "Applied math", 1);

        Department actualDepartment = departmentDao.getById(1).orElse(null);

        assertEquals(expectedDepartment, actualDepartment);
    }

    @Test
    void givenNonExistentId_whenGetById_thenReturnedOptionalEmpty() {
        Optional<Department> actualDepartment = departmentDao.getById(4);

        assertEquals(Optional.empty(), actualDepartment);
    }

    @Test
    void givenNothing_whenGetAll_thenReturnedAllDepartments() {
        List<Department> expectedDepartments = new ArrayList<>();
        expectedDepartments.add(new Department(1, "Applied math", 1));
        expectedDepartments.add(new Department(2, "Physics", 2));
        expectedDepartments.add(new Department(3, "Biology", 3));

        List<Department> actualDepartments = departmentDao.getAll();

        assertEquals(expectedDepartments, actualDepartments);
    }

    @Test
    void givenDepartment_whenSave_thenAddedGivenDepartment() throws Exception {
        departmentDao.save(new Department(4, "NEW", 2));

        String file = getClass().getClassLoader().getResource("DepartmentDao/add.xml").getFile();
        IDataSet expectedData = new FlatXmlDataSetBuilder().build(new File(file));
        ITable expectedTable = expectedData.getTable("departments");

        ITable actualTable = tester.getConnection().createDataSet().getTable("departments");

        Assertion.assertEquals(expectedTable, actualTable);
    }

    @Test
    void givenDepartment_whenUpdate_thenUpdatedDepartmentWithEqualId() throws Exception {
        departmentDao.update(new Department(3, "UPDATED", 3));

        String file = getClass().getClassLoader().getResource("DepartmentDao/update.xml").getFile();
        IDataSet expectedData = new FlatXmlDataSetBuilder().build(new File(file));
        ITable expectedTable = expectedData.getTable("departments");

        ITable actualTable = tester.getConnection().createDataSet().getTable("departments");

        Assertion.assertEquals(expectedTable, actualTable);
    }

    @Test
    void givenId3_whenDelete_thenDeletedThirdDepartment() throws Exception {
        departmentDao.delete(3);

        String file = getClass().getClassLoader().getResource("DepartmentDao/delete.xml").getFile();
        IDataSet expectedData = new FlatXmlDataSetBuilder().build(new File(file));
        ITable expectedTable = expectedData.getTable("departments");

        ITable actualTable = tester.getConnection().createDataSet().getTable("departments");

        Assertion.assertEquals(expectedTable, actualTable);
    }

    @Test
    void givenBiology_whenGetAverageSalaryByName_thenReturned3000() {
        double actualAverageSalary = departmentDao.getAverageSalaryByName("Biology");

        assertEquals(3000.0, actualAverageSalary);
    }

    @Test
    void givenBiology_whenGetQuantityOfEmployeeByName_thenReturned1() {
        int actualQuantityOfEmployee = departmentDao.getQuantityOfEmployeeByName("Biology");

        assertEquals(1, actualQuantityOfEmployee);
    }

    @Test
    void givenBiology_whenGetByName_thenReturnedDepartmentOfBiology() {
        Department expectedDepartment = new Department(3, "Biology", 3);

        Department actualDepartment = departmentDao.getByName("Biology").orElse(null);

        assertEquals(expectedDepartment, actualDepartment);
    }

    @Test
    void givenBiology_whenGetDegreeStatisticByName_thenReturnedMapWithKeyProfessorAndValue1() {
        Map<String, Integer> expectedResult = new HashMap<>();
        expectedResult.put("professor", 1);

        Map<String, Integer> actualResult = departmentDao.getDegreeStatisticByName("Biology");

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void givenOlo_whenSearchIfContains_thenReturnedListWithBiologyDepartment() {
        List<Department> expectedResult = new ArrayList<>();
        expectedResult.add(new Department(3, "Biology", 3));

        List<Department> actualResult = departmentDao.searchIfNameContains("olo");

        assertEquals(expectedResult, actualResult);
    }
}