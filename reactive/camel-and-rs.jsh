///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.apache.camel:camel-bom:3.11.1@pom
//DEPS org.apache.camel:camel-core
//DEPS org.apache.camel:camel-stream
//DEPS org.apache.camel:camel-reactive-streams
//DEPS io.reactivex.rxjava3:rxjava:3.0.12
//DEPS org.slf4j:slf4j-simple:1.7.31

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
        from("direct:output")
            .log("Done: ${body}");
    }
});
CamelReactiveStreamsService rsCamel = CamelReactiveStreams.get(camel);
try {
    camel.start();

    // Camel route & Reactive Streams flow
    Flowable.fromPublisher(rsCamel.from("seda:input"))
        .map(e -> {
            int i = e.getMessage().getBody(Integer.class);
            e.getMessage().setBody(i * i);
            return e;
        })
        .doOnNext(e -> rsCamel.to("stream:out", e))
        .subscribe(rsCamel.subscriber("direct:output"));

    FluentProducerTemplate template = camel.createFluentProducerTemplate();
    IntStream.rangeClosed(1, 5).forEach(i ->
        template.withBody(i).to("seda:input").send());

    Thread.sleep(1000);

} finally {
    camel.stop();
}
