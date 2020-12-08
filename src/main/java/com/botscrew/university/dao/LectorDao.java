package com.botscrew.university.dao;

import com.botscrew.university.domain.Department;
import com.botscrew.university.domain.Lector;
import com.botscrew.university.utils.ConnectionProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class LectorDao implements Dao<Lector> {

    private final ConnectionProvider connectionProvider;

    public LectorDao(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    private static final String GET_LECTOR_BY_ID_SQL = "SELECT * FROM lectors WHERE id = ?";
    private static final String GET_ALL_LECTORS_SQL = "SELECT * FROM lectors";
    private static final String CREATE_LECTOR_SQL = "INSERT INTO lectors VALUES (DEFAULT, ?, ?, ?, ?)";
    private static final String UPDATE_LECTOR_SQL = "UPDATE lectors SET name = ?, surname = ?, degree = ?, salary = ? WHERE id = ?";
    private static final String DELETE_LECTOR_SQL = "DELETE FROM lectors WHERE id = ?";
    private static final String GET_HEAD_OF_DEPARTMENT_SQL = "SELECT * FROM lectors " +
            "LEFT JOIN departments ON lectors.id = head " +
            "WHERE departments.name = ?";
    private static final String SEARCH_BY_LECTOR_NAME_SQL = "SELECT * FROM lectors WHERE name LIKE ? " +
            "OR surname LIKE ?";

    @Override
    public Optional<Lector> getById(int id) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_LECTOR_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapToLector(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Lector> getAll() {
        List<Lector> lectors = new ArrayList<>();
        try (Connection connection = connectionProvider.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_LECTORS_SQL)) {
            while (resultSet.next()) {
                lectors.add(mapToLector(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lectors;
    }

    @Override
    public void save(Lector lector) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_LECTOR_SQL, RETURN_GENERATED_KEYS)) {
            statement.setString(1, lector.getName());
            statement.setString(2, lector.getSurname());
            statement.setString(3, lector.getDegree());
            statement.setInt(4, lector.getSalary());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    lector.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Lector lector) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_LECTOR_SQL)) {
            statement.setString(1, lector.getName());
            statement.setString(2, lector.getSurname());
            statement.setString(3, lector.getDegree());
            statement.setInt(4, lector.getSalary());
            statement.setInt(5, lector.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_LECTOR_SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Lector> getHeadOfDepartment(String nameOfDepartment) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_HEAD_OF_DEPARTMENT_SQL)) {
            statement.setString(1, nameOfDepartment);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapToLector(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Lector> searchIfNameContains(String line) {
        List<Lector> result = new ArrayList<>();
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SEARCH_BY_LECTOR_NAME_SQL)) {
            statement.setString(1, "%"+line+"%");
            statement.setString(2, "%"+line+"%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(mapToLector(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Lector mapToLector(ResultSet resultSet) throws SQLException {
        return new Lector(resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("surname"),
                resultSet.getString("degree"),
                resultSet.getInt("salary"));
    }
}
