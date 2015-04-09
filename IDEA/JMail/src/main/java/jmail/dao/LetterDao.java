package jmail.dao;

import jmail.model.Letter;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 04.10.2014.
 */
public interface LetterDao {

    public Letter findById(int id);

    public List<Letter> findByDateRange(Date start, Date end, int id_user);

    /*
    * Find all letters that consist keyWord in title or in body
    * */
    public List<Letter> findByKeyWord(String keyWord, int id_user);

    public Letter create(Letter letter);

    public void update(Letter letter);

    public void delete(int id);

    public List<Letter> allByUserLogin(String login) throws SQLException;

    public List<Letter> allByUserLoginSend(String login) throws SQLException;

    public List<Letter> allByUserLoginReceived(String login) throws SQLException;

    public List<Letter> allByUserId(int id) throws SQLException;

    public List<Letter> allByUserIdSend(int id) throws SQLException;

    public List<Letter> allByUserIdReceived(int id) throws SQLException;

}
