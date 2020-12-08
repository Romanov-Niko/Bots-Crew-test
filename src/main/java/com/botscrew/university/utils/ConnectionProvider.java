package com.botscrew.university.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionProvider {

    private String connectionUrl;
    private String userName;
    private String password;

    public ConnectionProvider() throws FileNotFoundException {
        prepareDataForConnection();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionUrl, userName, password);
    }

    private void prepareDataForConnection() throws FileNotFoundException {
        Properties properties = new Properties();
        URL dataBasePropertyFile = this.getClass().getClassLoader().getResource("config.properties");
        if (dataBasePropertyFile == null) {
            throw new FileNotFoundException("Missing file config.properties");
        }
        try {
            String pathToPropertyFile = dataBasePropertyFile.getPath();
            FileReader fileReader = new FileReader(pathToPropertyFile);
            properties.load(fileReader);
            connectionUrl = properties.getProperty("url");
            userName = properties.getProperty("user");
            password = properties.getProperty("password");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
