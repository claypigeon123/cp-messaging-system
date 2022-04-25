package com.cp.projects.messagingsystem.cpmessagingdomain.document;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class Message extends Document {
    private String conversationId;

    private String senderId;

    private String content;

    public Message(String id, OffsetDateTime createdDate, OffsetDateTime updatedDate, String conversationId, String senderId, String content) {
        super(id, createdDate, updatedDate);
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;
    }
}
