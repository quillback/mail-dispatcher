package com.coris.example.maildispatcher.network.inbound;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Map;

public interface Communication<M> {

    WebClient webClient();

    Mono<M> getOne(Query query);

    Flux<M> getMany(Query query);


    @Getter
    class Query{
        final String accountNumber;
        final LocalDate toExclusive;
        final LocalDate fromInclusive;
        final Map<String, Object> params;

        final Class<?> expect;


        @Builder
        public Query(String accountNumber, LocalDate toExclusive, LocalDate fromInclusive, Map<String, Object> params, Class<?> expect) {
            this.accountNumber = accountNumber;
            this.toExclusive = toExclusive;
            this.fromInclusive = fromInclusive;
            this.params = params;
            this.expect = expect;
        }
    }
}

