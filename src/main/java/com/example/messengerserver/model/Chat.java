package com.example.messengerserver.model;

import jakarta.persistence.*;

@Entity
@Table(name = "chats_table")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    public Long getGroupId() { return groupId; }
    public Long getId() { return id; }

    public void setGroupId(Long groupId) { this.groupId = groupId; }
    public void setId(Long id) { this.id = id; }
}
