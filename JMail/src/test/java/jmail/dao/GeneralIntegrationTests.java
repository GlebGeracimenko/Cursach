package jmail.dao;

import jdk.nashorn.internal.ir.annotations.Ignore;
import jmail.model.Letter;
import jmail.model.User;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Глеб on 14.11.2014.
 */
@Ignore
@FixMethodOrder(MethodSorters.DEFAULT)
public class GeneralIntegrationTests {

    private static UserDao userDao = new UserDaoImpl();
    private static LetterDao letterDao = new LetterDaoImpl();

    private static User user1 = null;
    private static User user2 = null;

    @BeforeClass
    public static void init() {
        String loginUser1 = UUID.randomUUID().toString();
        String loginUser2 = UUID.randomUUID().toString();
        user1 = userDao.create(new User(loginUser1, "1234"));
        user2 = userDao.create(new User(loginUser2, "4321"));
        Assert.assertNotNull(userDao.find(user1.getLogin()));
        Assert.assertNotNull(userDao.find(user2.getLogin()));
        Assert.assertNotNull(userDao.findById(user1.getId()));
        Assert.assertNotNull(userDao.findById(user2.getId()));
    }

    @Test
    public void delete_user() {
        userDao.delete(user1.getLogin());
    }

    @Test
    public void all_user() {
        userDao.all();
    }

    @Test
    public void _2_updateLetter(){
        letterDao.update(
                new Letter("testTitle1", "some text u1 - u2", user1, user2, new Date()));
        letterDao.update(
                new Letter("testTitle2", "some text u2 - u1", user2, user1, new Date()));
    }

    @Test
    public void update() {
        user2.setId(20);
        user2.setLogin("FFFGGGHHH");
        user2.setPass("qwerty");
        System.out.println(userDao.update(user2));
    }

}
