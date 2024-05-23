///usr/bin/env jbang --javaagent=org.jolokia:jolokia-agent-jvm:2.0.2:javaagent=discoveryEnabled=true "$0" "$@" ; exit $?
//DEPS io.quarkus.platform:quarkus-bom:3.10.1@pom
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

    static final String objectName = "com.github.tadayosi.samples.jbang:name=Sample";
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
        String hello(String message);
        long longValue(long value);
        BigInteger bigIntegerValue(BigInteger value);
    }

    public class Sample implements SampleMBean {
        public String hello(String message) {
            out.println("hello: " + message);
            return "Hello World";
        }

        public long longValue(long value) {
            out.println("long: " + value);
            return Long.MAX_VALUE;
        }

        public BigInteger bigIntegerValue(BigInteger value) {
            out.println("BigInteger: " + value);
            return BigInteger.valueOf(Long.MAX_VALUE).pow(2);
        }
    }
}
