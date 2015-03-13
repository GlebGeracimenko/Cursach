package jmail.dao;

import jmail.model.Letter;
import jmail.util.DBConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

/**
 * Created by Глеб on 11.11.2014.
 */
public class LetterDaoImpl implements LetterDao {


    private UserDao userDao = new UserDaoImpl();

    @Override
    public Letter findById(int id) {
        return null;
    }

    @Override
    public Letter findByDateRange(Date start, Date end) {
        return null;
    }

    @Override
    public List<Letter> findByKeyWord(String keyWord) {
        return null;
    }

    @Override
    public void create(Letter letter) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = DBConnectionFactory.getConnection();
            statement = connection.createStatement();
            String query = String.format("INSERT INTO letters (title, body, to_user, from_user, send_date) VALUES " +
                            "('%s', '%s', '%s', '%s', '%s')", letter.getTitle(), letter.getBody(), letter.getTo().getId(),
                    letter.getFrom().getId(), new java.sql.Date(System.currentTimeMillis()).toString());
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void update(Letter letter) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public List<Letter> allByUserLogin(String login) {
        return null;
    }
}
