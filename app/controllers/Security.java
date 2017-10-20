package controllers;

import models.User;
import play.mvc.Controller;

/**
 * Created by Dmitry on 18.10.2017.
 */

/**
 * Класс для входа пользователя в систему и выхода из системы
 */
public class Security extends Controller {

    /**
     * Страница с логином
     */
    public static void login() {
        render();
    }

    /**
     * Метод для аутентификации пользователя в системе
     * @param username - username пользователя
     * @param password - пароль
     * @throws Throwable
     */
    public static void auth(String username, String password) throws Throwable {
        User user = User.find("byUsernameAndPassword", username, password).first();
        if (user != null) {
            flash.success("Добро пожаловать, " + user.username);
            session.put("username", username);
            Admin.index();
        } else {
            flash.keep("url");
            flash.error("Неправильный логин или пароль");
            params.flash();
            login();
        }
    }

    /**
     * Метод для выхода пользователя из системы
     * @throws Throwable
     */
    public static void logout() throws Throwable{
        session.clear();
        flash.success("Вы успешно вышли");
        Application.index();
    }
}
