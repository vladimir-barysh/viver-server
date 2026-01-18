package com.example.messengerserver.repository;

import com.example.messengerserver.model.ChatGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<ChatGroup, Long> {}
