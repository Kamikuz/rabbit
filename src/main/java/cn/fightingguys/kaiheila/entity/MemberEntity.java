package cn.fightingguys.kaiheila.entity;

import cn.fightingguys.kaiheila.RabbitImpl;
import cn.fightingguys.kaiheila.core.RabbitObject;
import cn.fightingguys.kaiheila.core.action.Operation;

import java.time.LocalDateTime;
import java.util.List;

public class MemberEntity extends RabbitObject {

    private String userId;
    private String nickname;
    private List<Integer> roles;
    private LocalDateTime joinedAt;
    private LocalDateTime activeTime;

    public MemberEntity(RabbitImpl rabbit) {
        super(rabbit);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<Integer> getRoles() {
        return roles;
    }

    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public LocalDateTime getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(LocalDateTime activeTime) {
        this.activeTime = activeTime;
    }
}
