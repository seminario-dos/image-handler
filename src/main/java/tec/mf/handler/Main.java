package tec.mf.handler;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import tec.mf.handler.io.ImageRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetAddress;

public class Main {

    private static final AppConfig APP_CONFIG;
    private static final IMonitoringController MONITORING_CONTROLLER;

    static {
        APP_CONFIG = AppConfig.getInstance();
        MONITORING_CONTROLLER = MonitoringController.getInstance();
    }

    public static void main(String... args) {

        try {
            final long tin = MONITORING_CONTROLLER.getTimeSource().getTime();
            handleRequest();
            final long tout = MONITORING_CONTROLLER.getTimeSource().getTime();
            final OperationExecutionRecord e = new OperationExecutionRecord("public void "+ Main.class.getName() +".handleRequest()",
                    OperationExecutionRecord.NO_SESSION_ID,
                    OperationExecutionRecord.NO_TRACE_ID,
                    tin, tout,
                    InetAddress.getLocalHost().getHostName(),
                    0,
                    0);
            MONITORING_CONTROLLER.newMonitoringRecord(e);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void handleRequest(){

        try {
            InputStream inputStream = new FileInputStream(new File("src/test/resources/image-request-event.json"));
            FileOutputStream outputStream = new FileOutputStream(new File("a-response.json"));


            final long tin1 = MONITORING_CONTROLLER.getTimeSource().getTime();
            ImageRequest imageRequest = APP_CONFIG.getInputEventParser().processInputEvent(inputStream, null);
            final long tout1 = MONITORING_CONTROLLER.getTimeSource().getTime();
            final OperationExecutionRecord e1 = new OperationExecutionRecord("public ImageRequest "+ APP_CONFIG.getInputEventParser().getClass().getName() +".processInputEvent(InputStream)",
                    OperationExecutionRecord.NO_SESSION_ID,
                    OperationExecutionRecord.NO_TRACE_ID,
                    tin1, tout1,
                    InetAddress.getLocalHost().getHostName(),
                    1,
                    1);

            final long tin2 = MONITORING_CONTROLLER.getTimeSource().getTime();
            InputStream imageStream = APP_CONFIG.getImageService().getImageFrom(imageRequest);
            final long tout2 = MONITORING_CONTROLLER.getTimeSource().getTime();
            final OperationExecutionRecord e2 = new OperationExecutionRecord("public InputStream "+ APP_CONFIG.getImageService().getClass().getName() +".getImageFrom(InputStream)",
                    OperationExecutionRecord.NO_SESSION_ID,
                    OperationExecutionRecord.NO_TRACE_ID,
                    tin2, tout2,
                    InetAddress.getLocalHost().getHostName(),
                    2,
                    1);


            final long tin3 = MONITORING_CONTROLLER.getTimeSource().getTime();
            APP_CONFIG.getImageHandlerResponseWriter().writeResponse(imageStream, outputStream, imageRequest);
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
}
