///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.apache.camel:camel-bom:3.11.0@pom
//DEPS org.apache.camel:camel-core
//DEPS org.apache.camel:camel-stream
//DEPS org.apache.camel:camel-reactive-streams
//DEPS io.reactivex.rxjava3:rxjava:3.0.12
//DEPS org.slf4j:slf4j-nop:1.7.31

/open PRINTING

import org.apache.camel.*
import org.apache.camel.component.reactive.streams.api.*
import org.apache.camel.impl.*

import org.reactivestreams.*
import io.reactivex.rxjava3.core.*

CamelContext camel = new DefaultCamelContext();
CamelReactiveStreamsService rsCamel = CamelReactiveStreams.get(camel);
try {
    camel.start();

    // Camel (pub) -> Reactive Streams (sub)
    //Publisher<Integer> publisher = rsCamel.from("seda:input", Integer.class);
    Publisher<Integer> publisher = rsCamel.fromStream("input", Integer.class);
    Flowable.fromPublisher(publisher)
        .map(i -> i*i)
        .doOnNext(System.out::println)
        .subscribe();

    FluentProducerTemplate template = camel.createFluentProducerTemplate();
    IntStream.rangeClosed(1, 5).forEach(i ->
        //template.withBody(i).to("seda:input").send());
        template.withBody(i).to("reactive-streams:input").send());

    Thread.sleep(1000);

} finally {
    camel.stop();
}
