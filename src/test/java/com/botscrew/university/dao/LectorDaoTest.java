package com.botscrew.university.dao;

import com.botscrew.university.DBUnitConfig;
import com.botscrew.university.DBUnitConfigParameterResolver;
import com.botscrew.university.domain.Lector;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(DBUnitConfigParameterResolver.class)
class LectorDaoTest extends DBUnitConfig {

    public LectorDaoTest(String name) throws Exception {
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
    void givenId_whenGetById_thenReturnedLectorWithGivenId() {
        Lector expectedLector = new Lector(1, "First", "Lector", "assistant", 1000);

        Lector actualLector = lectorDao.getById(1).orElse(null);

        assertEquals(expectedLector, actualLector);
    }

    @Test
    void givenNonExistentId_whenGetById_thenReturnedOptionalEmpty() {
        Optional<Lector> actualLector = lectorDao.getById(4);

        assertEquals(Optional.empty(), actualLector);
    }

    @Test
    void givenNothing_whenGetAll_thenReturnedAllLectors() {
        List<Lector> expectedLectors = new ArrayList<>();
        expectedLectors.add(new Lector(1, "First", "Lector", "assistant",1000));
        expectedLectors.add(new Lector(2, "Second", "Lector", "associate professor",2000));
        expectedLectors.add(new Lector(3, "Third", "Lector", "professor",3000));

        List<Lector> actualLectors = lectorDao.getAll();

        assertEquals(expectedLectors, actualLectors);
    }

    @Test
    void givenLector_whenSave_thenAddedGivenLector() throws Exception {
        lectorDao.save(new Lector("NEW", "Lector", "assistant",1200));

        String file = getClass().getClassLoader().getResource("LectorDao/add.xml").getFile();
        IDataSet expectedData = new FlatXmlDataSetBuilder().build(new File(file));
        ITable expectedTable = expectedData.getTable("lectors");

        ITable actualTable = tester.getConnection().createDataSet().getTable("lectors");

        Assertion.assertEquals(expectedTable, actualTable);
    }

    @Test
    void givenLector_whenUpdate_thenUpdatedLectorWithEqualId() throws Exception {
        lectorDao.update(new Lector(3, "UPDATED", "Lector", "professor",3000));

        String file = getClass().getClassLoader().getResource("LectorDao/update.xml").getFile();
        IDataSet expectedData = new FlatXmlDataSetBuilder().build(new File(file));
        ITable expectedTable = expectedData.getTable("lectors");

        ITable actualTable = tester.getConnection().createDataSet().getTable("lectors");

        Assertion.assertEquals(expectedTable, actualTable);
    }

    @Test
    void givenId3_whenDelete_thenDeletedThirdLector() throws Exception {
        lectorDao.delete(3);

        String file = getClass().getClassLoader().getResource("LectorDao/delete.xml").getFile();
        IDataSet expectedData = new FlatXmlDataSetBuilder().build(new File(file));
        ITable expectedTable = expectedData.getTable("lectors");

        ITable actualTable = tester.getConnection().createDataSet().getTable("lectors");

        Assertion.assertEquals(expectedTable, actualTable);
    }

    @Test
    void givenBiology_whenGetHeadOfDepartment_thenReturnedThirdLector() {
        Lector expectedLector = new Lector(3, "Third", "Lector", "professor", 3000);

        Lector actualLector = lectorDao.getHeadOfDepartment("Biology").orElse(null);

        assertEquals(expectedLector, actualLector);
    }

    @Test
    void givenNonExistentDepartment_whenGetHeadOfDepartment_thenReturnedOptionalEmpty() {
        Optional<Lector> actualLector = lectorDao.getHeadOfDepartment("NON EXISTENT");

        assertEquals(Optional.empty(), actualLector);
    }

    @Test
    void givenTor_whenSearchIfContains_thenReturnedListOfAllLectors() {
        List<Lector> expectedResult = new ArrayList<>();
        expectedResult.add(new Lector(1, "First", "Lector", "assistant",1000));
        expectedResult.add(new Lector(2, "Second", "Lector", "associate professor",2000));
        expectedResult.add(new Lector(3, "Third", "Lector", "professor",3000));

        List<Lector> actualResult = lectorDao.searchIfNameContains("tor");

        assertEquals(expectedResult, actualResult);
    }
}