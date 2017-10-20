package models;

import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Entity;

/**
 * Created by Dmitry on 18.10.2017.
 */

/** Класс определяет таблицу пользователей в БД */
@Entity
public class User extends Model {
    /** username пользователя, должен быть не менее 4 символов */
    @Required(message = "Обязательное поле*")
    @MinSize(value = 4, message = "Слишком короткий логин")
    public String username;

    /** пароль пользователя */
    @Required(message = "Обязательное поле*")
    @MaxSize(value = 20, message = "Пароль должен быть не более 20 знаков")
    @MinSize(value = 5, message = "Слишком короткий пароль")
    public String password;

    /** Реальное имя пользователя */
    @Required(message = "Обязательное поле*")
    @MinSize(value = 4, message = "Слишком короткое имя пользователя")
    public String fullname;

    /** Является пользователь администратором или нет */
    public boolean isAdmin;

    /** Инициализируем столбцы и сохраняем */
    public User(String username, String password, String fullname) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        save();
    }

    /** Сеттеры и геттеры для изменения ячеек таблицы */
    public void setPassword(String _password) {
        this.password = _password;
    }

    public void setUsername(String _username) {
        this.username = _username;
    }

    public void setFullname(String _fullname) {
        this.fullname = _fullname;
    }


    /** @return строчное значение столбца username
     */
    public String toString() {
        return username;
    }
}
