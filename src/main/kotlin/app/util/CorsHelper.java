package app.util;

import java.util.HashMap;
import spark.Filter;
import spark.Spark;

public final class CorsHelper {

    private static final HashMap<String, String> corsHeaders = new HashMap<String, String>();

    static {
        corsHeaders.put("Access-Control-Allow-Method", "GET,PUT,POST,DELETE,OPTIONS");
        corsHeaders.put("Access-Control-Allow-Origin", "*");
        corsHeaders.put("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
        corsHeaders.put("Access-Control-Allow-Credentials", "true");
    }

    public final static void apply() {
        Filter filter = (request, response) -> corsHeaders.forEach((key, value) -> {
            response.header(key, value);
        });
        Spark.after(filter);
    }
}