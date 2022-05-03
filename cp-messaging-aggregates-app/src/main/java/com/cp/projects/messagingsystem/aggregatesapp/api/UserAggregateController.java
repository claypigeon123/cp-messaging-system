package com.cp.projects.messagingsystem.aggregatesapp.api;

import com.cp.projects.messagingsystem.aggregatesapp.model.document.UserAggregate;
import com.cp.projects.messagingsystem.aggregatesapp.service.UserAggregateService;
import com.cp.projects.messagingsystem.aggregatesapp.util.Constants;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/aggregates/" + Constants.USER_AGGREGATE)
public class UserAggregateController extends AbstractAggregateController<UserAggregate> {

    public UserAggregateController(UserAggregateService service) {
        super(Constants.USER_AGGREGATE, LoggerFactory.getLogger(UserAggregateController.class), service);
    }
}
