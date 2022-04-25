package com.cp.projects.messagingsystem.cpmessagingdomain.document;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;
import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
public class Conversation extends Document {
    private Collection<String> userIds;

    public Conversation(String id, OffsetDateTime createdDate, OffsetDateTime updatedDate, Collection<String> userIds) {
        super(id, createdDate, updatedDate);
        this.userIds = userIds;
    }
}
