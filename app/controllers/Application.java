package controllers;

import models.Book;
import models.Status;
import models.User;
import play.data.validation.*;
import play.mvc.Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс с основными контроллерами: гланая страница, страница регистрации, начало чтения, конец чтения
 */
public class Application extends Controller {

    private final static String path = "resources";
    private final static String postfix = ".pdf";

    /** <h1>Метод для отображения страницы с таблицей книг, для не авторизованного пользователя</h1> */
    public static void index() {
        session.clear();
        List<Book> bookses = Book.find("order by name").fetch();
        if (bookses == null) {
            bookses = new ArrayList<Book>();
        }
        render(bookses);
    }

    /** <h1>Метод для отображения страницы регистрации</h1> */
    public static void registration() {
        render();
    }

    /**
     * </h1>Метод для регистрации нового пользователя и добавления его в БД
     * с использованием валидации введёных им данных <h1>
     * После проверки введённых пользователем данных, пользователь либо
     * остаётся на странице регистрации для исправления ошибок, либо
     * переходит на страницу авторизованного пользователя:
     * @see Admin
     * Получаемые параметры:
     * @param username - юзернейм или логин пользователя
     * @param password - пароль
     * @param password2 - проверка пароля введённого раннее
     * @param fullname - реальное имя пользователя
     */
    public static void saveUser(
            @Required(message = "Обязательное поле")
            @MinSize(value = 4, message = "Слишком короткий логин")
                    String username,
            @Required(message = "Обязательное поле")
            @MinSize(value = 5, message = "Минимум 5 символов")
            @MaxSize(value = 20, message = "Пароль должен быть не более 20 знаков")
                    String password,
            @Required(message = "Обязательное поле")
            @Equals(value = "password", message = "Пароли не совпадают")
                    String password2,
            @Required(message = "Обязательное поле")
            @MinSize(value = 4, message = "Слишком короткое имя пользователя")
                    String fullname
    ) {
        User name = User.find("byUsername", username).first();
        if (name != null) {
            flash.error("Пользователь с таким логином уже соществует!");
            registration();
        }
        if (Validation.hasErrors()) {
            flash.keep("url");
            Validation.keep();
            params.flash();
            flash.error("Исправте ошибки");
            registration();
        }
        User user = new User(username, password, fullname);
        session.put("username", user.username);
        flash.success("Добро пожаловать, " + user.username + " вы успешно зарегистрировались!");
        Admin.index();
    }

    /**
     * <h1>Метод для начала чтения книги пользователем</h1>
     * В БД, для таблицы книг и статуса чтения делаются запросы:
     * для книги - это то, есть ли в списке читатателей авторизованный
     * пользователь, и для статуса - если пользователь не читал эту книгу,
     * то создаётся новый кортеж в таблице:
     * @see Status
     * А также, в список читателей определённой книги в таблице Book добавляется
     * fullname или имя пользователя, начавшего читать
     * Получаемый параметр:
     * @param name - название книги, которую пользователь будет читать
     */
    public static void StartRead(String name) {
        if (!session.contains("username")) {
            flash.error("Пользователь не авторизован!");
            Admin.index();
        }
        String user = session.get("username");
        User us = User.find("byUsername", user).first();
        if (us == null) {
            flash.error("Пользователь не найден!");
            Admin.index();
        }
        Book book = Book.find("byName", name).first();
        if (book == null) {
            flash.error("Книга не существует!");
            Admin.index();
        }
        Status stat = Status.find("byNickAndTitle", us.username, name).first();
        if (book.readers.contains(us)) {
            flash.success("Вы уже читали эту книгу");
        } else {
            book.readers.add(us);
            book.save();
        }
        File file = null;
        try {
            file = getFile(name);
            if (file == null) {
                flash.error("Книга не найдена!");
                Admin.index();
            } else {
                renderBinary(file);
            }
        } catch (NullPointerException e) {
            e.getLocalizedMessage();
        }
        if (stat == null && file != null) {
            stat = new Status(user, name);
            stat.save();
            flash.success("Теперь вы читаете книгу: " + name);
        } else {
            flash.success("Вы уже читаете эту книгу!");
            Admin.index();
        }
    }

    /**
     * Метод проверяет наличие файла в ресурсах, если файл есть,
     * то после начала чтения он откроется методом renderBinary()
     * @param name - название книги
     * @return - файл книги в формате pdf
     */
    private static File getFile(String name) throws NullPointerException {
        name += postfix;
        File f = new File(path + ".");
        File[] files = f.listFiles();
        for (File file : files) {
            if (file.toString().equals(path + ".\\" + name) && file.isFile()) {
                return file;
            }
        }
        return null;
    }

    /**
     * <h1>Метод для завершения чтения книги</h1>
     * После того, как авторизованный пользователь нажимает на странице
     * кнопку "Закончить", в БД, в таблицу Status делается запрос поиска
     * кортежа по  username'у пользователя и названию книги, если он есть
     * то удаляется
     * Получаемый параметр:
     * @param name - название книги
     */
    public static void StopRead(String name) {
        if (!session.contains("username")) {
            flash.error("Пользователь не авторизован!");
            Admin.index();
        }
        String user = session.get("username");
        Status st = Status.find("byNickAndTitle", user, name).first();
        if (st == null) {
            flash.error("Статус чтения не найден!");
            Admin.index();
        }
        st.delete();
        flash.success("Вы успешно сдали книгу!");
        Admin.index();
    }

}