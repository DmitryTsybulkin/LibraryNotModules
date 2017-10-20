/**
 * Created by Dmitry on 18.10.2017.
 */

import models.User;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

/** <h1>Класс для выполнения задач перед запуском приложения</h1> */

@OnApplicationStart
public class Bootstrap extends Job {

    /** Метод проверяет, если таблица с пользователями пуста, то загружает
     * тестовую информацию в БД из файла initial-data.yml */

    public void doJob() {
        if (User.count() == 0) {
            Fixtures.loadModels("initial-data.yml");
        }
    }
}