///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.apache.camel:camel-bom:3.11.0@pom
//DEPS org.apache.camel:camel-core
//DEPS org.apache.camel:camel-stream
//DEPS org.apache.camel:camel-reactive-streams
//DEPS io.reactivex.rxjava3:rxjava:3.0.12
//DEPS org.slf4j:slf4j-nop:1.7.31

/open PRINTING

import org.apache.camel.*
import org.apache.camel.builder.*
import org.apache.camel.component.reactive.streams.api.*
import org.apache.camel.impl.*

import org.reactivestreams.*
import io.reactivex.rxjava3.core.*

CamelContext camel = new DefaultCamelContext();
camel.addRoutes(new RouteBuilder() {
    public void configure() {
        from("reactive-streams:input")
            .to("stream:out");
    }
});
CamelReactiveStreamsService rsCamel = CamelReactiveStreams.get(camel);
try {
    camel.start();

    // Reactive Streams (pub) -> Camel (sub)
    Flowable.just(1, 2, 3, 4, 5)
        .map(i -> i*i)
        .subscribe(rsCamel.streamSubscriber("input", Integer.class));

    Thread.sleep(1000);

} finally {
    camel.stop();
}
