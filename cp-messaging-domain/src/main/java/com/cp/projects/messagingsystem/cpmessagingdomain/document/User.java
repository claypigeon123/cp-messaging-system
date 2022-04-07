package com.cp.projects.messagingsystem.cpmessagingdomain.document;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends Document {
    private static final String TYPE = "user";
    private String password;

    private String displayName;

    private List<String> friends;

    private boolean canReceiveAnonymousMessages;

    public User(String id, OffsetDateTime createdDate, OffsetDateTime updatedDate, String password, String displayName, List<String> friends, boolean canReceiveAnonymousMessages) {
        super(id, createdDate, updatedDate);
        this.password = password;
        this.displayName = displayName;
        this.friends = friends;
        this.canReceiveAnonymousMessages = canReceiveAnonymousMessages;
    }
}
