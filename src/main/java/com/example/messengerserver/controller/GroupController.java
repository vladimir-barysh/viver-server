package com.example.messengerserver.controller;

import com.example.messengerserver.dto.CreateGroupRequest;
import com.example.messengerserver.model.Chat;
import com.example.messengerserver.model.ChatGroup;
import com.example.messengerserver.model.GroupMember;
import com.example.messengerserver.repository.ChatRepository;
import com.example.messengerserver.repository.GroupMemberRepository;
import com.example.messengerserver.repository.GroupRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupChatController {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ChatRepository chatRepository;

    public GroupChatController(GroupRepository groupRepository,
                           GroupMemberRepository groupMemberRepository,
                           ChatRepository chatRepository) {
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.chatRepository = chatRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestBody CreateGroupRequest request) {
        if (request.getGroupName() == null || request.getUserIds() == null || request.getUserIds().isEmpty()) {
            return ResponseEntity.badRequest().body("Название группы и участники обязательны");
        }

        ChatGroup group = new ChatGroup();
        group.setName(request.getGroupName());
        group = groupRepository.save(group);

        for (Long userId : request.getUserIds()) {
            GroupMember member = new GroupMember();
            member.setGroupId(group.getId());
            member.setUserId(userId);
            groupMemberRepository.save(member);
        }

        Chat chat = new Chat();
        chat.setGroupId(group.getId());
        chatRepository.save(chat);

        return ResponseEntity.ok("Группа создана успешно");
    }
}


