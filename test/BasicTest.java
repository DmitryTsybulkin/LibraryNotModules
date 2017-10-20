import models.Book;
import models.Status;
import models.User;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

public class BasicTest extends UnitTest {

    @Test
    public void aVeryImportantThingToTest() {
        assertEquals(2, 1 + 1);
    }

    /** Предварительное удаление БД */
    @Before
    public void setup() {
        Fixtures.deleteDatabase();
    }

    /** Проверка загрузки тестовой информации в БД */
    @Test
    public void BeforeJobs() {
        Fixtures.deleteDatabase();
        assertTrue(User.count() == 0);
        assertTrue(Book.count() == 0);
        assertTrue(Status.count() == 0);
        Fixtures.loadModels("data.yml");
        assertTrue(User.count() != 0);
        assertTrue(Book.count() != 0);
    }

    /** Создание нового пользователя */
    @Test
    public void newUser() {
        new User("demo", "demo1", "demo").save();
        User demo = User.find("byUsername", "demo").first();
        assertNotNull(demo);
        assertEquals("demo", demo.fullname);
        assertEquals("demo", demo.username);
        assertEquals("demo1", demo.password);
    }

    /** Проверка, что пользователь является администратором */
    @Test
    public void IsAdministration() {
        new User("admin", "admin", "admin").save();
        User user = User.find("byUsername", "admin").first();
        assertTrue("admin".equals(user.username));
        assertFalse("demo".equals(user.username));
    }

    /** Создание новой книги */
    @Test
    public void newBook() {
        new User("user", "user1", "user").save();
        User user = User.find("byUsername", "user").first();
        assertNotNull(user);
        assertEquals("user", user.username);

        Book books = new Book();
        new Book("Best Book", "SuperAuthor", "Interesting", books.readers);
        Book book = Book.find("byName", "Best Book").first();
        assertNotNull(book);
        assertEquals(book.name, "Best Book");
        assertEquals(book.author, "SuperAuthor");
        assertEquals(book.genre, "Interesting");
        assertNull(book.readers);
    }

    /** Тестирование изменения и сохранения книги */
    @Test
    public void testBookEdit() {
        String nameChange = "BestBook";
        String authorChange = "BestAuthor";
        String genreChange = "BestGenre";
        Book book = new Book();
        new Book("Book", "Author", "Genre", book.readers).save();
        Book boo = Book.find("byName", "Book").first();
        assertEquals("Book", boo.name);
        assertEquals("Author", boo.author);
        assertEquals("Genre", boo.genre);
        assertNull(boo.readers);
        boo.setName(nameChange);
        boo.setAuthor(authorChange);
        boo.setGenre(genreChange);
        boo.save();
        assertFalse("Book".equals(boo.name));
        assertFalse("Author".equals(boo.author));
        assertFalse("Genre".equals(boo.genre));
    }

    /** Тестирование удаления книги */
    @Test
    public void testBookDelete() {
        Book books = new Book();
        new Book("Best Book", "SuperAuthor", "Interesting", books.readers).save();
        Book book = Book.find("byName", "Best Book").first();
        assertNotNull(book);
        assertEquals("SuperAuthor", book.author);
        book.delete();
        assertNull(Book.find("byName", "Best Book").first());
    }

    /** Тестирование изменения и сохранения пользователя */
    @Test
    public void testUserEdit() {
        String usernameChange = "BestUser";
        String passwordChange = "BestPassword";
        String fullnameChange = "BestFullname";
        new User("user", "user1", "user").save();
        User user = User.find("byUsername", "user").first();
        assertEquals("user", user.username);
        assertEquals("user1", user.password);
        assertEquals("user", user.fullname);
        user.setUsername(usernameChange);
        user.setPassword(passwordChange);
        user.setFullname(fullnameChange);
        user.save();
        assertFalse("user".equals(user.username));
        assertFalse("user1".equals(user.password));
        assertFalse("user".equals(user.fullname));
    }

    /** Тестирование удаления пользователя */
    @Test
    public void testUserDelete() {
        new User("user", "user1", "user").save();
        User user = User.find("byUsername", "user").first();
        assertNotNull(user);
        assertEquals(user.username, "user");
        user.delete();
        assertNull(User.find("byUsername", "user").first());
    }

}
