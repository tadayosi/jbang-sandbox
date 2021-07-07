///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.apache.camel:camel-bom:3.11.0@pom
//DEPS org.apache.camel:camel-core
//DEPS org.apache.camel:camel-main
//DEPS org.apache.camel:camel-stream
//DEPS org.apache.camel:camel-http
//DEPS org.apache.camel:camel-jsonpath
///DEPS org.slf4j:slf4j-simple:1.7.31
//DEPS org.slf4j:slf4j-nop:1.7.31

/open camel/camel-imports

import static java.lang.System.*;

setProperty("org.slf4j.simpleLogger.logFile", "System.out");
setProperty("camel.main.durationMaxMessages", "1");

var location = "Tokyo";
var message = "Today's weather in " + location + ": ";

Main main = new Main();
main.configure().addRoutesBuilder(new RouteBuilder() {
    public void configure() throws Exception {
        from("timer:json-to-file?repeatCount=1")
            .toF("http://wttr.in/%s?format=j1", location)
            .transform().jsonpath("$.current_condition[0].weatherDesc[0].value")
            .choice()
                .when(or(body().contains("Sunny"), body().contains("Clear")))
                    .transform().constant(message + "☀")
                    .endChoice()
                .when(or(body().contains("Cloudy"), body().contains("cloudy"), body().contains("Overcast")))
                    .transform().constant(message + "☁")
                    .endChoice()
                .when(body().contains("rain"))
                    .transform().constant(message + "☂")
                    .endChoice()
                .otherwise()
                    .transform().simple(message + "${body}")
            .end()
            .to("stream:out");
    }
});
main.run();
