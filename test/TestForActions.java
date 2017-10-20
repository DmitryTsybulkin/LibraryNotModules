import models.Book;
import models.Status;
import models.User;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

/**
 * Created by Dmitry on 19.10.2017.
 */

public class TestForActions extends UnitTest {

    /** Предварительное удаление БД */
    @Before
    public void setup() {
        Fixtures.deleteDatabase();
    }


    /** Проверка соответствия пользовательских данных условиям валидации */
    @Test
    public void saveNewUser() {
        new User("user", "12345", "user");
        User user = User.find("byUsername", "user").first();
        assertNotNull(user);
        assertEquals(user.username, "user");
        assertEquals(user.fullname, "user");
        assertEquals(user.password, "12345");
        assertTrue(user.username.length() >= 4 && !user.username.equals(""));
        assertFalse(user.username.length() == 3 || user.username.equals(""));
        assertTrue(user.password.length() >= 5 && !user.password.equals(""));
        assertFalse(user.password.length() == 3 || user.password.equals(""));
        assertTrue(user.fullname.length() >= 4 && !user.fullname.equals(""));
        assertFalse(user.fullname.length() == 3 || user.fullname.equals(""));
    }

    /** Проверка начала чтения */
    @Test(expected = NullPointerException.class)
    public void StartReading() {
        new User("user", "12345", "user").save();
        Book book = new Book();
        new Book("Cool book", "Cool author", "Cool genre", book.readers).save();
        User user = User.find("byUsername", "user").first();
        Book bookee = Book.find("byName", "Cool book").first();
        assertNotNull(user);
        assertEquals(user.username, "user");
        assertNotNull(bookee);
        assertEquals(bookee.name, "Cool book");
        new Status(user.username, bookee.name).save();
        Status status = Status.find("byNickAndTitle", "user", "Cool book").first();
        if (!bookee.readers.contains(user)) {
            bookee.readers.add(user);
            bookee.save();
        }
        assertNotNull(status);
        assertEquals(status.nick, user.username);
        assertEquals(status.title, bookee.name);
    }

    /** Проверка завершения чтения */
    @Test(expected = NullPointerException.class)
    public void StopRead() {
        StartReading();
        User user = User.find("byUsername", "user").first();
        assertNotNull(user);
        assertEquals(user.username, "user");
        Book book = Book.find("byName", "Cool book").first();
        assertNotNull(book);
        assertEquals(book.name, "Cool book");
        Status status = Status.find("byNickAndTitle", "user", "Cool book").first();
        assertNotNull(status);
        assertEquals(status.nick, user.username);
        assertEquals(status.title, book.name);
        status.delete();
        assertNull(status);
    }
}
