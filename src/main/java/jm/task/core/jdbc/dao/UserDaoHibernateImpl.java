package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS users ( id bigint NOT NULL AUTO_INCREMENT," +
                                            " name VARCHAR(45) NOT NULL,  lastName VARCHAR(45) NOT NULL," +
                                            " age smallint NOT NULL,  PRIMARY KEY (id));";
    private final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS users";
    private final SessionFactory sessionFactory = Util.getSessionFactory();
    private Session session = null;

    public UserDaoHibernateImpl() {

    }

    private void procedureUsersTable(String sqlQuery) {
        try {
            if (sessionFactory != null) {
                session = sessionFactory.openSession();
                session.beginTransaction();

                session.createSQLQuery(sqlQuery).executeUpdate();

                session.getTransaction().commit();
            }
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void createUsersTable() {
        procedureUsersTable(SQL_CREATE_TABLE);
    }

    @Override
    public void dropUsersTable() {
        procedureUsersTable(SQL_DROP_TABLE);
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try {
            if (sessionFactory != null) {
                session = sessionFactory.openSession();
                session.beginTransaction();

                User user = new User(name, lastName, age);
                session.persist(user);

                session.getTransaction().commit();

                System.out.println(user);
            }
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try {
            if (sessionFactory != null) {
                session = sessionFactory.openSession();
                session.beginTransaction();

                User user = session.get(User.class, id);
                session.remove(user);

                session.getTransaction().commit();
            }
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            if (sessionFactory != null) {
                session = sessionFactory.openSession();
                session.beginTransaction();

                List<User> users = session.createQuery("FROM User").getResultList();

                for (User user : users) {
                    System.out.println(user);
                }

                session.getTransaction().commit();

                return users;
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return null;
    }

    @Override
    public void cleanUsersTable() {
        try {
            if (sessionFactory != null) {
                session = sessionFactory.openSession();
                session.beginTransaction();

                session.createQuery("DELETE FROM User ").executeUpdate();

                session.getTransaction().commit();
            }
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
