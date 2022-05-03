package com.cp.projects.messagingsystem.clients.reactive.aggregatesapp.client;

import com.cp.projects.messagingsystem.clients.reactive.aggregatesapp.properties.AggregatesAppClientProperties;
import com.cp.projects.messagingsystem.cpmessagingdomain.document.Conversation;
import com.cp.projects.messagingsystem.cpmessagingdomain.document.Document;
import com.cp.projects.messagingsystem.cpmessagingdomain.document.Message;
import com.cp.projects.messagingsystem.cpmessagingdomain.document.User;
import com.cp.projects.messagingsystem.cpmessagingdomain.exception.CpMessagingSystemException;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AggregatesAppReactiveClient {
    private final AggregatesAppClientProperties props;
    private final WebClient webClient;

    private final Map<Class<? extends Document>, String> pathVarMap;

    public AggregatesAppReactiveClient(AggregatesAppClientProperties props, WebClient webClient) {
        this.props = props;
        this.webClient = webClient;

        this.pathVarMap = new ConcurrentHashMap<>(Map.of(
            Message.class, props.getMessagesPathVariable(),
            Conversation.class, props.getConversationsPathVariable(),
            User.class, props.getUsersPathVariable()
        ));
    }

    public <T extends Document> Mono<T> findById(String id, Class<T> clazz) {
        String type = decideType(clazz);
        URI uri = UriComponentsBuilder.fromUriString(props.getFindByIdUri()).build(type, id);

        return webClient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(clazz);
    }

    public <T extends Document> Flux<T> findAllByIds(Collection<String> ids, Class<T> clazz) {
        String type = decideType(clazz);
        URI uri = UriComponentsBuilder.fromUriString(props.getFindAllByIdsUri()).build(type);

        return webClient.post()
            .uri(uri)
            .body(BodyInserters.fromValue(ids))
            .retrieve()
            .bodyToFlux(clazz);
    }

    public <T extends Document> Mono<T> create(T aggregate, Class<T> clazz) {
        String type = decideType(clazz);
        URI uri = UriComponentsBuilder.fromUriString(props.getAggregatesUri()).build(type);

        return webClient.post()
            .uri(uri)
            .body(BodyInserters.fromValue(aggregate))
            .retrieve()
            .bodyToMono(clazz);
    }

    public <T extends Document> Mono<T> update(String id, T aggregate, Class<T> clazz) {
        String type = decideType(clazz);
        URI uri = UriComponentsBuilder.fromUriString(props.getAggregatesIdUri()).build(type, id);

        return webClient.put()
            .uri(uri)
            .body(BodyInserters.fromValue(aggregate))
            .retrieve()
            .bodyToMono(clazz);
    }

    public <T extends Document> Mono<Void> delete(String id, Class<T> clazz) {
        String type = decideType(clazz);
        URI uri = UriComponentsBuilder.fromUriString(props.getAggregatesIdUri()).build(type, id);

        return webClient.delete()
            .uri(uri)
            .retrieve()
            .bodyToMono(Void.class);
    }

    // --

    private <T> String decideType(Class<T> clazz) {
        if (!pathVarMap.containsKey(clazz)) {
            throw new CpMessagingSystemException("Bad aggregate type", HttpStatus.BAD_REQUEST.value());
        }

        return pathVarMap.get(clazz);
    }
}
