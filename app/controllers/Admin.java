package controllers;

import models.Book;
import models.Status;
import models.User;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.mvc.Before;
import play.mvc.Controller;

import java.util.List;

/**
 * Created by Dmitry on 18.10.2017.
 */

/**
 * Класс содержит метод для отображения страницы авторизованного
 * пользователя, а также методы для работы администратора с БД
 */
public class Admin extends Controller {

    /**
     * Метод кладёт username пользователя, если она авторизован, в
     * HashMap, для работы с ним в фазу рендеринга, иначе возвращает
     * на страницу неавторизованного пользователя
     */
    @Before
    public static void setConnectedUser() {
        if (session.contains("username")) {
            User user = User.find("byUsername", session.get("username")).first();
            renderArgs.put("username", user.username);
        } else {
            flash.error("Вы ещё не вошли в систему!");
            Application.index();
        }
    }

    /**
     * Страница для авторизованного пользователя
     */
    public static void index() {
        List<Book> books = Book.find("order by name").fetch();
        render(books);
    }

    /**
     * Страница для редактирования таблицы книг
     */
    public static void booksedit() {
        List<Book> books = Book.find("order by name").fetch();
        render(books);
    }

    /**
     * Метод редактирует и сохраняет информацию о редактируемой книге
     * @param id - id книги
     * @param name - название книги
     * @param author - автор
     * @param genre - жанр
     */
    public static void EditBook(long id, @Required String name, @Required String author, @Required String genre) {
        if (Validation.hasErrors()) {
            flash.error("Все поля должны быть заполнены");
        } else {
            Book book = Book.findById(id);
            book.setName(name);
            book.setAuthor(author);
            book.setGenre(genre);
            book.save();
        }
        booksedit();
    }

    /**
     * Метод создаёт новый кортеж(новую книгу) в таблице книг.
     * Внимание! Чтобы книги были отображены в браузере, нужно
     * добавить pdf файл в директорию resources
     * @param name - название
     * @param author - автор
     * @param genre - жанр
     */
    public static void NewBook(@Required String name, @Required String author, @Required String genre) throws Throwable {
        if (Validation.hasErrors()) {
            flash.error("Все поля должны быть заполнены");
        } else {
            Book book = new Book();
            new Book(name, author, genre, book.readers).save();
            flash.success("Новая книга: " + name);
        }
        booksedit();
    }

    /**
     * Метод удаляет кортеж(книгу) из таблицы книг
     * @param id - id книги
     */
    public static void deleteBook(long id) {
        Book book = Book.findById(id);
        book.delete();
        booksedit();
    }

    /**
     * Страница для редактирования таблицы пользователей
     */
    public static void usersedit() {
        List<User> users = User.findAll();
        render(users);
    }

    /**
     * Метод редактирует и сохраняет информацию о редактируемом пользователе
     * @param id - id пользователя
     * @param username - username пользователя
     * @param password - пароль
     * @param fullname - реальное имя
     */
    public static void EditUser(long id,
                                @Required
                                @MinSize(value = 4)
                                        String username,
                                @Required
                                @MinSize(value = 5)
                                @MaxSize(value = 20)
                                        String password,
                                @Required
                                @MinSize(value = 4)
                                        String fullname) {
        if (Validation.hasErrors()) {
            flash.error("Неправильно заполнены поля или не заполнены!");
        } else {
            User user = User.findById(id);
            user.setUsername(username);
            user.setPassword(password);
            user.setFullname(fullname);
            user.save();
            flash.success("Пользователь: " + user.username + " изменён");
        }
        usersedit();
    }

    /**
     * Метод удаляет пользователя из тпблицы пользователей
     * @param id - id пользователя
     */
    public static void deleteUser(long id) {
        User user = User.findById(id);
        if (user.isAdmin) {
            flash.error("Вы не можете удалить администратора");
        } else {
            flash.success("Пользователь " + user.username + " удалён");
            user.delete();
        }
        usersedit();
    }

    /**
     * Метод создаёт и сохраняет нового пользователя в таблицу пользователей
     * @param username - username пользователя
     * @param password - пароль
     * @param fullname - реальное имя
     * @throws Throwable
     */
    public static void NewUser(@Required
                               @MinSize(value = 4)
                                       String username,
                               @Required
                               @MinSize(value = 5)
                               @MaxSize(value = 20)
                                       String password,
                               @Required
                               @MinSize(value = 4)
                                       String fullname) throws Throwable {
        if (Validation.hasErrors()) {
            flash.error("Неправильно заполнены поля или не заполнены");
        } else {
            new User(username, password, fullname).save();
            flash.success("Пользователь " + username + " добавлен");
        }
        usersedit();
    }

    /**
     * Страница с таблицей статусов чтения(кто что читает)
     */
    public static void statusview() {
        List<Status> statuses = Status.findAll();
        render(statuses);
    }

}
