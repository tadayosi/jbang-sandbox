///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.apache.camel:camel-bom:3.14.0@pom
//DEPS org.apache.camel:camel-core
//DEPS org.apache.camel:camel-main
//DEPS org.apache.camel:camel-stream
//DEPS org.slf4j:slf4j-nop:1.7.31

// Camel imports
import org.apache.camel.*;
import org.apache.camel.builder.*;
import org.apache.camel.main.*;
import org.apache.camel.spi.*;
import static org.apache.camel.builder.PredicateBuilder.*;

import static java.lang.System.*;

class camel {

    public static void main(String... args) throws Exception {
        out.println("Running Camel route...");

        Main main = new Main();
        main.configure().addRoutesBuilder(new RouteBuilder() {
            public void configure() throws Exception {
                from("timer:hello?period=3000")
                    .setBody().constant("Hello Camel!")
                    .to("stream:out");
            }
        });
        main.run();
    }
}
