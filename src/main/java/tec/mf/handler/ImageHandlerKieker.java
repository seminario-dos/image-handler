package tec.mf.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import tec.mf.handler.io.ImageHandlerResponseWriter;
import tec.mf.handler.io.ImageRequest;
import tec.mf.handler.io.InputEventParser;
import tec.mf.handler.service.ImageService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ImageHandlerKieker implements RequestStreamHandler {

    private static final AppConfig APP_CONFIG;
    private static final IMonitoringController MONITORING_CONTROLLER;

    static {
        APP_CONFIG = AppConfig.getInstance();
        MONITORING_CONTROLLER = MonitoringController.getInstance();
    }

    private final AppConfig appConfig;

    public ImageHandlerKieker() {
        this(APP_CONFIG);
    }

    public ImageHandlerKieker(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        LambdaLogger logger = context.getLogger();
        logger.log("Inside Image Handler ");
        ImageRequest imageRequest = this.inputEventParser().processInputEvent(inputStream, logger);

        final long tin = MONITORING_CONTROLLER.getTimeSource().getTime();
        InputStream imageResized = this.imageService().getImageFrom(imageRequest);
        final long tout = MONITORING_CONTROLLER.getTimeSource().getTime();
        final OperationExecutionRecord e = new OperationExecutionRecord("public InputStream tec.mf.handler.service.ImageService.getImageFrom(InputStream)",
                OperationExecutionRecord.NO_SESSION_ID,
                OperationExecutionRecord.NO_TRACE_ID,
                tin, tout,
                OperationExecutionRecord.NO_HOSTNAME,
                OperationExecutionRecord.NO_EOI_ESS,
                OperationExecutionRecord.NO_EOI_ESS);
        MONITORING_CONTROLLER.newMonitoringRecord(e);


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

}
