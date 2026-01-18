package com.example.messengerserver.config;

import com.example.messengerserver.repository.MessageRepository;
import com.example.messengerserver.services.JwtService;
import com.example.messengerserver.websocket.ChatWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final JwtService jwtService;
    private final MessageRepository messageRepository;
    private final ObjectMapper objectMapper;

    public WebSocketConfig(JwtService jwtService,
                           MessageRepository messageRepository,
                           ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.messageRepository = messageRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(
                new ChatWebSocketHandler(jwtService, messageRepository, objectMapper),
                "/ws/chat"
        ).setAllowedOrigins("*");
    }
}
