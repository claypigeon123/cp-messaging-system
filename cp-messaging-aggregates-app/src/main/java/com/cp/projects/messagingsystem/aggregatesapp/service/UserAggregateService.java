package com.cp.projects.messagingsystem.aggregatesapp.service;

import com.cp.projects.messagingsystem.aggregatesapp.model.document.UserAggregate;
import com.cp.projects.messagingsystem.aggregatesapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class UserAggregateService extends AbstractAggregateService<UserAggregate> {

    public UserAggregateService(Clock clock, UserRepository repository) {
        super(clock, repository);
    }
}
