package com.example.messengerserver.model;
import jakarta.persistence.*;

@Entity
@Table(name = "groups_table")
public class ChatGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(name = "group_name", nullable = false)
    private String name;

    public Long getId() { return id; }
    public String getName() { return name; }

    public void setId(Long groupId) { this.id = groupId; }
    public void setName(String groupName) { this.name = groupName; }
}
