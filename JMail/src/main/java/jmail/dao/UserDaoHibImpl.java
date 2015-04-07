package jmail.dao;

import jmail.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Transactional
@Repository
public class UserDaoHibImpl implements UserDao {

    @Autowired
    private EntityManagerFactory factory;// = Persistence.createEntityManagerFactory("my_unit");

    @Override
    public User findById(int id) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        User user  = entityManager.find(User.class, id);
        transaction.commit();
        return user;
    }

    @Override
    public User find(String login) {
        EntityManager entityManager = factory.createEntityManager();
        Query query = entityManager.createQuery("FROM User u WHERE u.login = '" + login + "'", User.class);
        User user = (User)query.getSingleResult();
        return user;
    }

    @Override
    @Transactional
    public User create(User user) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(user);
        transaction.commit();
        return user;
    }

    @Override
    public boolean update(User user) {
        EntityManager em = factory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.merge(user);
        transaction.commit();
        return true;
    }

    /*em.getTransaction().begin();
        Query query = em.createQuery("UPDATE User u SET u.login = :login, u.pass = :pass WHERE u.id = :id");
        int k = query.setParameter("login", user.getLogin()).setParameter("pass", user.getPass()).setParameter("id", user.getId()).executeUpdate();
        /*if (findById(user.getId()) != null) {
            em.merge(user);
            em.getTransaction().commit();
            return true;
        }
        return false;*/

    @Override
    public boolean delete(String login) {
        EntityManager em = factory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.remove(find(login));
        transaction.commit();
        return true;
        /*em.getTransaction().begin();
        Query query = em.createQuery("DELETE User u WHERE u.login = :login");
        int k = query.setParameter("login", login).executeUpdate();
        //em.remove((User)find(login));
        em.getTransaction().commit();
        return true;*/
    }

    @Override
    public List<User> all() {
        EntityManager em = factory.createEntityManager();
        TypedQuery<User> userTypedQuery = em.createNamedQuery("User.getAll", User.class);
        return userTypedQuery.getResultList();
    }
}
