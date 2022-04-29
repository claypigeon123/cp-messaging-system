package com.cp.projects.messagingsystem.aggregatesapp.service;

import com.cp.projects.messagingsystem.aggregatesapp.model.document.MessageAggregate;
import com.cp.projects.messagingsystem.aggregatesapp.repository.MessageRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Clock;
import java.time.OffsetDateTime;

@Service
public class MessageAggregateService extends AbstractAggregateService<MessageAggregate> {

    public MessageAggregateService(Clock clock, MessageRepository repository) {
        super(clock, repository);
    }

    public Flux<MessageAggregate> findAllByConversationIdAndCreatedDateBefore(String conversationId, OffsetDateTime before, int limit) {
        Pageable pageable = PageRequest.ofSize(limit).withSort(Sort.Direction.DESC, "createdDate");
        return ((MessageRepository)repository).findAllByConversationIdAndCreatedDateBefore(conversationId, before, pageable);
    }
}
