package models;

import play.db.jpa.Model;

import javax.persistence.Entity;

/**
 * Created by Dmitry on 18.10.2017.
 */

/** Класс определяет таблицу со статусом чтения:
 *  кто из пользователей читает определённую книгу
 */
@Entity
public class Status extends Model {
    /** username пользователя */
    public String nick;

    /** название книги */
    public String title;

    /** Инициализируем столбцы и сохраняем */
    public Status(String nick, String title) {
        this.nick = nick;
        this.title = title;
        save();
    }

    public Status() {}
}
