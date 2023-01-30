package uz.developers.bot.controller;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.extern.slf4j.Slf4j;
import uz.developers.bot.model.Request;
import uz.developers.service.InternalBotService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static uz.developers.AgentMessageApplication.bot;
import static uz.developers.configuration.Properties.ADMINS;
import static uz.developers.configuration.Properties.NOTIFICATION;

@Slf4j
public class InternalBotController {
    private static InternalBotService internalBotService = new InternalBotService();
    public static int handleUpdates(List<Update> updates) {
        List<Request> requests = updates.stream().map(Request::new).collect(Collectors.toList());
        long id = 0;
        for (Request update : requests) {
            if (update.getUpdate().message() != null){
                if (id != update.getUpdate().updateId()){
                    if (isAdmin(update.getUpdate().message().from().id())){
                        log.info("Received bot_admin request : {}",updates);
                        internalBotService.handleAdminRequest(update,bot);
                    }else{
                        log.info("Received bot_user request : {}",updates);
                        internalBotService.handleUserRequest(update,bot);
                    }
                }
            }

        }
        log.info("Processed request successfully.");

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public static void sendAdminNotification() {
        log.info("Notifying admins: {}", ADMINS);
        for (Long adminId : ADMINS) {
            SendMessage request = new SendMessage(adminId, NOTIFICATION).parseMode(ParseMode.HTML).disableWebPagePreview(true).disableNotification(true).replyMarkup(new ForceReply());

            bot.execute(request, new Callback<SendMessage, SendResponse>() {
                @Override
                public void onResponse(SendMessage request, SendResponse response) {
                    log.info("Notification sent to admin {}", adminId);
                }

                @Override
                public void onFailure(SendMessage request, IOException e) {
                    log.error("Failed to send notification to admin {}", adminId, e);
                }
            });
        }
    }

    private static boolean isAdmin(Long adminId){
        for (Long id: ADMINS)
            if (adminId.equals(id))
                return true;
        return false;
    }

}
