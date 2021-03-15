///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.apache.camel:camel-core-engine:3.8.0
//DEPS org.apache.camel:camel-main:3.8.0
//DEPS org.apache.camel:camel-stream:3.8.0
//DEPS org.apache.camel:camel-netty:3.9.0-SNAPSHOT
//DEPS org.slf4j:slf4j-nop:1.7.25

/open camel/camel-imports

import org.apache.camel.component.netty.*;

Main main = new Main();
main.configure().addRoutesBuilder(new RouteBuilder() {
    public void configure() throws Exception {
        EndpointUriFactory factory = getContext().adapt(ExtendedCamelContext.class).getEndpointUriFactory("netty");
        Map<String, Object> config = Map.of(
            "protocol", "tcp",
            "host", "localhost",
            "port", 9999
        );
        String uri = factory.buildUri("netty", config, false);
        System.out.println("uri = " + uri);
        from(uri).to("stream:out");
        //from("netty:tcp://localhost:9999").to("stream:out");
    }
});
main.run();
