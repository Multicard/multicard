package ch.cas.html5.multicardgame.messaging;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic/", "/queue/");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //https://stackoverflow.com/questions/26500954/sockjs-not-passing-credential-information-when-on-a-different-domaincors
        //registry.addEndpoint("/ws-endpoint").setAllowedOrigins("*").withSockJS();
        registry.addEndpoint("/ws-endpoint").setAllowedOriginPatterns("http://localhost:*").withSockJS();
    }
}
