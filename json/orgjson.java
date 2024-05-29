///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.json:json:20240303

import static java.lang.System.*;
import org.json.*;

public class orgjson {

    public static void main(String... args) {
        var json = new JSONObject();
        json.put("boolean", true);
        json.put("string", "hello");
        json.put("long", 900719925474099123L);
        json.put("bigint", "900719925474099123n");
        out.println(json.toString());
    }
}
