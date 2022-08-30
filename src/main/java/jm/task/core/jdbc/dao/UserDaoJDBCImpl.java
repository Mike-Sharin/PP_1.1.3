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
    //@SuppressWarnings("FieldCanBeLocal")
    private final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS users ( id bigint NOT NULL AUTO_INCREMENT," +
                                            " name VARCHAR(45) NOT NULL,  lastName VARCHAR(45) NOT NULL," +
                                            " age smallint NOT NULL,  PRIMARY KEY (id));";
    private final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS users";
    private final String SQL_INSERT_NEW_ROW = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
    private final String SQL_CLEAN_TABLE = "DELETE FROM users";
    private final String SQL_SELECT_ROWS = "SELECT * FROM users";
    private final Connection connection = Util.getConnection();
    private PreparedStatement preparedStatement;

    public UserDaoJDBCImpl() {

    }

    private void procedureUserTable(String sql_create_table) {
        try {
            if (connection != null) {
                connection.setAutoCommit(false);
                preparedStatement = connection.prepareStatement(sql_create_table);
                preparedStatement.execute();
                connection.commit();
                connection.setAutoCommit(true);
            }
        } catch (SQLException e ) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public void createUsersTable() {
        procedureUserTable(SQL_CREATE_TABLE);
    }

    public void dropUsersTable() {
        procedureUserTable(SQL_DROP_TABLE);
    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            if (connection != null) {
                connection.setAutoCommit(false);
                preparedStatement = connection.prepareStatement(SQL_INSERT_NEW_ROW);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, lastName);
                preparedStatement.setByte(3, age);

                preparedStatement.executeUpdate();
                connection.commit();
                connection.setAutoCommit(true);

                System.out.printf("User с именем – '%s' добавлен в базу данных\n", name);
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        procedureUserTable(SQL_CLEAN_TABLE + " WHERE id = " + id);
    }

    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();
        try {
            if (connection != null) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public void cleanUsersTable() {
        procedureUserTable(SQL_CLEAN_TABLE);
    }
}
