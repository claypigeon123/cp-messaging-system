package com.cp.projects.messagingsystem.aggregatesapp.service;

import com.cp.projects.messagingsystem.aggregatesapp.model.document.UserAggregate;
import com.cp.projects.messagingsystem.aggregatesapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class UserAggregateService extends AbstractAggregateService<UserAggregate> {

    private final PasswordEncoder encoder;

    public UserAggregateService(Clock clock, UserRepository repository, PasswordEncoder encoder) {
        super(clock, repository);
        this.encoder = encoder;
    }

    @Override
    protected UserAggregate prepareForInsertion(UserAggregate aggregate) {
        super.prepareForInsertion(aggregate);
        String hash = encoder.encode(aggregate.getPassword());
        aggregate.setPassword(hash);
        return aggregate;
    }
}
