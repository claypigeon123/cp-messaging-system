package com.cp.projects.messagingsystem.messagingcontrollerapp.repository;

import com.cp.projects.messagingsystem.messagingcontrollerapp.model.document.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

}
