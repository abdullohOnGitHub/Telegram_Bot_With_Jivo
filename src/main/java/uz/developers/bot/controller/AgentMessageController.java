package uz.developers.bot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.developers.AgentMessageApplication;
import uz.developers.service.AgentMessageService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
@Slf4j
public class AgentMessageController {
    private final AgentMessageService agentMessageService;

    @PostMapping("/send")
    public void handleAgentMessage(@RequestBody String AgentMessage) {
        log.info("Received agent message with body : {}",AgentMessage);
        agentMessageService.sendAgentMessageToClient(AgentMessage,AgentMessageApplication.bot);
        log.info("Response sent successfully to bot_user.");
    }
}
