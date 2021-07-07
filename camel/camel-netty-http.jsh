///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.apache.camel:camel-bom:3.11.0@pom
//DEPS org.apache.camel:camel-core
//DEPS org.apache.camel:camel-main
//DEPS org.apache.camel:camel-stream
//DEPS org.apache.camel:camel-netty-http
//DEPS org.slf4j:slf4j-nop:1.7.31

/open camel/camel-imports
/open JAVASE
/open PRINTING

import io.netty.buffer.*

import org.apache.camel.component.netty.*

Main main = new Main();
main.configure().addRoutesBuilder(new RouteBuilder() {
    public void configure() throws Exception {
        from("netty-http:http://localhost:9999/test?disableStreamCache=false")
            .toD("direct:end?pollingConsumerQueueSize=1000&pollingConsumerBlockTimeout=0&pollingConsumerBlockWhenFull=true")
            //.setBody().constant("OK")
            ;
        from("direct:end")
            .process(e -> {
                StreamCache cache = e.getMessage().getBody(StreamCache.class);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                cache.writeTo(os);
                print(new String(os.toByteArray(), StandardCharsets.UTF_8));
            })
            //.to("stream:out")
            ;
            /*
            .convertBodyTo(String.class)
            .process(e -> {
               println(e.getMessage().getBody().getClass().getName()); 
            })
            .process(e -> e.getMessage().setBody(e.getMessage().getBody().toString()))
            .to("stream:out")
            .transform().constant("Hello from Netty HTTP!\n");
            */
    }
});
main.run();
