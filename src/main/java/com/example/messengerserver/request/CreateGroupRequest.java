package com.example.messengerserver.request;

import java.util.List;

public class CreateGroupRequest {
    private String groupName;
    private List<Long> userIds;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}

