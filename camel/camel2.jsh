///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.apache.camel:camel-bom:3.11.1@pom
//DEPS org.apache.camel:camel-core
//DEPS org.apache.camel:camel-main
//DEPS org.apache.camel:camel-stream
//DEPS org.slf4j:slf4j-nop:1.7.31

/open camel/camel-imports

Main main = new Main();
main.configure().addRoutesBuilder(new RouteBuilder() {
    public void configure() throws Exception {
        from("timer:hello?period=3000")
            .setBody().constant("Hello Camel!")
            .to("stream:out");
    }
});
main.run();
