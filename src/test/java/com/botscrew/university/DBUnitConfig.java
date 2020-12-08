package com.botscrew.university;

import com.botscrew.university.dao.DepartmentDao;
import com.botscrew.university.dao.LectorDao;
import com.botscrew.university.utils.ConnectionProvider;
import org.dbunit.DBTestCase;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class DBUnitConfig extends DBTestCase {

    protected IDatabaseTester tester;
    private final Properties properties;
    protected IDataSet beforeData;
    protected ConnectionProvider connectionProvider = new ConnectionProvider();
    protected LectorDao lectorDao = new LectorDao(connectionProvider);
    protected DepartmentDao departmentDao = new DepartmentDao(connectionProvider);

    @Before
    public void setUp() throws Exception {
        tester = new JdbcDatabaseTester(properties.getProperty("driver"),
                properties.getProperty("url"),
                properties.getProperty("user"),
                properties.getProperty("password"));
    }

    public DBUnitConfig(String name) throws Exception {
        super(name);
        properties = new Properties();
        URL dataBasePropertyFile = this.getClass().getClassLoader().getResource("config.properties");
        try {
            String pathToPropertyFile = dataBasePropertyFile.getPath();
            FileReader fileReader = new FileReader(pathToPropertyFile);
            properties.load(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, properties.getProperty("driver"));
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, properties.getProperty("url"));
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, properties.getProperty("user"));
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, properties.getProperty("password"));
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA, "");

        TablesCreator tablesCreator = new TablesCreator(connectionProvider);
        tablesCreator.createTables();
    }

    @Override
    protected IDataSet getDataSet() {
        return beforeData;
    }

    @Override
    protected DatabaseOperation getTearDownOperation() {
        return DatabaseOperation.DELETE_ALL;
    }

}
