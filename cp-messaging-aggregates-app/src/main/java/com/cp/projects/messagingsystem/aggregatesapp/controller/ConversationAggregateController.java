package com.cp.projects.messagingsystem.aggregatesapp.controller;

import com.cp.projects.messagingsystem.aggregatesapp.model.document.ConversationAggregate;
import com.cp.projects.messagingsystem.aggregatesapp.service.ConversationAggregateService;
import com.cp.projects.messagingsystem.aggregatesapp.util.Constants;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/aggregates/" + Constants.CONVERSATION_AGGREGATE)
public class ConversationAggregateController extends AbstractAggregateController<ConversationAggregate> {

    public ConversationAggregateController(ConversationAggregateService service) {
        super(Constants.CONVERSATION_AGGREGATE, LoggerFactory.getLogger(ConversationAggregateController.class), service);
    }
}
