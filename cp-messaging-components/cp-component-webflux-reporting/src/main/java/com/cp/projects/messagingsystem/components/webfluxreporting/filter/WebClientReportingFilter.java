package com.cp.projects.messagingsystem.components.webfluxreporting.filter;

import com.cp.projects.messagingsystem.components.webfluxreporting.properties.WebClientReportingFilterProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RequiredArgsConstructor
public class WebClientReportingFilter implements ExchangeFilterFunction {
    private static final Logger log = LoggerFactory.getLogger(WebClientReportingFilter.class);

    private final WebClientReportingFilterProperties props;

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        if (props.getExcludedPaths().contains(request.url().getPath())) {
            return next.exchange(request);
        }

        String baseUri = request.url().getScheme() + "://" + request.url().getHost() + (request.url().getPort() != -1 ? ":" + request.url().getPort() : "");
        String queryString = Optional.ofNullable(request.url().getQuery())
            .map("?"::concat)
            .orElse("");
        String method = request.method().toString();
        String path = request.url().getPath().concat(queryString);

        log.debug("API Request: [method=\"{}\", base=\"{}\", path=\"{}\"]", method, baseUri, path);

        long start = System.currentTimeMillis();
        return next.exchange(request).map(response -> {
            long duration = System.currentTimeMillis() - start;
            log.debug("API Response: [method=\"{}\", base=\"{}\", path=\"{}\", statusCode=\"{}\", duration=\"{}ms\"]", method, baseUri, path, response.rawStatusCode(), duration);

            return response;
        });
    }
}
