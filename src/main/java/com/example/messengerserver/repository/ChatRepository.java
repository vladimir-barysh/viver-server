package com.example.messengerserver.repository;

import com.example.messengerserver.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByGroupId(Long groupId);
}
