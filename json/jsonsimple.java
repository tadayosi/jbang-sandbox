///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS com.googlecode.json-simple:json-simple:1.1.1

import static java.lang.System.*;
import org.json.simple.*;

public class jsonsimple {

    @SuppressWarnings("unchecked")
    public static void main(String... args) {
        var json = new JSONObject();
        json.put("boolean", true);
        json.put("string", "hello");
        json.put("long", 900719925474099123L);
        json.put("bigint", "900719925474099123n");
        out.println(json.toJSONString());
    }
}
