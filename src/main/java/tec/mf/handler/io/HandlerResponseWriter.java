package tec.mf.handler.io;

import org.json.simple.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Base64;

public class HandlerResponseWriter implements ImageHandlerResponseWriter {

    @Override
    public void writeResponse(InputStream inputStream, OutputStream outputStream, ImageRequest imageRequest) {
        try {
            JSONObject responseJson = new JSONObject();
            JSONObject responseBody = new JSONObject();
            responseBody.put("filename", imageRequest.getFilename());
            responseBody.put("width", imageRequest.getWidth());
            responseBody.put("height", imageRequest.getHeight());

            JSONObject headerJson = new JSONObject();
            headerJson.put("Content-Type", "image/jpeg");
            headerJson.put("x-resize-header", "my custom header value");

            responseJson.put("isBase64Encoded", true);
            responseJson.put("statusCode", 200);
            responseJson.put("headers", headerJson);
            responseJson.put("body", toBase64(inputStream));

//        logger.log(responseJson.toJSONString());
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
            writer.write(responseJson.toJSONString());
            writer.close();
        } catch (Exception e) {

        }
    }

    private static String toBase64(InputStream inputStream) {
//        String imageResizedBase64 = "";
        try{
            byte imageData[] = new byte[inputStream.available()];
            inputStream.read(imageData);
            return  Base64.getEncoder().encodeToString(imageData);
        } catch (Exception e) {
            //TODO: change this
            e.printStackTrace();
        }
        return "";
    }
}
