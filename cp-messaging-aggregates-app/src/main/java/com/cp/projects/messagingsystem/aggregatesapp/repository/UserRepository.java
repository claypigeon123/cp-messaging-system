package com.cp.projects.messagingsystem.aggregatesapp.repository;

import com.cp.projects.messagingsystem.aggregatesapp.model.document.UserAggregate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserAggregate, String> {

}
