package com.cp.projects.messagingsystem.aggregatesapp.api;

import com.cp.projects.messagingsystem.aggregatesapp.model.document.ConversationAggregate;
import com.cp.projects.messagingsystem.aggregatesapp.service.ConversationAggregateService;
import com.cp.projects.messagingsystem.aggregatesapp.util.Constants;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.constraints.NotBlank;

@Validated
@RestController
@RequestMapping("/aggregates/" + Constants.CONVERSATION_AGGREGATE)
public class ConversationAggregateController extends AbstractAggregateController<ConversationAggregate> {

    public ConversationAggregateController(ConversationAggregateService service) {
        super(Constants.CONVERSATION_AGGREGATE, LoggerFactory.getLogger(ConversationAggregateController.class), service);
    }

    @GetMapping("/query/by-user-id/{userId}")
    public Flux<ConversationAggregate> findAllByUserId(
        @PathVariable @NotBlank String userId
    ) {
        log.debug("Request to get {} aggregate by user id: {}", aggregateType, userId);
        return ((ConversationAggregateService) service).findAllByUserId(userId);
    }
}
