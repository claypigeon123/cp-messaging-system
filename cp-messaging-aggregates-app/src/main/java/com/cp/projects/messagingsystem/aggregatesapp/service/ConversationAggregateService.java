package com.cp.projects.messagingsystem.aggregatesapp.service;

import com.cp.projects.messagingsystem.aggregatesapp.model.document.ConversationAggregate;
import com.cp.projects.messagingsystem.aggregatesapp.repository.ConversationRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class ConversationAggregateService extends AbstractAggregateService<ConversationAggregate> {

    public ConversationAggregateService(Clock clock, ConversationRepository repository) {
        super(clock, repository);
    }
}
