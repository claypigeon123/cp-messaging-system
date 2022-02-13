package com.cp.projects.messagingsystem.messagingserver.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@org.springframework.data.mongodb.core.mapping.Document(collection = "messages")
public class Message extends Document {
    private String sender;

    private String receiver;

    private String message;

    public Message(String id, OffsetDateTime createdDate, OffsetDateTime updatedDate, String sender, String receiver, String message) {
        super(id, createdDate, updatedDate);
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }
}
