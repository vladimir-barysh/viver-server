package com.example.messengerserver.services;

import com.example.messengerserver.model.ChatGroup;
import com.example.messengerserver.model.GroupMember;
import com.example.messengerserver.repository.GroupMemberRepository;
import com.example.messengerserver.repository.GroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    public GroupService(GroupRepository groupRepository, GroupMemberRepository groupMemberRepository) {
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    @Transactional
    public ChatGroup createGroup(String groupName, List<Long> userIds) {
        if (userIds.size() < 2) {
            throw new IllegalArgumentException("Группа должна содержать минимум двух пользователей.");
        }

        ChatGroup group = new ChatGroup();
        group.setName(groupName);
        group = groupRepository.save(group);

        for (Long userId : userIds) {
            GroupMember member = new GroupMember();
            member.setGroupId(group.getId());
            member.setUserId(userId);
            groupMemberRepository.save(member);
        }

        return group;
    }
}
