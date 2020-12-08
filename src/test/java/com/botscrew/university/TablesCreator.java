package com.botscrew.university;

import com.botscrew.university.utils.ConnectionProvider;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

public class TablesCreator {

    private final ConnectionProvider connectionProvider;

    public TablesCreator(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public void createTables() throws FileNotFoundException {
        URL scriptFilePath = this.getClass().getClassLoader().getResource("schema.sql");
        if (scriptFilePath == null) {
            throw new FileNotFoundException("Missing file schema.sql");
        }
        executeScript(scriptFilePath);
    }

    public void fillTables() throws FileNotFoundException {
        URL scriptFilePath = this.getClass().getClassLoader().getResource("data.sql");
        if (scriptFilePath == null) {
            throw new FileNotFoundException("Missing file data.sql");
        }
        executeScript(scriptFilePath);
    }

    private void executeScript(URL scriptFilePath) {
        try (Connection connection = connectionProvider.getConnection()) {
            try {
                ScriptRunner scriptRunner = new ScriptRunner(connection);
                Reader reader = new BufferedReader(new FileReader(scriptFilePath.getPath()));
                scriptRunner.runScript(reader);
                reader.close();

            } catch (Exception exception) {
                System.err.println("Failed to Execute" + scriptFilePath
                        + " The error is " + exception.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
        }
    }
}
