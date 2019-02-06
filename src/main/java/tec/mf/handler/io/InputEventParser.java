package tec.mf.handler.io;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.util.Optional.ofNullable;

public class InputEventParser {


    public Input processEvent(InputStream inputStream, LambdaLogger logger) {

        JSONParser parser = new JSONParser();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try{
            JSONObject event = (JSONObject) parser.parse(reader);

            logger.log(event.toJSONString());

            // By QueryString
            String width = ofNullable((JSONObject) event.get("queryStringParameters"))
                    .map(qps ->  (String)qps.get("width"))
                    .orElse("");

            System.out.println("Width: " + width);

            // By Headers
            String day = ofNullable((JSONObject) event.get("headers"))
                    .map(hps -> (String) hps.get("Day"))
                    .orElse("");

            System.out.println("Day: " + day);

            // By Body
            String time = "";
            if (event.get("body") != null) {
                JSONObject body = (JSONObject)parser.parse((String)event.get("body"));
                if ( body.get("time") != null) {
                    time = (String)body.get("time");
                }
            }

            System.out.println("Time: " + time);

            return new Input(width, day, time);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Input();
    }

}
