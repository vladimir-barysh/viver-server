package com.example.messengerserver.controller;

import com.example.messengerserver.model.Chat;
import com.example.messengerserver.model.Message;
import com.example.messengerserver.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupRepository groupRepository;

    public ChatController(MessageRepository messageRepository, UserRepository userRepository,
                          ChatRepository chatRepository, GroupMemberRepository groupMemberRepository,
                          GroupRepository groupRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.groupRepository = groupRepository;
    }
    @GetMapping("/messages/{chatId}")
    public ResponseEntity<List<Message>> getMessagesByChat(@PathVariable Long chatId) {
        List<Message> messages = messageRepository.findByChatIdOrderByTimestampAsc(chatId);
        return ResponseEntity.ok(messages);
    }
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody Message message,
                                         Authentication auth) {
        Long senderId = (Long) auth.getPrincipal();
        if (message.getChatId() == null || message.getContent() == null || message.getContent().isBlank()) {
            return ResponseEntity.badRequest().body("chatId и content обязательны");
        }
        message.setSenderId(senderId);
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);
        return ResponseEntity.ok("Сообщение отправлено");
    }
    @Transactional
    @DeleteMapping("/{chatId}")
    public ResponseEntity<?> deleteChat(@PathVariable Long chatId, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();

        Optional<Chat> chatOpt = chatRepository.findByGroupId(chatId);
        if (chatOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        messageRepository.deleteByChatId(chatId);
        groupMemberRepository.deleteByGroupId(chatOpt.get().getGroupId());
        chatRepository.deleteById(chatId);
        groupRepository.deleteById(chatOpt.get().getGroupId());
        return ResponseEntity.ok().build();
    }

}
