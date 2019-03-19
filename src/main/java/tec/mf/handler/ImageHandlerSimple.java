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
import java.net.InetAddress;


public class ImageHandlerSimple implements RequestStreamHandler {

    private static final AppConfig APP_CONFIG;
    private static final IMonitoringController MONITORING_CONTROLLER;

    static {
        APP_CONFIG = AppConfig.getInstance();
        MONITORING_CONTROLLER = MonitoringController.getInstance();
    }

    private final AppConfig appConfig;

    public ImageHandlerSimple() {
        this(APP_CONFIG);
    }

    public ImageHandlerSimple(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        LambdaLogger logger = context.getLogger();
        logger.log("Inside Image Handler ");

        long tin = System.currentTimeMillis();
        handleRequestInternal(inputStream, outputStream, context);
        long tout = System.currentTimeMillis();
        final OperationExecutionRecord e = new OperationExecutionRecord("public void "+ this.getClass().getName()+".handleRequest(InputStream, OutputStream, Context)",
                OperationExecutionRecord.NO_SESSION_ID,
                OperationExecutionRecord.NO_TRACE_ID,
                tout, tout - tin,
                InetAddress.getLocalHost().getHostName(),
                0,
                0);
        MONITORING_CONTROLLER.newMonitoringRecord(e);
    }

    private void handleRequestInternal(InputStream inputStream, OutputStream outputStream, Context context) {

        try {

            final long tin1 = MONITORING_CONTROLLER.getTimeSource().getTime();
            ImageRequest imageRequest = this.inputEventParser().processInputEvent(inputStream, null);
            final long tout1 = MONITORING_CONTROLLER.getTimeSource().getTime();
            final OperationExecutionRecord e1 = new OperationExecutionRecord("public ImageRequest "+ APP_CONFIG.getInputEventParser().getClass().getName() +".processInputEvent(InputStream)",
                    OperationExecutionRecord.NO_SESSION_ID,
                    OperationExecutionRecord.NO_TRACE_ID,
                    tin1, tout1,
                    InetAddress.getLocalHost().getHostName(),
                    1,
                    1);

            final long tin2 = MONITORING_CONTROLLER.getTimeSource().getTime();
            InputStream imageStream = this.imageService().getImageFrom(imageRequest);
            final long tout2 = MONITORING_CONTROLLER.getTimeSource().getTime();
            final OperationExecutionRecord e2 = new OperationExecutionRecord("public InputStream "+ APP_CONFIG.getImageService().getClass().getName() +".getImageFrom(ImageRequest)",
                    OperationExecutionRecord.NO_SESSION_ID,
                    OperationExecutionRecord.NO_TRACE_ID,
                    tin2, tout2,
                    InetAddress.getLocalHost().getHostName(),
                    2,
                    1);


            final long tin3 = MONITORING_CONTROLLER.getTimeSource().getTime();
            this.imageHandlerResponseWriter().writeResponse(imageStream, outputStream, imageRequest);
            final long tout3 = MONITORING_CONTROLLER.getTimeSource().getTime();
            final OperationExecutionRecord e3 = new OperationExecutionRecord("public void "+ APP_CONFIG.getImageHandlerResponseWriter().getClass().getName() +".writeResponse(InputStream, OutputStream, ImageRequest)",
                    OperationExecutionRecord.NO_SESSION_ID,
                    OperationExecutionRecord.NO_TRACE_ID,
                    tin3, tout3,
                    InetAddress.getLocalHost().getHostName(),
                    5,
                    1);

            APP_CONFIG.getImageHandlerResponseWriter().writeResponse(imageStream, outputStream, imageRequest);

            MONITORING_CONTROLLER.newMonitoringRecord(e1);
            MONITORING_CONTROLLER.newMonitoringRecord(e2);
            MONITORING_CONTROLLER.newMonitoringRecord(e3);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
