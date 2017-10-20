import org.junit.Test;
import play.Play;
import play.mvc.Http.Response;
import play.test.FunctionalTest;

/** Функциональное тестирование */
public class ApplicationTest extends FunctionalTest {

    /** Тест проверяет ответ от первой, главной страницы */
    @Test
    public void testThatIndexPageWorks() {
        Response response = GET("/");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }

    /** Проверка ответа от страницы авторизованного пользователя */
    @Test
    public void testSecurity() {
        Response response = GET("/admin");
        assertStatus(302, response);
        assertHeaderEquals("Location", "/", response);
    }

    /** Проверка ответа от страницы входа в систему */
    @Test
    public void testLogin() {
        Response response = GET("/login");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(Play.defaultWebEncoding, response);
    }

    /** Проверка ответа от страницы регистрации */
    @Test
    public void testReg() {
        Response response = GET("/registration");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(Play.defaultWebEncoding, response);
    }
    
}