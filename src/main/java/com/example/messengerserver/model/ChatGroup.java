package com.example.messengerserver.model;
import jakarta.persistence.*;

@Entity
@Table(name = "groups_table")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    private String groupName;

    public Long getGroupId() { return groupId; }
    public String getGroupName() { return groupName; }

    public void setGroupId(Long groupId) { this.groupId = groupId; }
    public void setGroupName(String groupName) { this.groupName = groupName; }
}
