package com.botscrew.university.dao;

import com.botscrew.university.domain.Department;
import com.botscrew.university.utils.ConnectionProvider;

import java.sql.*;
import java.util.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class DepartmentDao implements Dao<Department> {

    private final ConnectionProvider connectionProvider;

    public DepartmentDao(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    private static final String GET_DEPARTMENT_BY_ID_SQL = "SELECT * FROM departments WHERE id = ?";
    private static final String GET_ALL_DEPARTMENTS_SQL = "SELECT * FROM departments";
    private static final String CREATE_DEPARTMENT_SQL = "INSERT INTO departments VALUES (DEFAULT, ?, ?)";
    private static final String UPDATE_DEPARTMENT_SQL = "UPDATE departments SET name = ?, head = ? WHERE id = ?";
    private static final String DELETE_DEPARTMENT_SQL = "DELETE FROM departments WHERE id = ?";
    private static final String GET_AVERAGE_SALARY_BY_NAME_SQL = "SELECT AVG(salary) FROM lectors " +
            "LEFT JOIN departments_lectors ON lectors.id = departments_lectors.lector_id " +
            "LEFT JOIN departments ON departments_lectors.department_id = departments.id " +
            "WHERE departments.name = ?";
    private static final String GET_QUANTITY_OF_EMPLOYEE_BY_NAME_SQL = "SELECT COUNT(*) FROM departments_lectors " +
            "LEFT JOIN departments ON departments_lectors.department_id = departments.id " +
            "WHERE departments.name = ?";
    private static final String GET_DEPARTMENT_BY_NAME_SQL = "SELECT * FROM departments WHERE name = ?";
    private static final String GET_DEGREE_STATISTIC_BY_NAME_SQL = "SELECT degree, COUNT(*) FROM lectors " +
            "LEFT JOIN departments_lectors ON lectors.id = departments_lectors.lector_id " +
            "LEFT JOIN departments ON departments_lectors.department_id = departments.id " +
            "WHERE departments.name = ? " +
            "GROUP BY degree";
    private static final String SEARCH_BY_DEPARTMENT_NAME_SQL = "SELECT * FROM departments WHERE name LIKE ?";

    @Override
    public Optional<Department> getById(int id) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_DEPARTMENT_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapToDepartment(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Department> getAll() {
        List<Department> departments = new ArrayList<>();
        try (Connection connection = connectionProvider.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_DEPARTMENTS_SQL)) {
            while (resultSet.next()) {
                departments.add(mapToDepartment(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }

    @Override
    public void save(Department department) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_DEPARTMENT_SQL, RETURN_GENERATED_KEYS)) {
            statement.setString(1, department.getName());
            statement.setInt(2, department.getHead());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    department.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Department department) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_DEPARTMENT_SQL)) {
            statement.setString(1, department.getName());
            statement.setInt(2, department.getHead());
            statement.setInt(3, department.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_DEPARTMENT_SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getAverageSalaryByName(String name) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_AVERAGE_SALARY_BY_NAME_SQL)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getQuantityOfEmployeeByName(String name) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_QUANTITY_OF_EMPLOYEE_BY_NAME_SQL)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Optional<Department> getByName(String name) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_DEPARTMENT_BY_NAME_SQL)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapToDepartment(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Map<String, Integer> getDegreeStatisticByName(String name) {
        Map<String, Integer> result = new HashMap<>();
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_DEGREE_STATISTIC_BY_NAME_SQL)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result.put(resultSet.getString(1), resultSet.getInt(2));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Department> searchIfNameContains(String name) {
        List<Department> result = new ArrayList<>();
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SEARCH_BY_DEPARTMENT_NAME_SQL)) {
            statement.setString(1, "%"+name+"%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(mapToDepartment(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Department mapToDepartment(ResultSet resultSet) throws SQLException {
        return new Department(resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getInt("head"));
    }
}
