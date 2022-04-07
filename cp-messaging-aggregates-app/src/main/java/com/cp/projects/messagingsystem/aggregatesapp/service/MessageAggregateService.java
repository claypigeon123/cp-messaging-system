package com.cp.projects.messagingsystem.aggregatesapp.service;

import com.cp.projects.messagingsystem.aggregatesapp.model.document.MessageAggregate;
import com.cp.projects.messagingsystem.aggregatesapp.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class MessageAggregateService extends AbstractAggregateService<MessageAggregate> {

    public MessageAggregateService(Clock clock, MessageRepository repository) {
        super(clock, repository);
    }
}
