package com.cp.projects.messagingsystem.messagingserver.model.document;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@org.springframework.data.mongodb.core.mapping.Document(collection = "users")
public class User extends Document {
    private String username;

    private String password;

    private String displayName;

    private List<String> friends;

    private boolean canReceiveAnonymousMessages;

    public User(String id, OffsetDateTime createdDate, OffsetDateTime updatedDate, String username, String password, String displayName, List<String> friends, boolean canReceiveAnonymousMessages) {
        super(id, createdDate, updatedDate);
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.friends = friends;
        this.canReceiveAnonymousMessages = canReceiveAnonymousMessages;
    }
}
