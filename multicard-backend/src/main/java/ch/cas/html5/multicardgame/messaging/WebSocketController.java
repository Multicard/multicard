package ch.cas.html5.multicardgame.messaging;

import ch.cas.html5.multicardgame.control.GameControlService;
import ch.cas.html5.multicardgame.messages.GameMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Controller
@Transactional
public class WebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private GameControlService gameControlService;

    public void setGameControlService(GameControlService gameControlService) {
        this.gameControlService = gameControlService;
    }

    public WebSocketController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    public void sendToUser(String playgroundId, String userId, GameMessage msg) {
        String dest = "/queue/" + playgroundId + "/" + userId;
        simpMessagingTemplate.convertAndSend(dest, msg);
//        System.out.println("Message sent to: " + dest);
    }

    @MessageMapping("/{gameId}/{playerId}")
    public void handleMessage(@DestinationVariable String gameId, @DestinationVariable String playerId, @RequestBody GameMessage gameMessage) {
//        System.out.println("Message received: " + gameMessage.getCommand() + " - from: " + gameId + "/" + playerId);
        gameControlService.handleMessage(gameMessage, gameId, playerId);
    }

    @EventListener
    public void onApplicationEvent(SessionUnsubscribeEvent event) {
        System.out.println("Websocket unsubscribe - Header: " + event.getMessage().getHeaders().get("simpDestination"));
        gameControlService.removeConnection((String) event.getMessage().getHeaders().get("simpDestination"),
                (String) event.getMessage().getHeaders().get("simpSessionId"));
    }

    @EventListener
    public void onApplicationEvent(SessionSubscribeEvent event) {
        System.out.println("Websocket subscribe - Header: " + event.getMessage().getHeaders().get("simpDestination"));
        gameControlService.addConnection((String) event.getMessage().getHeaders().get("simpDestination"),
                (String) event.getMessage().getHeaders().get("simpSessionId"));
    }

    @EventListener
    public void onApplicationEvent(SessionDisconnectEvent event) {
        System.out.println("Websocket disconnect - Header: " + event.getMessage().getHeaders().get("simpSessionId"));
        gameControlService.playerOff((String) event.getMessage().getHeaders().get("simpSessionId"));
    }
}

