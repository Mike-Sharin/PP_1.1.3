package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        // реализуйте алгоритм здесь

        try(Connection connection = new Util().getConnection()) {

            UserDao userDao = new UserDaoJDBCImpl();

            userDao.createUsersTable();

            userDao.saveUser("Alex", "Bolduin", (byte) 20);
            userDao.saveUser("Bob", "Faler", (byte) 25);
            userDao.saveUser("Elena", "Prekrasnaya", (byte) 31);
            userDao.saveUser("Jim", "Tonic", (byte) 38);

            userDao.removeUserById(1);
            userDao.getAllUsers();
            userDao.cleanUsersTable();
            userDao.dropUsersTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
