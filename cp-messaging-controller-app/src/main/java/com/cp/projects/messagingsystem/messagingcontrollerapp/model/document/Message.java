package com.cp.projects.messagingsystem.messagingcontrollerapp.model.document;

import com.cp.projects.messagingsystem.cpmessagingdomain.document.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@org.springframework.data.mongodb.core.mapping.Document(collection = "messages")
public class Message extends Document {
    private String conversationId;

    private String senderId;

    private String message;

    public Message(String id, OffsetDateTime createdDate, OffsetDateTime updatedDate, String conversationId, String senderId, String message) {
        super(id, createdDate, updatedDate);
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.message = message;
    }
}
