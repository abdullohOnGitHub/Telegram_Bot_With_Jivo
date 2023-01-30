package uz.developers;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uz.developers.bot.controller.InternalBotController;

import static uz.developers.configuration.Properties.BOT_TOKEN;
import static uz.developers.bot.controller.InternalBotController.sendAdminNotification;

@SpringBootApplication
public class AgentMessageApplication {
    public static final TelegramBot bot = new TelegramBot(BOT_TOKEN);

    public static void main(String[] args) {
        sendAdminNotification();
        bot.setUpdatesListener(InternalBotController::handleUpdates);
        SpringApplication.run(AgentMessageApplication.class, args);
    }

}
