package com.cp.projects.messagingsystem.aggregatesapp.controller;

import com.cp.projects.messagingsystem.aggregatesapp.service.AbstractAggregateService;
import com.cp.projects.messagingsystem.cpmessagingdomain.document.Document;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;

public abstract class AbstractAggregateController<AGGREGATE_TYPE extends Document> {
    protected final String aggregateType;
    protected final Logger log;
    protected final AbstractAggregateService<AGGREGATE_TYPE> service;

    public AbstractAggregateController(String aggregateType, Logger log, AbstractAggregateService<AGGREGATE_TYPE> service) {
        this.aggregateType = aggregateType;
        this.log = log;
        this.service = service;
    }

    @GetMapping("/query/by-id/{id}")
    public Mono<AGGREGATE_TYPE> findById(
        @PathVariable @NotBlank String id
    ) {
        log.debug("Request to get {} aggregate by id: {}", aggregateType, id);
        return service.findById(id);
    }

    @PostMapping("/query/by-ids")
    public Flux<AGGREGATE_TYPE> findAllById(
        @RequestBody @NotEmpty Collection<String> ids
    ) {
        log.debug("Request to get {} aggregates by ids: {}", aggregateType, ids);
        return service.findAllById(ids);
    }

    @PostMapping
    public Mono<AGGREGATE_TYPE> create(
        @RequestBody @Valid AGGREGATE_TYPE aggregate
    ) {
        log.info("{}", aggregate);
        log.debug("Request to create new {} aggregate", aggregateType);
        return service.create(aggregate);
    }

    @PutMapping("/by-id/{id}")
    public Mono<AGGREGATE_TYPE> update(
        @PathVariable @NotBlank String id,
        @RequestBody @Valid AGGREGATE_TYPE aggregate
    ) {
        log.debug("Request to update {} aggregate by id: {}", aggregateType, id);
        return service.update(id, aggregate);
    }

    @DeleteMapping("/by-id/{id}")
    public Mono<Void> deleteById(
        @PathVariable @NotBlank String id
    ) {
        log.debug("Request to delete {} aggregate by id: {}", aggregateType, id);
        return service.deleteById(id);
    }
}
