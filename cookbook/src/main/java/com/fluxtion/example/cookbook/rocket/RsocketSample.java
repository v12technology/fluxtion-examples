package com.fluxtion.example.cookbook.rocket;

import io.rsocket.core.RSocketServer;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.server.TcpServerTransport;
import reactor.core.publisher.Mono;

public class RsocketSample {

    public static void main(String[] args) {
        RSocketServer.create(new PingHandler())
                .payloadDecoder(PayloadDecoder.ZERO_COPY)
                .bind(TcpServerTransport.create(7878))
                .block()
                .onClose();

    }
}
