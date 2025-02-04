///usr/bin/env jbang "$0" "$@" ; exit $?
// //DEPS <dependency1> <dependency2>

import java.util.stream.*;
import java.net.*;

import static java.lang.System.*;

public class ss {

    public static void main(String... args) {
        IntStream.range(0, 100).forEach(i -> {
            try (ServerSocket ss = new ServerSocket(9999)) {
            } catch (Exception e) {
                out.println(e.getMessage());
            }
        });
    }
}
