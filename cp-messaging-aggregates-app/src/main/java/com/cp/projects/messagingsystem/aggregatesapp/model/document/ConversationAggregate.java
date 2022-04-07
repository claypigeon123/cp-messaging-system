package com.cp.projects.messagingsystem.aggregatesapp.model.document;

import com.cp.projects.messagingsystem.cpmessagingdomain.document.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;
import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
@org.springframework.data.mongodb.core.mapping.Document(collection = "conversations")
public class ConversationAggregate extends Document {
    private Collection<String> userIds;

    public ConversationAggregate(String id, OffsetDateTime createdDate, OffsetDateTime updatedDate, Collection<String> userIds) {
        super(id, createdDate, updatedDate);
        this.userIds = userIds;
    }
}
