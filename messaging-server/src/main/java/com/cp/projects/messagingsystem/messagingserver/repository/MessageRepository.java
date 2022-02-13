package com.cp.projects.messagingsystem.messagingserver.repository;

import com.cp.projects.messagingsystem.messagingserver.model.Message;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends ReactiveMongoRepository<Message, String> {

}
