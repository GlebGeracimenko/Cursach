package jmail;

import jmail.dao.UserDao;
import jmail.dao.UserDaoHibImpl;
import jmail.dao.UserDaoImpl;
import jmail.model.User;
import jmail.util.DBConnectionFactory;

import java.sql.*;

/**
 * Created by Глеб on 11.11.2014.
 */
public class Test {
    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl();
        User user = new User("Vasi", "pass");
        UserDao userDao1 = new UserDaoHibImpl();
        userDao1.create(user);
    }
}
