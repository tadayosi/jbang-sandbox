///usr/bin/env jbang --javaagent=org.jolokia.mcp:jolokia-mcp-agent-jvm:0.4.1:javaagent "$0" "$@" ; exit $?

//JAVA 17
//DEPS io.quarkus.platform:quarkus-bom:3.22.3@pom
//DEPS io.quarkus:quarkus-rest

import static java.lang.System.err;
import static java.lang.System.out;

import java.lang.management.ManagementFactory;
import java.math.BigInteger;

import javax.management.ObjectName;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/")
@ApplicationScoped
public class mcp {

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

        String html();

        long longValue(long value);

        BigInteger bigIntegerValue(BigInteger value);
    }

    public class Sample implements SampleMBean {
        public String hello(String message) {
            out.println("hello: " + message);
            return "Hello World";
        }

        public String html() {
            out.println("html");
            return """
                    <!DOCTYPE html>
                    <html>
                    <body>
                    <table class="pf-v5-c-table">
                      <thead class="pf-v5-c-table__thead">
                        <tr class="pf-v5-c-table__tr">
                          <th class="pf-v5-c-table__th">key1</td>
                          <th class="pf-v5-c-table__th">key2</td>
                        </tr>
                      </thead>
                      <tbody class="pf-v5-c-table__tbody">
                        <tr class="pf-v5-c-table__tr">
                          <td class="pf-v5-c-table__td">value1</td>
                          <td class="pf-v5-c-table__td">value2</td>
                        </tr>
                        <tr class="pf-v5-c-table__tr">
                          <td class="pf-v5-c-table__td">value3</td>
                          <td class="pf-v5-c-table__td">value4</td>
                        </tr>
                      </tbody>
                    </table>
                    </body>
                    </html>
                    """;
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
