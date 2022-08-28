package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS user ( id bigint NOT NULL AUTO_INCREMENT," +
                                            " name VARCHAR(45) NOT NULL,  lastName VARCHAR(45) NOT NULL," +
                                            " age smallint NOT NULL,  PRIMARY KEY (id));";
    private final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS user";
    private final String SQL_INSERT_NEW_ROW = "INSERT INTO user (name, lastName, age) VALUES (?, ?, ?)";
    private final String SQL_CLEAN_TABLE = "DELETE FROM user";
    private final String SQL_SELECT_ROWS = "SELECT * FROM user";
    private final Connection connection = Util.getConnection();
    private PreparedStatement preparedStatement;

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try {
            preparedStatement = connection.prepareStatement(SQL_CREATE_TABLE);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try {
            preparedStatement = connection.prepareStatement(SQL_DROP_TABLE);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            preparedStatement = connection.prepareStatement(SQL_INSERT_NEW_ROW);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            preparedStatement.execute();

            System.out.printf("User с именем – '%s' добавлен в базу данных\n", name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try {
            preparedStatement = connection.prepareStatement(SQL_CLEAN_TABLE + " WHERE id = " + id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(SQL_SELECT_ROWS);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User(resultSet.getString("name"),
                                        resultSet.getString("lastName"),
                                        (byte) resultSet.getInt("age"));
                user.setId(resultSet.getLong("id"));
                users.add(user);

                System.out.println(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        try {
            preparedStatement = connection.prepareStatement(SQL_CLEAN_TABLE);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
