package tec.mf.handler.io;

import com.amazonaws.services.lambda.runtime.Context;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.*;

import static java.util.Optional.*;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

public class ImageRequestParserTest {

    @Test
    public void some() throws IOException {
        InputStream inputStream = new FileInputStream(new File("src/test/resources/image-request-event.json"));

        ImageRequestParser parser = new ImageRequestParser();
        ImageRequest imageRequest = parser.processInputEvent(inputStream);

        assertThat(imageRequest.getFilename()).isEqualTo("51ca22dd0cf293ac67bb394a-295xh.jpg");
        assertThat(imageRequest.getWidth()).isEqualTo(100);
        assertThat(imageRequest.getHeight()).isEqualTo(100);


//        processInput(inputStream);

//        byte[] buffer = new byte[inputStream.available()];
//        inputStream.read(buffer);

//        File targetFile = new File("src/test/resources/targetFile.tmp");
//        OutputStream outStream = new FileOutputStream(targetFile);
//        outStream.write(buffer);

    }


//    public void processInput(InputStream inputStream) {
//
//        JSONParser parser = new JSONParser();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//        try{
//            JSONObject event = (JSONObject) parser.parse(reader);
//
//            // By QueryString
//            String name = ofNullable((JSONObject) event.get("queryStringParameters"))
//                    .map(qps ->  (String)qps.get("name"))
//                    .orElse("");
//
//            System.out.println("Name: " + name);
//
//            String day = ofNullable((JSONObject) event.get("headers"))
//                    .map(hps -> (String) hps.get("day"))
//                    .orElse("");
//
//            System.out.println("Day: " + day);
//
//            String time = "";
//            if (event.get("body") != null) {
//                JSONObject body = (JSONObject)parser.parse((String)event.get("body"));
//                if ( body.get("time") != null) {
//                    time = (String)body.get("time");
//                }
//            }
//
//            System.out.println("Time: " + time);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
