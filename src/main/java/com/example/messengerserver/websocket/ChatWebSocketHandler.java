package com.example.messengerserver.websocket;

import com.example.messengerserver.model.Message;
import com.example.messengerserver.repository.MessageRepository;
import com.example.messengerserver.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final JwtService jwtService;
    private final MessageRepository messageRepository;
    private final ObjectMapper objectMapper;

    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public ChatWebSocketHandler(JwtService jwtService,
                                MessageRepository messageRepository,
                                ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.messageRepository = messageRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String token = UriComponentsBuilder
                .fromUri(session.getUri())
                .build()
                .getQueryParams()
                .getFirst("token");

        if (token == null || !jwtService.validateToken(token)) {
            System.out.println("GOT TOKEN:" + token);
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid or missing token"));
            return;
        }

        Long userId = jwtService.extractUserId(token);
        session.getAttributes().put("user_id", userId);
        sessions.put(userId, session);

        System.out.println("Connected: userId = " + userId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long senderId = (Long) session.getAttributes().get("user_id");

        if (senderId == null) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Unauthorized session"));
            return;
        }

        Message incoming = objectMapper.readValue(message.getPayload(), Message.class);
        incoming.setSenderId(senderId);
        incoming.setTimestamp(LocalDateTime.now());

        messageRepository.save(incoming);

        Long chatId = incoming.getChatId();
        WebSocketSession recipientSession = sessions.get(chatId);
        if (recipientSession != null && recipientSession.isOpen()) {
            recipientSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(incoming)));
        }

        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(incoming)));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null) {
            sessions.remove(userId);
            System.out.println("Disconnected: userId = " + userId);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.err.println("WebSocket error: " + exception.getMessage());
        session.close(CloseStatus.SERVER_ERROR);
    }
}
