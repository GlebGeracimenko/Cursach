package jmail.dao;

import jdk.nashorn.internal.ir.annotations.Ignore;
import jmail.model.Letter;
import jmail.model.User;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

    private static Letter letter = null;

    @BeforeClass
    public static void init() {
        String loginUser1 = UUID.randomUUID().toString();
        String loginUser2 = UUID.randomUUID().toString();
        user1 = userDao.create(new User(loginUser1, "1234"));
        user2 = userDao.create(new User(loginUser2, "4321"));
        Assert.assertNotNull(userDao.find(user1.getLogin()));
        Assert.assertNotNull(userDao.find(user2.getLogin()));
        Assert.assertNotNull(userDao.findById(2));
        Assert.assertNotNull(userDao.findById(5));
        letterDao.create(new Letter("Message", "Hello", user1, user2, new Date()));
    }

    @Test
    public void delete() {
        userDao.delete("Kolay");
    }

    @Test
    public void update() {
        user1.setLogin("Podlesniy");
        userDao.update(user1);
    }

    @Test
    public void find() {
        userDao.find("Podlesniy");
    }


}
