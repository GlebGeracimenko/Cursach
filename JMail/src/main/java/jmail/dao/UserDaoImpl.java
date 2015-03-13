package jmail.dao;

import jmail.model.User;
import jmail.util.DBConnectionFactory;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Глеб on 11.11.2014.
 */
public class UserDaoImpl implements UserDao{
    @Override
    public User findById(int id) {
        User user = null;
        PreparedStatement statement = null;
        try(Connection connection = DBConnectionFactory.getConnection()) {
            statement = connection.prepareStatement("SELECT * FROM users as u where u.user_id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            user = getFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User find(String login) {
        User user = null;
        PreparedStatement statement = null;
        try(Connection connection = DBConnectionFactory.getConnection()) {
            statement = connection.prepareStatement("SELECT * FROM users as u where u.login = ?");
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            user = getFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getFromResultSet(ResultSet rs) throws SQLException {

        while (rs.next()){
            return new User(rs.getInt("user_id"), rs.getString("login"), rs.getString("pass"));
        }
        throw new NoSuchElementException();// write own checked exception
    }

    @Override
    public User create(User user) {
        Statement statement = null;
        try(Connection connection = DBConnectionFactory.getConnection()){
            statement = connection.createStatement();
            String query = String.format("INSERT INTO users (login, pass) VALUES ('%s', '%s')", user.getLogin(), user.getPass());
            statement.execute(query);
            ResultSet resultSet = statement.executeQuery("SELECT user_id FROM users WHERE login = '" + user.getLogin() + " ' ");
            while (resultSet.next())
                user.setId(resultSet.getInt("user_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public boolean update(User user) {
        PreparedStatement preparedStatement = null;
        try(Connection connection = DBConnectionFactory.getConnection()) {
            preparedStatement = connection.prepareStatement("UPDATE users SET login = ?, pass = ? WHERE user_id = ?");
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPass());
            preparedStatement.setInt(3, user.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean delete(String login) {
        PreparedStatement preparedStatement = null;
        try(Connection connection = DBConnectionFactory.getConnection()) {
            preparedStatement = connection.prepareStatement("DELETE FROM users where login = ?");
            preparedStatement.setString(1, login);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<User> all() {
        List<User> list = new ArrayList<User>();
        PreparedStatement preparedStatement = null;
        try(Connection connection = DBConnectionFactory.getConnection()) {
            preparedStatement = connection.prepareStatement("SELECT * FROM users");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                list.add(new User(resultSet.getString("login"), resultSet.getString("pass")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
