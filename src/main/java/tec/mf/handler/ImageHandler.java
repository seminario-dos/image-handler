package tec.mf.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import tec.mf.handler.io.ImageHandlerResponseWriter;
import tec.mf.handler.io.ImageRequest;
import tec.mf.handler.io.InputEventParser;
import tec.mf.handler.service.ImageService;

import java.io.*;


public class ImageHandler implements RequestStreamHandler {

    private static final AppConfig APP_CONFIG;
    private final AppConfig appConfig;

    static {
        APP_CONFIG = AppConfig.getInstance();
    }

    public ImageHandler() {
        this(APP_CONFIG);
    }

    public ImageHandler(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        LambdaLogger logger = context.getLogger();
        logger.log("Inside Image Handler ");
        ImageRequest imageRequest = this.inputEventParser().processInputEvent(inputStream, logger);
        InputStream imageResized = this.imageService().getImageFrom(imageRequest);
        this.imageHandlerResponseWriter().writeResponse(imageResized, outputStream, imageRequest);
    }

    private InputEventParser inputEventParser() {
        return this.appConfig.getInputEventParser();
    }

    private ImageService imageService() {
        return this.appConfig.getImageService();
    }

    private ImageHandlerResponseWriter imageHandlerResponseWriter() {
        return this.appConfig.getImageHandlerResponseWriter();
    }

//    private static String getStringFromInputStream(InputStream is) {
//
//        BufferedReader br = null;
//        StringBuilder sb = new StringBuilder();
//
//        String line;
//        try {
//
//            br = new BufferedReader(new InputStreamReader(is));
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return sb.toString();
//
//    }
}
