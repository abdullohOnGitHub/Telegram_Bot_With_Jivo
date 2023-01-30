package uz.developers.bot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AgentResponse {
    private Sender sender;
    private Recipient recipient;
    private Message message;

    public static class Sender {
        @JsonProperty("name")
        private String name;

        public Sender(String name, String photo) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

    public static class Recipient {
        @JsonProperty("id")
        private String id;

        public Recipient(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    public static class Message {
        @JsonProperty("type")
        private String type;
        @JsonProperty("id")
        private String id;
        @JsonProperty("text")
        private String text;

        public Message(String type, String id, String text) {
            this.type = type;
            this.id = id;
            this.text = text;
        }

        public String getType() {
            return type;
        }

        public String getId() {
            return id;
        }

        public String getText() {
            return text;
        }
    }

    public AgentResponse(String name, String photo, String id, String type, String messageId, String text) {
        this.sender = new Sender(name, photo);
        this.recipient = new Recipient(id);
        this.message = new Message(type, messageId, text);
    }

    public Sender getSender() {
        return sender;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public Message getMessage() {
        return message;
    }
}
