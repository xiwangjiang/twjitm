package com.twjitm.common.entity.online;

import com.alibaba.fastjson.JSON;
import com.twjitm.common.entity.BaseMessage;
import com.twjitm.user.entity.User;

/**
 * 玩家广播上线／下现消息
 */
public class OnlineUserBroadCastMessage extends BaseMessage {
    private User user;//上线／下限用户
    private int outOrInType;//类型
    private long messageTime;//时间

    public OnlineUserBroadCastMessage(String json) {
        super(json);
        OnlineUserBroadCastMessage broadCastMessage = (OnlineUserBroadCastMessage) JSON.parse(json);
        this.messageTime = broadCastMessage.getMessageTime();
        this.user = broadCastMessage.getUser();
        this.outOrInType = broadCastMessage.getOutOrInType();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getOutOrInType() {
        return outOrInType;
    }

    public void setOutOrInType(int outOrInType) {
        this.outOrInType = outOrInType;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}