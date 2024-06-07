///usr/bin/env jbang --javaagent=org.jolokia:jolokia-agent-jvm:2.0.3:javaagent=discoveryEnabled=true,serializeLong=number "$0" "$@" ; exit $?
//DEPS io.quarkus.platform:quarkus-bom:3.11.0@pom
//DEPS io.quarkus:quarkus-rest
//JAVAC_OPTIONS -parameters
//JAVA_OPTIONS -Djava.util.logging.manager=org.jboss.logmanager.LogManager

import static java.lang.System.*;
import java.lang.management.*;
import java.math.*;

import javax.management.*;

import io.quarkus.runtime.*;
import jakarta.enterprise.context.*;
import jakarta.enterprise.event.*;
import jakarta.ws.rs.*;

@Path("/")
@ApplicationScoped
public class mbean {

    static final String objectName = "samples.jbang:name=Sample";
    final Sample sampleMBean = new Sample();

    @GET
    public String hello() {
        var message = "Hello World";
        out.println(message);
        return message;
    }

    void onStart(@Observes StartupEvent event) {
        out.println("Register a mbean: " + objectName);
        try {
            var mbeanServer = ManagementFactory.getPlatformMBeanServer();
            mbeanServer.registerMBean(sampleMBean, new ObjectName(objectName));
        } catch (Exception e) {
            err.println(e.getMessage());
        }
    }

    void onStop(@Observes ShutdownEvent event) {
        out.println("Deregister a mbean: " + objectName);
        try {
            var mbeanServer = ManagementFactory.getPlatformMBeanServer();
            mbeanServer.unregisterMBean(new ObjectName(objectName));
        } catch (Exception e) {
            err.println(e.getMessage());
        }
    }

    public interface SampleMBean {
        String getMessage();
        long getLong();
        BigInteger getBigInteger();
        String hello(String message);
        long longValue(long value);
        BigInteger bigIntegerValue(BigInteger value);
    }

    public class Sample implements SampleMBean {
        String s = "";
        long l = 0L;
        BigInteger bi = BigInteger.ZERO;

        public String getMessage() { return s; }
        public long getLong() { return l; }
        public BigInteger getBigInteger() { return bi; }

        public String hello(String message) {
            s = message;
            out.println("hello: " + message);
            return "Hello World";
        }

        public long longValue(long value) {
            l = value;
            out.println("long: " + value);
            return value;
        }

        public BigInteger bigIntegerValue(BigInteger value) {
            bi = value;
            out.println("BigInteger: " + value);
            return value;
        }
    }
}
