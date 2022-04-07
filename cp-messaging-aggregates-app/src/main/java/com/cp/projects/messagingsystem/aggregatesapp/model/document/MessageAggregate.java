package com.cp.projects.messagingsystem.aggregatesapp.model.document;

import com.cp.projects.messagingsystem.cpmessagingdomain.document.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@org.springframework.data.mongodb.core.mapping.Document(collection = "messages")
public class MessageAggregate extends Document {
    @NotBlank
    private String conversationId;

    @NotBlank
    private String senderId;

    @NotBlank
    private String content;

    public MessageAggregate(String id, OffsetDateTime createdDate, OffsetDateTime updatedDate, String conversationId, String senderId, String content) {
        super(id, createdDate, updatedDate);
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;
    }
}
