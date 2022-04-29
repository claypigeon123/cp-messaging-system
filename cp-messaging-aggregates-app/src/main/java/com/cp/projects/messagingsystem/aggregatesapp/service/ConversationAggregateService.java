package com.cp.projects.messagingsystem.aggregatesapp.service;

import com.cp.projects.messagingsystem.aggregatesapp.model.document.ConversationAggregate;
import com.cp.projects.messagingsystem.aggregatesapp.repository.ConversationRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Clock;

@Service
public class ConversationAggregateService extends AbstractAggregateService<ConversationAggregate> {

    public ConversationAggregateService(Clock clock, ConversationRepository repository) {
        super(clock, repository);
    }

    public Flux<ConversationAggregate> findAllByUserId(String userId) {
        return ((ConversationRepository)repository).findAllByUserIdsContains(userId);
    }
}
