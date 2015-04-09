package jmail.dao;

import jmail.model.Letter;
import jmail.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.UUID;

/**
 * Created by GlebGeracimenko on 22.11.2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/app-context.xml"})
public class UserDaoHibTest {

    @Autowired
    private UserDao userDao;



    private User user1 = null;
    private User user2 = null;

    @Test
    public void addUser() {
        String loginUser1 = UUID.randomUUID().toString();
        String loginUser2 = UUID.randomUUID().toString();
        user1 = userDao.create(new User(loginUser1, "1234"));
        user2 = userDao.create(new User(loginUser2, "4321"));
    }

    @Test
    public void findById() {
        User user = userDao.findById(21);
        System.out.println(user);
    }

    @Test
    public void find() {
        User user = userDao.find("44d94f20-7e00-4e9c-8228-eef48f18f4d1");
        System.out.println(user);
    }

    @Test
    public void delete() {
        boolean bool = userDao.delete("Podlesniy");
        System.out.println(bool);
    }

    @Test
    public void update() {
        boolean bool = userDao.update(new User(150, "Sergey12345", "lala"));
        System.out.println(bool);
    }

    @Test
    public void getAll() {
        List<User> allUsers = userDao.all();
        for(User u : allUsers) {
            System.out.println("Users: " + u);
        }
    }

}
