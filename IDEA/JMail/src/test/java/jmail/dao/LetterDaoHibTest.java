package jmail.dao;

import jmail.model.Letter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by gleb on 07.04.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/app-context.xml"})
public class LetterDaoHibTest {

    @Autowired
    private LetterDao letterDao;

    @Autowired
    private UserDao userDao;


    private Letter letter1 = null;
    private Letter letter2 = null;

    @Before
    public void addLetter() {
        String title1 = "title1";
        String title2 = "title2";
        String message1 = UUID.randomUUID().toString();
        String message2 = UUID.randomUUID().toString();
        if(letter1 == null && letter2 == null) {
            letter1 = letterDao.create(new Letter(title1, message1, userDao.findById(2), userDao.findById(25), new Date()));
            letter2 = letterDao.create(new Letter(title2, message2, userDao.findById(25), userDao.findById(2), new Date()));
        }
    }

    @Test
    public void findById() {
        System.out.println("FindById: " + letterDao.findById(letter1.getId()));
    }

    @Test
    public void findByKeyWord() {
        List<Letter> letters = letterDao.findByKeyWord("4071", 2);
        System.out.println("*********FindByKeyWord*********");
        for(Letter letter : letters) {
            System.out.println(letter);
        }
        System.out.println("================================");
    }

    @Test
    public void findByDateRange() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2015, Calendar.MARCH, 25, 0, 0, 0);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2015, Calendar.APRIL, 07, 16, 47, 43);
        List<Letter> letters = letterDao.findByDateRange(new Date(calendar1.getTimeInMillis()), new Date(calendar2.getTimeInMillis()), 2);
        System.out.println("*********FindByDateRange*********");
        for(Letter letter : letters) {
            System.out.println(letter);
        }
        System.out.println("================================");
    }

    @Test
    public void update() {
        letter2.setBody("UPDATE");
        //letter2.setId(2);
        letterDao.update(letter2);
    }

    @Test
    public void delete() {
        letterDao.delete(letter1.getId());
        System.out.println("ID DELETE: " + letter1.getId());
    }

    @Test
    public void allByUserLogin() {
        try {
            List<Letter> letters = letterDao.allByUserLogin("Sergey");
            System.out.println("**********AllByUserLogin**********");
            for(Letter letter : letters) {
                System.out.println(letter);
            }
            System.out.println("==================================");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
