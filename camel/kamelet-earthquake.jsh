///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.apache.camel:camel-bom:3.14.0@pom
//DEPS org.apache.camel:camel-core
//DEPS org.apache.camel:camel-main
//DEPS org.apache.camel:camel-kamelet-main
//DEPS org.apache.camel:camel-stream
//DEPS org.apache.camel:camel-caffeine
//DEPS org.apache.camel:camel-http
//DEPS org.apache.camel:camel-jackson
//DEPS org.apache.camel:camel-jsonpath
///DEPS org.slf4j:slf4j-simple:1.7.31
//DEPS org.slf4j:slf4j-nop:1.7.31

/open camel/camel-imports

import static java.lang.System.*;

setProperty("org.slf4j.simpleLogger.logFile", "System.out");
setProperty("camel.main.durationMaxMessages", "1");

Main main = new Main();
main.addInitialProperty("camel.component.kamelet.location", "github:apache:camel-kamelets");
main.addInitialProperty("camel.main.lightweight", "true");

main.configure().addRoutesBuilder(new RouteBuilder() {
    public void configure() throws Exception {
        from("kamelet:earthquake-source")
            .unmarshal().json()
            .transform().simple("Earthquake with magnitude ${body[properties][mag]} at ${body[properties][place]}")
            .to("stream:out");
    }
});
main.run();
