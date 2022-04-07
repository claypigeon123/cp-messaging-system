package com.cp.projects.messagingsystem.aggregatesapp.controller;

import com.cp.projects.messagingsystem.aggregatesapp.model.document.MessageAggregate;
import com.cp.projects.messagingsystem.aggregatesapp.service.MessageAggregateService;
import com.cp.projects.messagingsystem.aggregatesapp.util.Constants;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/aggregates/" + Constants.MESSAGE_AGGREGATE)
public class MessageAggregateController extends AbstractAggregateController<MessageAggregate> {

    public MessageAggregateController(MessageAggregateService service) {
        super(Constants.MESSAGE_AGGREGATE, LoggerFactory.getLogger(MessageAggregateController.class), service);
    }
}
