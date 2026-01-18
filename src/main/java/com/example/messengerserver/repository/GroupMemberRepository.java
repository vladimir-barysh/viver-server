package com.example.messengerserver.repository;

import com.example.messengerserver.model.GroupMember;
import com.example.messengerserver.model.GroupMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {
    List<GroupMember> findByUserId(Long userId);
    void deleteByGroupId(Long groupId);
}
