package com.cp.projects.messagingsystem.messagingcontrollerapp.model.document;

import com.cp.projects.messagingsystem.cpmessagingdomain.document.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@org.springframework.data.mongodb.core.mapping.Document(collection = "messages")
@NoArgsConstructor
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
