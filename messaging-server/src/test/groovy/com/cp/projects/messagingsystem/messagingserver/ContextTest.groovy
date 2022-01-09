package com.cp.projects.messagingsystem.messagingserver

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class ContextTest extends Specification {

    def "context loads"() {
        expect:
        2 + 2 == 4
    }
}
