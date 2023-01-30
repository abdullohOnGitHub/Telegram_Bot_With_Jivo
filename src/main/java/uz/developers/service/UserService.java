package uz.developers.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.web.client.RestTemplate;
import uz.developers.dao.UserDao;
import uz.developers.bot.enums.State;

import java.util.HashMap;

import static uz.developers.configuration.Properties.UNIQUE_URL;

public class UserService {
    public static RestTemplate restTemplate = new RestTemplate();

    public static void greetUser(Long chatId, TelegramBot bot) {
        UserDao userDao = UserDao.getUserDao();
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup("Uzbek", "Russian").resizeKeyboard(true).oneTimeKeyboard(true);
        bot.execute(new SendMessage(chatId, "Assalomu alaykum botga xush kelibsiz.\nBotni ishga tushirishdan oldin iltimos, o'zingiz uchun qulay tilni tanlang\uD83D\uDE04\n\n\n" +
                "Привет и добро пожаловать в бота.\n" +
                "Перед запуском бота выберите предпочитаемый язык\uD83D\uDE04").replyMarkup(keyboardMarkup));
        userDao.updateState(chatId, State.LANGUAGE);
    }

    public static void handleLanguageOption(Long chatId, String lan, TelegramBot bot) {
        UserDao userDao = UserDao.getUserDao();
        userDao.changeLanguage(chatId, lan);
        if (lan.equals("Uzbek")) {
            userDao.updateState(chatId, State.MAIN);
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup("Nimadir", "Operator bilan bog'lanish").resizeKeyboard(true).oneTimeKeyboard(true);
            bot.execute(new SendMessage(chatId, "Quyidagilardan birini tanlang").replyMarkup(keyboardMarkup));
        } else if (lan.equals("Russian")) {
            userDao.updateState(chatId, State.MAIN);
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup("Что нибудь", "Связаться с оператором").resizeKeyboard(true).oneTimeKeyboard(true);
            bot.execute(new SendMessage(chatId, "Выберите один из следующих").replyMarkup(keyboardMarkup));
        } else {
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup("Uzbek", "Russian").resizeKeyboard(true).oneTimeKeyboard(true);
            bot.execute(new SendMessage(chatId, "Tilni knopkalar orqali tanlang\n\nВыберите язык с помощью кнопок").replyMarkup(keyboardMarkup));
        }
    }

    public static void handleMainMenuOption(Long userId, String text, TelegramBot bot) {
        UserDao userDao = UserDao.getUserDao();
        if (text.equals("Nimadir")) {
            bot.execute(new SendMessage(userId, "Bu qism endi tayyorlanadi"));
        } else if (text.equals("Что нибудь")) {
            bot.execute(new SendMessage(userId, "Эта часть уже готова"));
        } else if (text.equals("Operator bilan bog'lanish")) {
            userDao.updateState(userId, State.OPERATOR);
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup("Orqaga").resizeKeyboard(true).oneTimeKeyboard(true);
            bot.execute(new SendMessage(userId, "Savolingizni operatorga berishingiz mumkin : ").replyMarkup(keyboardMarkup));

        } else if (text.equals("Связаться с оператором")) {
            userDao.updateState(userId, State.OPERATOR);
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup("Назад").resizeKeyboard(true).oneTimeKeyboard(true);
            bot.execute(new SendMessage(userId, "Вы можете задать свой вопрос оператору: : ").replyMarkup(keyboardMarkup));

        } else {
            userDao.updateState(userId,State.MAIN);

            if (userDao.getUser(userId).get("language").equals("Uzbek")) {
                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup("Nimadir", "Operator bilan bog'lanish").resizeKeyboard(true).oneTimeKeyboard(true);
                bot.execute(new SendMessage(userId, "Buyruqlarni knopklar orqali bering").replyMarkup(keyboardMarkup));
            } else {
                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup("Что нибудь", "Связаться с оператором").resizeKeyboard(true).oneTimeKeyboard(true);
                bot.execute(new SendMessage(userId, "Выберите один из следующих").replyMarkup(keyboardMarkup));
            }
        }
    }

    public static void connectUserToOperator(Long userId, String text, TelegramBot bot) {
        UserDao userDao = UserDao.getUserDao();
        HashMap<String, Object> botUser = userDao.getUser(userId);
        if (text.equals("Orqaga")) {
            userDao.updateState(userId, State.MAIN);
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup("Nimadir", "Operator bilan bog'lanish").resizeKeyboard(true).oneTimeKeyboard(true);
            bot.execute(new SendMessage(userId, "Buyruqlarni knopklar orqali bering").replyMarkup(keyboardMarkup));
            String sender = "{\n" + "\"sender\" :\n" + "   {\n" + "      \"id\"    : \"" + botUser.get("id") + "\",\n" + "      \"name\"  : \"" + botUser.get("username")+botUser.get("first_name") + "\",\n" + "      \"photo\" : \"\",\n" + "      \"url\"   : \"\",\n" + "      \"phone\" : \"\",\n" + "      \"email\" : \"\",\n" + "      \"invite\" : \"\"\n" + "   },\n" + "   \"message\" :\n" + "   {\n" + "      \"type\" : \"text\",\n" + "      \"id\"   : \"customer_message_id\",\n" + "      \"text\" : \"" + "User chatni tark etdi.".toUpperCase() + "\"\n" + "   }    \n" + "}";
            restTemplate.postForEntity(UNIQUE_URL, sender, String.class);

        } else if (text.equals("Назад")) {
            userDao.updateState(userId, State.MAIN);
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup("Что нибудь", "Связаться с оператором").resizeKeyboard(true).oneTimeKeyboard(true);
            bot.execute(new SendMessage(userId, "Выберите один из следующих").replyMarkup(keyboardMarkup));
            String sender = "{\n" + "\"sender\" :\n" + "   {\n" + "      \"id\"    : \"" + botUser.get("id") + "\",\n" + "      \"name\"  : \"" + botUser.get("username")+botUser.get("first_name") + "\",\n" + "      \"photo\" : \"\",\n" + "      \"url\"   : \"\",\n" + "      \"phone\" : \"\",\n" + "      \"email\" : \"\",\n" + "      \"invite\" : \"\"\n" + "   },\n" + "   \"message\" :\n" + "   {\n" + "      \"type\" : \"text\",\n" + "      \"id\"   : \"customer_message_id\",\n" + "      \"text\" : \"" + "ПОЛЬЗОВАТЕЛЬ ВЫШЕЛ ИЗ ЧАТА."+ "\"\n" + "   }    \n" + "}";
            restTemplate.postForEntity(UNIQUE_URL, sender, String.class);


        } else {

            String sender = "{\n" + "\"sender\" :\n" + "   {\n" + "      \"id\"    : \"" + botUser.get("id") + "\",\n" + "      \"name\"  : \"" + botUser.get("username")+botUser.get("first_name") + "\",\n" + "      \"photo\" : \"\",\n" + "      \"url\"   : \"\",\n" + "      \"phone\" : \"\",\n" + "      \"email\" : \"\",\n" + "      \"invite\" : \"\"\n" + "   },\n" + "   \"message\" :\n" + "   {\n" + "      \"type\" : \"text\",\n" + "      \"id\"   : \"customer_message_id\",\n" + "      \"text\" : \"" + text + "\"\n" + "   }    \n" + "}";

            restTemplate.postForEntity(UNIQUE_URL, sender, String.class);

        }
    }
}
