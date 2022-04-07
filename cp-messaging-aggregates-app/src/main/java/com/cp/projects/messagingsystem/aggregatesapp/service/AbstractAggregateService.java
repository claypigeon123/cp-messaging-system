package com.cp.projects.messagingsystem.aggregatesapp.service;

import com.cp.projects.messagingsystem.aggregatesapp.model.exception.AggregateNotFoundException;
import com.cp.projects.messagingsystem.aggregatesapp.model.exception.IdTakenException;
import com.cp.projects.messagingsystem.cpmessagingdomain.document.Document;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;

public abstract class AbstractAggregateService<AGGREGATE_TYPE extends Document> {

    private final Clock clock;
    private final ReactiveMongoRepository<AGGREGATE_TYPE, String> repository;

    public AbstractAggregateService(Clock clock, ReactiveMongoRepository<AGGREGATE_TYPE, String> repository) {
        this.clock = clock;
        this.repository = repository;
    }

    public Mono<AGGREGATE_TYPE> findById(String id) {
        return repository.findById(id)
            .switchIfEmpty(Mono.error(AggregateNotFoundException::new));
    }

    public Flux<AGGREGATE_TYPE> findAllById(Collection<String> ids) {
        return repository.findAllById(ids);
    }

    @Transactional
    public Mono<AGGREGATE_TYPE> create(AGGREGATE_TYPE aggregate) {
        return repository.existsById(aggregate.getId())
            .flatMap(exists -> exists ? Mono.error(new IdTakenException()) : Mono.just(aggregate))
            .map(this::prepareForInsertion)
            .flatMap(repository::save);
    }

    public Mono<AGGREGATE_TYPE> update(String id, AGGREGATE_TYPE aggregate) {
        return repository.findById(id)
            .switchIfEmpty(Mono.error(AggregateNotFoundException::new))
            .flatMap(previous -> {
                aggregate.setId(previous.getId());
                aggregate.setCreatedDate(previous.getCreatedDate());
                aggregate.setUpdatedDate(OffsetDateTime.now(clock));
                return repository.save(aggregate);
            });
    }

    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }

    // --

    protected AGGREGATE_TYPE prepareForInsertion(AGGREGATE_TYPE aggregate) {
        OffsetDateTime now = OffsetDateTime.now(clock);
        aggregate.setCreatedDate(now);
        aggregate.setUpdatedDate(now);

        if (aggregate.getId() == null || aggregate.getId().isBlank()) {
            aggregate.setId(UUID.randomUUID().toString());
        }

        return aggregate;
    }
}
