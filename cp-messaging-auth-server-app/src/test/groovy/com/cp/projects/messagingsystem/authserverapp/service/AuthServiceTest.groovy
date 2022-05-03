package com.cp.projects.messagingsystem.authserverapp.service

import com.cp.projects.messagingsystem.clients.reactive.aggregatesapp.client.AggregatesAppReactiveClient
import com.cp.projects.messagingsystem.cpmessagingdomain.document.User
import com.cp.projects.messagingsystem.cpmessagingdomain.exception.CpMessagingSystemException
import com.cp.projects.messagingsystem.cpmessagingdomain.request.AuthRequest
import com.cp.projects.messagingsystem.cpmessagingdomain.request.RegisterRequest
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import spock.lang.Specification

import javax.crypto.SecretKey
import java.nio.charset.Charset
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class AuthServiceTest extends Specification {
    private static final String USERNAME = "username"
    private static final String RAW_PASSWORD = "password"
    private static final String ENCODED_PASSWORD = "encoded-password"

    // Beans
    AggregatesAppReactiveClient aggregatesClient
    PasswordEncoder passwordEncoder
    Clock clock
    SecretKey secretKey

    // Target
    AuthService service

    void setup() {
        aggregatesClient = Mock(AggregatesAppReactiveClient)
        passwordEncoder = Mock(PasswordEncoder)
        clock = Clock.fixed(Instant.now(), ZoneId.of(ZoneOffset.UTC.id))
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512)

        service = new AuthService(aggregatesClient, passwordEncoder, clock, secretKey)
    }

    def createUser() {
        return new User(
            USERNAME,
            OffsetDateTime.now(clock).minusMonths(2),
            OffsetDateTime.now(clock).minusWeeks(1),
            ENCODED_PASSWORD,
            USERNAME.concat("-display"),
            new ArrayList<String>(),
            false
        )
    }

    void "auth request - valid"() {
        given:
        def user = createUser()
        def authRequest = new AuthRequest(USERNAME, RAW_PASSWORD)

        when:
        def result = service.auth(authRequest).block()

        then:
        1 * aggregatesClient.findById(authRequest.username, User) >> Mono.just(user)
        1 * passwordEncoder.matches(_, _) >> true

        noExceptionThrown()
        !result.token.blank
        result.token.split("\\.").length == 3
        result.validUntil.isAfter(OffsetDateTime.now(clock))
    }

    void "auth request - no user found"() {
        given:
        def authRequest = new AuthRequest(USERNAME, RAW_PASSWORD)

        when:
        service.auth(authRequest).block()

        then:
        1 * aggregatesClient.findById(authRequest.username, User) >>
            Mono.error(WebClientResponseException.NotFound.create(HttpStatus.NOT_FOUND.value(), "Not found", new HttpHeaders(), new byte[]{}, Charset.defaultCharset()))

        def ex = thrown(CpMessagingSystemException)
        ex.getStatus() == HttpStatus.UNAUTHORIZED.value()
    }

    void "auth request - user found, wrong password"() {
        given:
        def user = createUser()
        def authRequest = new AuthRequest(USERNAME, "password-that-shouldn't-match")

        when:
        service.auth(authRequest).block()

        then:
        1 * aggregatesClient.findById(authRequest.username, User) >> Mono.just(user)
        1 * passwordEncoder.matches(_, _) >> false

        def ex = thrown(CpMessagingSystemException)
        ex.status == HttpStatus.UNAUTHORIZED.value()
    }

    void "register request - valid"() {
        given:
        def savedUser = createUser()
        def registerRequest = new RegisterRequest(savedUser.id, savedUser.displayName, RAW_PASSWORD, RAW_PASSWORD)

        when:
        service.register(registerRequest).block()

        then:
        1 * passwordEncoder.encode(registerRequest.password) >> ENCODED_PASSWORD
        1 * aggregatesClient.create(_ as User, User) >> Mono.just(savedUser)

        noExceptionThrown()
    }

    void "register request - exception by aggregates client thrown forward as is"() {
        given:
        def savedUser = createUser()
        def registerRequest = new RegisterRequest(savedUser.id, savedUser.displayName, RAW_PASSWORD, RAW_PASSWORD)

        when:
        service.register(registerRequest).block()

        then:
        1 * passwordEncoder.encode(registerRequest.password) >> ENCODED_PASSWORD
        1 * aggregatesClient.create(_ as User, User) >> Mono.error(WebClientResponseException.Conflict.create(HttpStatus.CONFLICT.value(), "Conflict", new HttpHeaders(), new byte[]{}, Charset.defaultCharset()))

        def ex = thrown(WebClientResponseException.Conflict)
        ex.statusCode == HttpStatus.CONFLICT
    }
}