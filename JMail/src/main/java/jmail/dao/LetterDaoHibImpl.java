package jmail.dao;

import com.oracle.deploy.update.UpdateCheck;
import com.sun.org.apache.xpath.internal.operations.Lte;
import jmail.model.Letter;
import jmail.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.swing.text.html.parser.Entity;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Глеб on 11.11.2014.
 */
@Repository
public class LetterDaoHibImpl implements LetterDao {

    @Autowired
    private EntityManagerFactory factory;

    @Autowired
    private UserDao userDao;

    @Override
    public Letter findById(int id) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Letter letter = entityManager.find(Letter.class, id);
        transaction.commit();
        return letter;
    }

    @Override
    public List<Letter> findByDateRange(Date start, Date end, int id_user) {
        EntityManager entityManager = factory.createEntityManager();
        String sql = String.format("FROM Letter l WHERE l.send_date BETWEEN %t AND $t", new java.sql.Date(start.getTime()), new java.sql.Date(end.getTime()));
        List<Letter> letters = null;
        try {
            letters = allByUserId(id_user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int startIndex = indexDateStart(start, letters);
        int endIndex = indexDateEnd(end, letters);
        letters = letters.subList(startIndex, endIndex);
        return letters;
    }

    private int indexDateStart(Date start, List<Letter> letters) {
        Iterator<Letter> iter = letters.iterator();
        Letter letter = null;
        int count = 0;
        while(iter.hasNext()) {
            letter = iter.next();
            if(letter.getDate().equals(start)) {
                return count;
            }
            count++;
        }
        return -1;
    }

    private int indexDateEnd(Date end, List<Letter> letters) {
        ListIterator<Letter> iter = letters.listIterator();
        Letter letter = null;
        int count = 0;
        while (iter.hasPrevious()) {
            letter = iter.previous();
            if (letter.getDate().equals(end)) {
                return count;
            }
            count++;
        }
        return -1;
    }

    @Override
    public List<Letter> findByKeyWord(String keyWord, int id_user) {
        List<Letter> letters = null;
        try {
            letters = allByUserId(id_user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Letter.searchByKeyWord(letters, keyWord);
    }


    @Override
    public Letter create(Letter letter) {
        EntityManager entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(letter);
        entityManager.getTransaction();
        return null;
    }

    @Override
    public void update(Letter letter) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(letter);
        transaction.commit();
    }

    @Override
    public void delete(int id) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.remove(findById(id));
        transaction.commit();
    }

    @Override
    public List<Letter> allByUserLogin(String login) throws SQLException {
        User user = userDao.find(login);
        List<Letter> letters = allByUserIdHelper("FROM Letter l WHERE l.from_user = ", user.getId());
        letters.addAll(allByUserIdHelper("FROM Letter l WHERE l.to_user = ", user.getId()));
        return letters;
    }

    @Override
    public List<Letter> allByUserLoginSend(String login) throws SQLException {
        User user = userDao.find(login);
        return allByUserIdHelper("FROM Letter l WHERE l.from_user = ", user.getId());
    }

    @Override
    public List<Letter> allByUserLoginReceived(String login) throws SQLException {
        User user = userDao.find(login);
        return allByUserIdHelper("FROM Letter l WHERE l.to_user = ", user.getId());
    }

    @Override
    public List<Letter> allByUserId(int id) throws SQLException {
        List<Letter> letters = allByUserIdHelper("FROM Letter l WHERE l.from_user = ", id);
        letters.addAll(allByUserIdHelper("FROM Letter l WHERE l.to_user = ", id));
        return letters;
    }

    @Override
    public List<Letter> allByUserIdSend(int id) throws SQLException {
        return allByUserIdHelper("FROM Letter l WHERE l.from_user = ", id);
    }

    @Override
    public List<Letter> allByUserIdReceived(int id) throws SQLException {
        return allByUserIdHelper("FROM Letter l WHERE l.to_user = ", id);
    }

    private List<Letter> allByUserIdHelper(String sql, int id) {
        //EntityManager entityManager = factory.createEntityManager();
        //TypedQuery<Letter> letterTypedQuery = entityManager.createNamedQuery(sql + id, Letter.class);
        return factory.createEntityManager().createNamedQuery(sql + id, Letter.class).getResultList();
    }

}
