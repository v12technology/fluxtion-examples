package com.fluxtion.example.cookbook.rocket;

import io.rsocket.ConnectionSetupPayload;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Mono;

public class PingHandler implements SocketAcceptor {
    @Override
    public Mono<RSocket> accept(ConnectionSetupPayload setup, RSocket rsocket) {
        rsocket
                .requestStream(DefaultPayload.create("Hello-Bidi"))
                .map(Payload::getDataUtf8)
                .log()
                .subscribe();

        return Mono.just(new RSocket() {});
    }
}
