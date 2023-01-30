package uz.developers.bot.model;

import com.pengrad.telegrambot.model.Update;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.developers.dao.UserDao;
import uz.developers.bot.enums.State;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    private Update update;
    private State state;
    private String language;

    public Request(Update update) {
        this.update = update;
        UserDao userDao = UserDao.getUserDao();
        HashMap<String, Object> data = userDao.getUser(update.message().chat().id());
        if (data == null){
            userDao.save(update.message().chat().id(),update.message().chat().firstName(),update.message().chat().lastName(),update.message().chat().username());
        }
        HashMap<String, Object> data2 = userDao.getUser(update.message().chat().id());
        this.state = State.valueOf(((String) data2.get("state")).toUpperCase());
        System.out.println(this.state);
        this.language = (String) data2.get("language");
        System.out.println(this.language);
    }
}
