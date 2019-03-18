package tec.mf.handler;

import tec.mf.handler.io.ImageRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Main {

    private static final AppConfig APP_CONFIG;

    static {
        APP_CONFIG = AppConfig.getInstance();
    }

    public static void main(String... args) {

        handleRequest();
    }

    private static void handleRequest(){

        try {
            InputStream inputStream = new FileInputStream(new File("src/test/resources/image-request-event.json"));
            FileOutputStream outputStream = new FileOutputStream(new File("a-response.json"));

            ImageRequest imageRequest = APP_CONFIG.getInputEventParser().processInputEvent(inputStream);

            InputStream imageStream = APP_CONFIG.getImageService().getImageFrom(imageRequest);

            APP_CONFIG.getImageHandlerResponseWriter().writeResponse(imageStream, outputStream, imageRequest);

            APP_CONFIG.getImageHandlerResponseWriter().writeResponse(imageStream, outputStream, imageRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
