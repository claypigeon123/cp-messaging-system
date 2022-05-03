package com.cp.projects.messagingsystem.authserverapp.api

import com.cp.projects.messagingsystem.authserverapp.config.props.AuthServerAppProperties
import com.cp.projects.messagingsystem.clients.reactive.aggregatesapp.client.AggregatesAppReactiveClient
import com.cp.projects.messagingsystem.cpmessagingdomain.document.User
import com.cp.projects.messagingsystem.cpmessagingdomain.exception.ExceptionResponse
import com.cp.projects.messagingsystem.cpmessagingdomain.request.AuthRequest
import com.cp.projects.messagingsystem.cpmessagingdomain.request.RegisterRequest
import com.cp.projects.messagingsystem.cpmessagingdomain.response.AuthResponse
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Mono
import spock.lang.Shared
import spock.lang.Specification

import javax.crypto.SecretKey
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest extends Specification {
    private static final String BASE_PATH = "/auth"
    private static final String AUTH_PATH = "$BASE_PATH/get-token"
    private static final String REGISTER_PATH = "$BASE_PATH/register"

    private static final String USERNAME = "username"
    private static final String RAW_PASSWORD = "password"

    // Helpers
    @Shared
    Clock clock = Clock.fixed(Instant.now().truncatedTo(ChronoUnit.MILLIS), ZoneId.of(ZoneOffset.UTC.id))

    @Shared
    AuthServerAppProperties props = new AuthServerAppProperties(4, 6, "username-short", "pass-short", "pass-nomatch")

    @LocalServerPort
    int port

    @Autowired
    ObjectMapper objectMapper

    @Autowired
    PasswordEncoder passwordEncoder

    WebTestClient webClient

    // Beans
    @SpringBean
    AggregatesAppReactiveClient aggregatesClient = Mock(AggregatesAppReactiveClient)

    @SpringBean
    SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512)

    @SpringBean
    Clock clockBean = clock

    @SpringBean
    AuthServerAppProperties propsBean = props

    void setup() {
        webClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:$port")
            .codecs(configurer -> {
                configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON))
                configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON))
            })
            .build()
    }

    def createUser() {
        return new User(
            USERNAME,
            OffsetDateTime.now(clock).minusMonths(2),
            OffsetDateTime.now(clock).minusWeeks(1),
            passwordEncoder.encode(RAW_PASSWORD),
            USERNAME.concat("-display"),
            new ArrayList<String>(),
            false
        )
    }

    void "valid auth request"() {
        given:
        def authRequest = new AuthRequest(USERNAME, RAW_PASSWORD)

        when:
        def result = webClient.post()
            .uri(AUTH_PATH)
            .body(BodyInserters.fromValue(authRequest))
            .exchange()
            .returnResult(AuthResponse)

        then:
        1 * aggregatesClient.findById(USERNAME, User) >> Mono.just(createUser())

        noExceptionThrown()
        result.status == HttpStatus.OK
        def body = result.responseBody.blockFirst()
        body.validUntil == OffsetDateTime.now(clock).plusSeconds(60 * 60 * 24 * 7)
        body.token.startsWith("Bearer ")

        when:
        Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(body.token.split(" ")[1])

        then:
        noExceptionThrown()
    }

    void "invalid auth request"() {
        given:
        def authRequest = new AuthRequest(username, password)

        when:
        def result = webClient.post()
            .uri(AUTH_PATH)
            .body(BodyInserters.fromValue(authRequest))
            .exchange()
            .returnResult(AuthResponse)

        then:
        noExceptionThrown()
        result.status == HttpStatus.BAD_REQUEST

        where:
        username | password
        USERNAME | null
        null     | RAW_PASSWORD
        null     | null
        ""       | RAW_PASSWORD
        USERNAME | ""
        ""       | ""
    }

    void "valid register request"() {
        given:
        def registerRequest = new RegisterRequest(USERNAME, "displayname", RAW_PASSWORD, RAW_PASSWORD)

        when:
        def result = webClient.post()
            .uri(REGISTER_PATH)
            .body(BodyInserters.fromValue(registerRequest))
            .exchange()
            .returnResult(Void)

        then:
        1 * aggregatesClient.create(_ as User, User) >> Mono.just(createUser())

        noExceptionThrown()
        result.status == HttpStatus.CREATED
    }

    void "invalid register request - basic validation"() {
        given:
        def registerRequest = new RegisterRequest(username, displayName, password, confirmPassword)

        when:
        def result = webClient.post()
            .uri(REGISTER_PATH)
            .body(BodyInserters.fromValue(registerRequest))
            .exchange()
            .returnResult(ExceptionResponse.class)

        then:
        noExceptionThrown()
        result.status == HttpStatus.BAD_REQUEST
        def body = result.responseBody.blockLast()
        body.status == HttpStatus.BAD_REQUEST.value()

        where:
        username   | displayName   | password   | confirmPassword
        null       | null          | null       | null
        ""         | ""            | ""         | ""
        "username" | null          | null       | null
        ""         | null          | null       | null
        null       | "displayname" | null       | null
        null       | ""            | null       | null
        null       | null          | "password" | null
        null       | null          | ""         | null
        null       | null          | null       | "password"
        null       | null          | null       | ""
    }

    void "invalid register request - advanced custom validation"() {
        given:
        def registerRequest = new RegisterRequest(username, displayName, password, confirmPassword)

        when:
        def result = webClient.post()
            .uri(REGISTER_PATH)
            .body(BodyInserters.fromValue(registerRequest))
            .exchange()
            .returnResult(new ParameterizedTypeReference<Map<String, Object>>() {})

        then:
        noExceptionThrown()
        result.status == HttpStatus.BAD_REQUEST

        def body = result.responseBody.blockLast()
        body.status == HttpStatus.BAD_REQUEST.value()

        def actualViolations = ((List<String>) body.violations)
        actualViolations.size() == violations.size()
        actualViolations.containsAll(violations)

        where:
        username   | displayName   | password    | confirmPassword || violations
        "username" | "displayname" | "password"  | "nopers"        || [ props.getMessagePasswordsDontMatch() ]
        "username" | "displayname" | "password"  | "nope"          || [ props.getMessagePasswordsDontMatch(), props.getMessagePasswordTooShort() ]
        "username" | "displayname" | "pass"      | "pass"          || [ props.getMessagePasswordTooShort() ]
        "us"       | "displayname" | "password"  | "password"      || [ props.getMessageUsernameTooShort() ]
        "us"       | "displayname" | "pass"      | "pa"            || [ props.getMessagePasswordsDontMatch(), props.getMessagePasswordTooShort(), props.getMessageUsernameTooShort() ]
        "us"       | "displayname" | "password1" | "password2"     || [ props.getMessagePasswordsDontMatch(), props.getMessageUsernameTooShort() ]
        "us"       | "displayname" | "pass"      | "pass"          || [ props.getMessagePasswordTooShort(), props.getMessageUsernameTooShort() ]
    }
}
