package com.cp.projects.messagingsystem.aggregatesapp.model.document;

import com.cp.projects.messagingsystem.cpmessagingdomain.document.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@org.springframework.data.mongodb.core.mapping.Document(collection = "users")
public class UserAggregate extends Document {

    @NotBlank
    private String password;

    @NotBlank
    private String displayName;

    private List<String> friends;

    private boolean canReceiveAnonymousMessages;

    public UserAggregate(String id, OffsetDateTime createdDate, OffsetDateTime updatedDate, String password, String displayName, List<String> friends, boolean canReceiveAnonymousMessages) {
        super(id, createdDate, updatedDate);
        this.password = password;
        this.displayName = displayName;
        this.friends = friends;
        this.canReceiveAnonymousMessages = canReceiveAnonymousMessages;
    }
}
