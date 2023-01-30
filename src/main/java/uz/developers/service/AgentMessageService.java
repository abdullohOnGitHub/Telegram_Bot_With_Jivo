package uz.developers.service;

import com.google.gson.Gson;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.developers.dao.UserDao;
import uz.developers.bot.enums.State;
import uz.developers.bot.model.AgentResponse;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AgentMessageService {

    private final Gson gson = new Gson();

    public void sendAgentMessageToClient(String object, TelegramBot bot){
        AgentResponse agentResponse = gson.fromJson(object, AgentResponse.class);
        UserDao userDao = UserDao.getUserDao();
        HashMap<String, Object> user = userDao.getUser(Long.parseLong(agentResponse.getRecipient().getId()));
        if (user.get("state").equals(String.valueOf(State.OPERATOR))){
            if (agentResponse.getMessage().getType().equals("text")){
                bot.execute(new SendMessage(agentResponse.getRecipient().getId(),agentResponse.getMessage().getText()));
            }
        }
    }
}
