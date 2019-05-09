package tec.mf.handler.io;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.lang.Integer.parseInt;
import static java.util.Optional.ofNullable;

public class ImageRequestParser implements InputEventParser{
    private static final String PATH_PARAMETERS_ELEMENT_NAME = "pathParameters";
    private static final String PATH_PARAMETERS_IMAGE_ELEMENT_NAME = "image";
    private static final String QUERY_STRING_ELEMENT_NAME = "queryStringParameters";
    private static final String QUERY_STRING_WIDTH_ELEMENT_NAME = "width";
    private static final String QUERY_STRING_HEIGHT_ELEMENT_NAME = "height";

    @Override
    public ImageRequest processInputEvent(InputStream inputStream) {


        try{

            JSONParser parser = new JSONParser();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            JSONObject event = (JSONObject) parser.parse(reader);
//            logger.log(event.toJSONString());

            String filename = ofNullable((JSONObject) event.get(PATH_PARAMETERS_ELEMENT_NAME))
                    .map(p -> (String) p.get(PATH_PARAMETERS_IMAGE_ELEMENT_NAME))
                    .orElse("");

            // By QueryString
            int width = ofNullable((JSONObject) event.get(QUERY_STRING_ELEMENT_NAME))
                    .map(qs -> parseInt((String) qs.get(QUERY_STRING_WIDTH_ELEMENT_NAME)))
                    .orElse(0);

            int height = ofNullable((JSONObject) event.get(QUERY_STRING_ELEMENT_NAME))
                    .map(qs ->  parseInt((String) qs.get(QUERY_STRING_HEIGHT_ELEMENT_NAME)))
                    .orElse(0);

            System.out.println("Filename: " + filename);
            System.out.println("Width: " + width);
            System.out.println("Height: " + height);


//            // By Headers
//            String day = ofNullable((JSONObject) event.get("headers"))
//                    .map(hps -> (String) hps.get("Day"))
//                    .orElse("");
//
//            System.out.println("Day: " + day);
//
//            // By Body
//            String time = "";
//            if (event.get("body") != null) {
//                JSONObject body = (JSONObject)parser.parse((String)event.get("body"));
//                if ( body.get("time") != null) {
//                    time = (String)body.get("time");
//                }
//            }
//            System.out.println("Time: " + time);

            return new ImageRequest(filename, width, height);

        } catch (Exception e) {
            //TODO: change this
            e.printStackTrace();
        }

        return new ImageRequest("", 0, 0);
    }

}
