package ch.cas.html5.multicardgame.messaging;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
public class WebSocketController {
    @MessageMapping("/ws-endpoint")
    @SendTo("/topic/")
    public String greeting(Message message) throws Exception {
        return "Hello, " + message.getText();
    }

}

