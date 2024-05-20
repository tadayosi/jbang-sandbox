///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS io.quarkus.platform:quarkus-bom:3.10.1@pom
//DEPS io.quarkus:quarkus-rest
//JAVAC_OPTIONS -parameters
//JAVA_OPTIONS -Djava.util.logging.manager=org.jboss.logmanager.LogManager

import static java.lang.System.*;

import jakarta.enterprise.context.*;
import jakarta.ws.rs.*;

@Path("/")
@ApplicationScoped
public class rest {

    @GET
    public String hello() {
        var message = "Hello World";
        out.println(message);
        return message;
    }
}
