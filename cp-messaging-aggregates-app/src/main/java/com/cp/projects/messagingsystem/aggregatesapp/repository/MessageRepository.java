package com.cp.projects.messagingsystem.aggregatesapp.repository;

import com.cp.projects.messagingsystem.aggregatesapp.model.document.MessageAggregate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.OffsetDateTime;

@Repository
public interface MessageRepository extends ReactiveMongoRepository<MessageAggregate, String> {
    Flux<MessageAggregate> findAllByConversationIdAndCreatedDateBefore(String conversationId, OffsetDateTime before, Pageable pageable);
}
