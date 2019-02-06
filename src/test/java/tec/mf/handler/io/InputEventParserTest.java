package tec.mf.handler.io;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Optional;

import static java.util.Optional.*;

public class InputEventParserTest {

    @Test
    public void some() throws IOException {
        InputStream inputStream =  new FileInputStream(new File("src/test/resources/input-event.json"));
        processInput(inputStream);

//        byte[] buffer = new byte[inputStream.available()];
//        inputStream.read(buffer);

//        File targetFile = new File("src/test/resources/targetFile.tmp");
//        OutputStream outStream = new FileOutputStream(targetFile);
//        outStream.write(buffer);


    }


    public void processInput(InputStream inputStream) {

        JSONParser parser = new JSONParser();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try{
            JSONObject event = (JSONObject) parser.parse(reader);

            // By QueryString
            String name = ofNullable((JSONObject) event.get("queryStringParameters"))
                    .map(qps ->  (String)qps.get("name"))
                    .orElse("");

            System.out.println("Name: " + name);

            String day = ofNullable((JSONObject) event.get("headers"))
                    .map(hps -> (String) hps.get("day"))
                    .orElse("");

            System.out.println("Day: " + day);

            String time = "";
            if (event.get("body") != null) {
                JSONObject body = (JSONObject)parser.parse((String)event.get("body"));
                if ( body.get("time") != null) {
                    time = (String)body.get("time");
                }
            }

//            String time = ofNullable((JSONObject)parser.parse((String)event.get("body")))
//                    .map(b -> (String) b.get("time")
//            ).orElse("");

            System.out.println("Time: " + time);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
