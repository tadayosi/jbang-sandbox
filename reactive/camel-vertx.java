///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.apache.camel:camel-bom:3.11.0@pom
//DEPS io.vertx:vertx-core:4.0.3
//DEPS org.apache.camel:camel-core
//DEPS org.apache.camel:camel-stream
//DEPS org.apache.camel:camel-vertx
//DEPS org.slf4j:slf4j-simple:1.7.31

import org.apache.camel.*;
import org.apache.camel.builder.*;
import org.apache.camel.impl.*;
import org.apache.camel.component.vertx.*;

import io.vertx.core.*;
import io.vertx.core.eventbus.*;

import static java.lang.System.*;

class camel_vertx {
    public static void main(String[] args) throws Exception {
        // logging settings
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "INFO");

        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new LoggingVerticle());

        EventBus eventBus = vertx.eventBus();
        vertx.createHttpServer()
            .requestHandler(req -> {
                eventBus.send("camel-logging", "Got request: " + req.method() + " " + req.uri());
                req.response().end("Hello!");
            })
            .listen(8888)
            .onSuccess(s -> out.println("HTTP server started on port " + s.actualPort()));

        CamelContext camel = new DefaultCamelContext();
        camel.getComponent("vertx", VertxComponent.class).setVertx(vertx);
        camel.addRoutes(new RouteBuilder() {
            public void configure() {
                from("vertx:camel-logging")
                    .log("body = ${body}")
                    .to("direct:logging");
                from("direct:logging")
                    .to("vertx:logging");
            }
        });
        camel.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            vertx.close();
            camel.stop();
            try { camel.close(); } catch (Exception e) {}
        }));
    }

    static class LoggingVerticle extends AbstractVerticle {
        public void start() {
            out.println("[Logging] " + getClass().getSimpleName() + " running");
            vertx.eventBus().<String>consumer("logging",
                m -> out.println("[Logging] " + m.body()));
        }
    }
}
