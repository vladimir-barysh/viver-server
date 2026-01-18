package com.example.messengerserver.model;

import java.io.Serializable;
import java.util.Objects;

public class GroupMemberId implements Serializable {
    private Long groupId;
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupMemberId that)) return false;
        return Objects.equals(groupId, that.groupId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, userId);
    }
}

