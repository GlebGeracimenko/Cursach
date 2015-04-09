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
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Глеб on 11.11.2014.
 */
@Repository
public class LetterDaoHibImpl implements LetterDao {

    private class Sort implements Comparator<Letter>{

        @Override
        public int compare(Letter o1, Letter o2) {
            return o1.getId() - o2.getId();
        }

    }


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
        List<Letter> letters = null;
        try {
            letters = allByUserId(id_user);
            Collections.sort(letters, new Sort());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int startIndex = indexDateStart(start, letters);
        int endIndex = indexDateEnd(end, letters);
        letters = letters.subList(startIndex, endIndex + 1);
        return letters;
    }

    private int indexDateStart(Date start, List<Letter> letters) {
        Iterator<Letter> iter = letters.iterator();
        Letter letter = null;
        int count = 0;
        Timestamp timestamp = new Timestamp(start.getTime());
        timestamp.setNanos(0);
        while(iter.hasNext()) {
            letter = iter.next();
            if(letter.getDate().compareTo(timestamp) == 1 || letter.getDate().compareTo(timestamp) == 0) {
                return count;
            }
            count++;
        }
        return -1;
    }

    private int indexDateEnd(Date end, List<Letter> letters) {
        Letter letter = null;
        Timestamp timestamp = new Timestamp(end.getTime());
        timestamp.setNanos(0);
        for(int i = letters.size() - 1; i >= 0; i--) {
            letter = letters.get(i);
            if (letter.getDate().compareTo(timestamp) == -1 || letter.getDate().compareTo(timestamp) == 0) {
                return i;
            }
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
        letters = Letter.searchByKeyWord(letters, keyWord);
        return letters;
    }


    @Override
    public Letter create(Letter letter) {
        EntityManager entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(letter);
        entityManager.getTransaction().commit();
        return letter;
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
        Letter letter = findById(id);
        entityManager.remove(entityManager.contains(letter) ? letter : entityManager.merge(letter));
        transaction.commit();
    }

    @Override
    public List<Letter> allByUserLogin(String login) throws SQLException {
        User user = userDao.find(login);
        List<Letter> letters = allByUserIdHelper("FROM Letter WHERE from_user = ", user.getId());
        letters.addAll(allByUserIdHelper("FROM Letter WHERE to_user = ", user.getId()));
        return letters;
    }

    @Override
    public List<Letter> allByUserLoginSend(String login) throws SQLException {
        User user = userDao.find(login);
        return allByUserIdHelper("FROM Letter WHERE from_user = ", user.getId());
    }

    @Override
    public List<Letter> allByUserLoginReceived(String login) throws SQLException {
        User user = userDao.find(login);
        return allByUserIdHelper("FROM Letter WHERE to_user = ", user.getId());
    }

    @Override
    public List<Letter> allByUserId(int id) throws SQLException {
        List<Letter> letters = allByUserIdHelper("FROM Letter WHERE from_user = ", id);
        letters.addAll(allByUserIdHelper("FROM Letter WHERE to_user = ", id));
        return letters;
    }

    @Override
    public List<Letter> allByUserIdSend(int id) throws SQLException {
        return allByUserIdHelper("FROM Letter WHERE from_user = ", id);
    }

    @Override
    public List<Letter> allByUserIdReceived(int id) throws SQLException {
        return allByUserIdHelper("FROM Letter WHERE to_user = ", id);
    }

    private List<Letter> allByUserIdHelper(String sql, int id) {
        //EntityManager entityManager = factory.createEntityManager();
        //TypedQuery<Letter> letterTypedQuery = entityManager.createNamedQuery(sql + id, Letter.class);
        return factory.createEntityManager().createQuery(sql + id, Letter.class).getResultList();
    }

}
