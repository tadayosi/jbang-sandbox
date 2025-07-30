///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS io.vertx:vertx-stack-depchain:4.5.16@pom
//DEPS io.vertx:vertx-core
//DEPS org.slf4j:slf4j-simple:2.0.17

import io.vertx.core.*;
import io.vertx.core.eventbus.*;

import static java.lang.System.*;

class vertx {
    public static void main(String[] args) {
        // logging settings
        setProperty("org.slf4j.simpleLogger.defaultLogLevel", "INFO");

        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new LoggingVerticle());

        EventBus eventBus = vertx.eventBus();
        vertx.createHttpServer()
            .requestHandler(req -> {
                eventBus.send("logging", "Got request: " + req.method() + " " + req.uri());
                req.response().end("Hello!");
            })
            .listen(8888)
            .onSuccess(s -> out.println("HTTP server started on port: " + s.actualPort()));

        Runtime.getRuntime().addShutdownHook(new Thread(vertx::close));
    }

    static class LoggingVerticle extends AbstractVerticle {
        public void start() {
            out.println("[Logging] " + getClass().getSimpleName() + " running");
            vertx.eventBus().consumer("logging", m -> out.println("[Logging] " + m.body()));
        }
    }
}
