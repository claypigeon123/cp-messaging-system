package com.cp.projects.messagingsystem.messagingcontrollerapp.repository;

import com.cp.projects.messagingsystem.messagingcontrollerapp.model.document.Message;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends ReactiveMongoRepository<Message, String> {

}
