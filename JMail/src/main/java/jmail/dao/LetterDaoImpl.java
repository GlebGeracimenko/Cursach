package jmail.dao;

import jmail.model.Letter;
import jmail.model.User;
import jmail.util.DBConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LetterDaoImpl implements LetterDao {


    private UserDao userDao = new UserDaoImpl();

    @Override
    public Letter findById(int id) {
        Letter letter = null;
        PreparedStatement preparedStatement = null;
        try(Connection connection = DBConnectionFactory.getConnection()) {
            preparedStatement = connection.prepareStatement("SELECT * FROM letters as l WHERE l.letter_id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                letter = getLetter(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return letter;
    }

    private Letter getLetter(ResultSet resultSet) throws SQLException {
        return new Letter(resultSet.getInt("letter_id"), resultSet.getString("title"), resultSet.getString("body"),
                    userDao.findById(resultSet.getInt("to_user")), userDao.findById(resultSet.getInt("from_user")), resultSet.getDate("send_date"));
    }

    @Override
    public List<Letter> findByDateRange(Date start, Date end, int id_user) {
        PreparedStatement preparedStatement = null;
        List<Letter> letters = new ArrayList<>();
        try(Connection connection = DBConnectionFactory.getConnection()) {
            preparedStatement = connection.prepareStatement("SELECT * FROM letters WHERE send_date BETWEEN ? AND ?");
            preparedStatement.setDate((int)1, new java.sql.Date(start.getTime()));
            preparedStatement.setDate((int)2, new java.sql.Date(end.getTime()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                letters.add(getLetter(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return letters;
    }

    @Override
    public List<Letter> findByKeyWord(String keyWord, int id_user) {
        List<Letter> letters = null;
        try {
            letters = allByUserId(id_user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        letters = Letter.searchByKeyWord(letters, keyWord);

        return letters;
    }



    @Override
    public Letter create(Letter letter) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO  letters (title, body, to_user, from_user, send_date) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, letter.getTitle());
            preparedStatement.setString(2, letter.getBody());
            preparedStatement.setInt(3, letter.getTo().getId());
            preparedStatement.setInt(4, letter.getFrom().getId());
            preparedStatement.setDate(5, new java.sql.Date(System.currentTimeMillis()));
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.executeQuery("SELECT MAX(letter_id) FROM letters");
            if (resultSet.next()) {
                letter.setId(resultSet.getInt("letter_id"));
            }
            //from == send && to == received
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
        return  letter;
    }

    @Override
    public void update(Letter letter) {
        PreparedStatement preparedStatement = null;
        try(Connection connection = DBConnectionFactory.getConnection()) {
            preparedStatement = connection.prepareStatement("UPDATE letters SET title = ?, body = ?, to_user = ?, from_user = ? WHERE id = ?");
            preparedStatement.setString(1, letter.getTitle());
            preparedStatement.setString(2, letter.getBody());
            preparedStatement.setInt(3, letter.getTo().getId());
            preparedStatement.setInt(4, letter.getFrom().getId());
            preparedStatement.setInt(5, letter.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        PreparedStatement preparedStatement = null;
        try(Connection connection = DBConnectionFactory.getConnection()) {
            //Letter letter = findById(id);
            preparedStatement = connection.prepareStatement("DELETE FROM letters WHERE letter_id = ?");
            preparedStatement.setInt((int)1, id);
            preparedStatement.executeUpdate();
            //from == send && to == received
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Letter> allByUserLogin(String login) throws SQLException {
        User user = userDao.find(login);
        List<Letter> letters = allByUserId(user.getId());
        return letters;
    }

    @Override
    public List<Letter> allByUserLoginSend(String login) throws SQLException {
        User user = userDao.find(login);
        return allByUserIdSend(user.getId());
    }

    @Override
    public List<Letter> allByUserLoginReceived(String login) throws SQLException {
        User user = userDao.find(login);
        return allByUserId(user.getId());
    }

    @Override
    public List<Letter> allByUserId(int id) throws SQLException {
        List<Letter> letters = allByUserIdHelper(id, "SELECT * FROM letters WHERE from_user = ?");
        letters.addAll(allByUserIdHelper(id, "SELECT * FROM letters WHERE to_user = ?"));
        return letters;
    }

    @Override
    public List<Letter> allByUserIdSend(int id) throws SQLException {
        return allByUserIdHelper(id, "SELECT * FROM letters WHERE from_user = ?");
    }

    @Override
    public List<Letter> allByUserIdReceived(int id) throws SQLException {
        return allByUserIdHelper(id, "SELECT * FROM letters WHERE to_user = ?");
    }

    private List<Letter> allByUserIdHelper(int id, String sql) {
        PreparedStatement preparedStatement = null;
        List<Letter> letters = new ArrayList<>();
        try (Connection connection = DBConnectionFactory.getConnection()) {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                letters.add(getLetter(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return letters;
    }

}
