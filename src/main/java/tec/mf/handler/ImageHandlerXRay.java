package tec.mf.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
//import com.amazonaws.xray.AWSXRay;
//import com.amazonaws.xray.entities.Subsegment;
import tec.mf.handler.io.ImageHandlerResponseWriter;
import tec.mf.handler.io.ImageRequest;
import tec.mf.handler.io.InputEventParser;
import tec.mf.handler.service.ImageService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Function;

public class ImageHandlerXRay implements RequestStreamHandler {

    private static final AppConfig APP_CONFIG;
    private final AppConfig appConfig;

    static {
        APP_CONFIG = AppConfig.getInstance();
    }

    public ImageHandlerXRay() {
        this(APP_CONFIG);
    }

    public ImageHandlerXRay(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
//        LambdaLogger logger = context.getLogger();
//        logger.log("Inside Image Handler ");
//
//        ImageRequest imageRequest = AWSXRay.createSubsegment("input event", new Function<Subsegment, ImageRequest>() {
//            @Override
//            public ImageRequest apply(Subsegment subsegment) {
//                return inputEventParser().processInputEvent(inputStream, logger);
//            }
//        });
//
//        InputStream imageResized = this.imageService().getImageFrom(imageRequest);
//        this.imageHandlerResponseWriter().writeResponse(imageResized, outputStream, imageRequest);
    }

//    private InputEventParser inputEventParser() {
//        return this.appConfig.getInputEventParser();
//    }
//
//    private ImageService imageService() {
//        return this.appConfig.getImageService();
//    }
//
//    private ImageHandlerResponseWriter imageHandlerResponseWriter() {
//        return this.appConfig.getImageHandlerResponseWriter();
//    }
}
