///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.json:json:20240303

import static java.lang.System.*;
import org.json.*;

public class orgjson {

    public static void main(String... args) {
        // Object -> JSON
        var obj = new JSONObject();
        obj.put("boolean", true);
        obj.put("string", "hello");
        obj.put("long", 900719925474099123L);
        obj.put("bigint", "900719925474099123n");
        out.println("Object -> JSON:");
        out.println(obj.toString());

        // JSON -> Object
        var json = """
                {
                  "boolean": true,
                  "string": "hello",
                  "bigint": 900719925474099123n,
                  "long": 900719925474099123
                }
                """;
        var obj2 = new JSONObject(json);
        out.println("JSON -> Object:");
        out.println(obj2);
    }
}
