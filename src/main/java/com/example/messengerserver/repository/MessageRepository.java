package com.example.messengerserver.repository;

import com.example.messengerserver.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatIdOrderByTimestampAsc(Long chatId);
    void deleteByChatId(Long chatId);
}