package com.example.messengerserver.controller;

import com.example.messengerserver.request.CreateGroupRequest;
import com.example.messengerserver.model.Chat;
import com.example.messengerserver.model.ChatGroup;
import com.example.messengerserver.model.GroupMember;
import com.example.messengerserver.repository.ChatRepository;
import com.example.messengerserver.repository.GroupMemberRepository;
import com.example.messengerserver.repository.GroupRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ChatRepository chatRepository;

    public GroupController(GroupRepository groupRepository,
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

        Set<Long> allUserIds = new HashSet<>(request.getUserIds());
        Long currentUserId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        allUserIds.add(currentUserId);

        for (Long userId : allUserIds) {
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

    @GetMapping("/groups")
    public List<GroupDTO> getUserGroups(Authentication auth) {
        Long userId = (Long) auth.getPrincipal(); // ID текущего пользователя

        List<GroupMember> memberships = groupMemberRepository.findByUserId(userId);
        System.out.println(" !!! Пользователь ID: " + userId);
        System.out.println(" !!! Найдено записей group_members: " + memberships.size());


        return memberships.stream()
                .map(member -> groupRepository.findById(member.getGroupId()).orElse(null))
                .filter(Objects::nonNull)
                .map(group -> new GroupDTO(group.getId(), group.getName()))
                .collect(Collectors.toList());
    }

    public record GroupDTO(Long groupId, String groupName) {}
}
