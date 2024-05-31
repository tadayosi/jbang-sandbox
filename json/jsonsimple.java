///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS com.googlecode.json-simple:json-simple:1.1.1

import static java.lang.System.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class jsonsimple {

    @SuppressWarnings("unchecked")
    public static void main(String... args) throws Exception {
        // Object -> JSON
        var obj = new JSONObject();
        obj.put("boolean", true);
        obj.put("string", "hello");
        obj.put("long", 900719925474099123L);
        obj.put("bigint", "900719925474099123n");
        out.println("Object -> JSON:");
        out.println(obj.toJSONString());

        // JSON -> Object
        // json-simple cannot parse JS BigInt as-is
        var json = """
                {
                  "boolean": true,
                  "string": "hello",
                  "bigint": "900719925474099123n",
                  "long": 900719925474099123
                }
                """;
        var parser = new JSONParser();
        var obj2 = parser.parse(json);
        out.println("JSON -> Object:");
        out.println(obj2);
    }
}
