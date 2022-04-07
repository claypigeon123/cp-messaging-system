package com.cp.projects.messagingsystem.aggregatesapp.repository;

import com.cp.projects.messagingsystem.aggregatesapp.model.document.MessageAggregate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends ReactiveMongoRepository<MessageAggregate, String> {

}
