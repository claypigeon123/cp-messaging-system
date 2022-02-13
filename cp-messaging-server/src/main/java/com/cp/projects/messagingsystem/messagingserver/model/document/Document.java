package com.cp.projects.messagingsystem.messagingserver.model.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Document {
    private String id;

    private OffsetDateTime createdDate;

    private OffsetDateTime updatedDate;
}
