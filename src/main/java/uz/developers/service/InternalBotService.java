package uz.developers.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import uz.developers.bot.enums.State;
import uz.developers.bot.model.Request;

import static uz.developers.service.AdminService.*;
import static uz.developers.service.AdminService.sendAdvertisementAsForward;
import static uz.developers.service.UserService.*;

@Service
public class InternalBotService {

    public void handleAdminRequest(Request update, TelegramBot bot){
        Long userId = update.getUpdate().message().from().id();
        if (update.getUpdate().message().text().equals("/start")) {
            saveUser(update);
            bot.execute(new SendMessage(userId, "Salom, admin"));
        } else if (update.getUpdate().message().text().equals("/users")) {
            sendAllUsersList(userId, bot);
        } else if (update.getUpdate().message().text().equals("/send")) {
            sendAdvertisementToAllUsers(update, bot);
        } else if (update.getUpdate().message().text().equals("/send_forward")) {
            sendAdvertisementAsForward(update, bot);
        }
    }

    public void handleUserRequest(Request update, TelegramBot bot){
        Long userId = update.getUpdate().message().chat().id();
        String text = update.getUpdate().message().text();

        try {

            if (update.getState() == State.NONE) {
                saveUser(update);
                greetUser(userId, bot);
            } else if (text.equals("/start") && update.getState() != State.NONE) {
                handleMainMenuOption(userId, text, bot);
            } else if (update.getState() == State.LANGUAGE) {
                handleLanguageOption(userId, text, bot);
            } else if (update.getState() == State.MAIN) {
                handleMainMenuOption(userId, text, bot);
            } else if (update.getState() == State.OPERATOR) {
                connectUserToOperator(userId, text, bot);
            } else bot.execute(new SendMessage(userId, "Berilgan knopkalar orqali ishlang"));

        } catch (Exception e) {
            bot.execute(new SendMessage(userId, "Bot media, audio, rasm va fayllarni qo'llab quvvatlamaydi"));
            e.printStackTrace();
        }
    }
}
