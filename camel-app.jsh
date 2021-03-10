///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.apache.camel:camel-bom:3.8.0@pom
//DEPS org.apache.camel:camel-core-engine
//DEPS org.apache.camel:camel-main
//DEPS org.apache.camel:camel-stream
//DEPS org.apache.camel:camel-netty
//DEPS org.slf4j:slf4j-nop:1.7.30

/open camel-imports

Main main = new Main();
main.configure().addRoutesBuilder(new RouteBuilder() {
    @Override
    public void configure() throws Exception {
        from("netty:tcp://localhost:9999")
            .to("stream:out")
            .transform().constant("Hello from Netty!\n");
    }
});
main.run();
