package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    // реализуйте настройку соеденения с БД

    private static final String URL = "jdbc:mysql://localhost:3306/preproject";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public Util() {
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SessionFactory getSessionFactory() {
        try {
            Properties properties = new Properties();

            properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");

            properties.put(Environment.URL, URL);
            properties.put(Environment.USER, USERNAME);
            properties.put(Environment.PASS, PASSWORD);

            properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");

            properties.put(Environment.SHOW_SQL, true);
            properties.put(Environment.FORMAT_SQL, true);
            properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
            //hibernate.hbm2ddl.auto=create-drop

            return new Configuration().addProperties(properties).addAnnotatedClass(User.class).buildSessionFactory();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
