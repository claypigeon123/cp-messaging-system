package com.cp.projects.messagingsystem.messagingcontrollerapp.repository;

import com.cp.projects.messagingsystem.messagingcontrollerapp.model.document.Conversation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends ReactiveMongoRepository<Conversation, String> {

}
