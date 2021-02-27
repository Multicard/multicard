package ch.cas.html5.multicardgame.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void sendToUser(String playgroundId, String userId, Message msg) {
        String dest = "/queue/" + playgroundId + "/" + userId;
        simpMessagingTemplate.convertAndSend(dest, msg);
        System.out.println("Message sent to: " + dest);
    }

    @MessageMapping("/{playgroundId}/{userId}")
    public void handleMessage(@DestinationVariable String playgroundId, @DestinationVariable String userId, String message) {
        System.out.println("Message received: " + message + "from: " + playgroundId + "/" + userId);
    }

}

