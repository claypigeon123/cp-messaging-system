package com.cp.projects.messagingsystem.aggregatesapp.api;

import com.cp.projects.messagingsystem.aggregatesapp.model.document.MessageAggregate;
import com.cp.projects.messagingsystem.aggregatesapp.service.MessageAggregateService;
import com.cp.projects.messagingsystem.aggregatesapp.util.Constants;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

@Validated
@RestController
@RequestMapping("/aggregates/" + Constants.MESSAGE_AGGREGATE)
public class MessageAggregateController extends AbstractAggregateController<MessageAggregate> {

    public MessageAggregateController(MessageAggregateService service) {
        super(Constants.MESSAGE_AGGREGATE, LoggerFactory.getLogger(MessageAggregateController.class), service);
    }

    @GetMapping("/query/by-conversation-id/{conversationId}")
    public Flux<MessageAggregate> findAllByConversationId(
        @PathVariable @NotBlank String conversationId,
        @RequestParam(required = false) OffsetDateTime before,
        @RequestParam(defaultValue = "20") int limit
    ) {
        log.debug("Request to get {} aggregate by user id: {}", aggregateType, conversationId);

        if (before == null) {
            before = OffsetDateTime.now();
        }

        return ((MessageAggregateService)service).findAllByConversationIdAndCreatedDateBefore(conversationId, before, limit);
    }
}
