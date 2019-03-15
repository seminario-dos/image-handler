package tec.mf.handler.service;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import tec.mf.handler.s3.S3Dao;

import java.io.InputStream;
import java.net.InetAddress;

public class S3ImageService extends BaseImageService {

    private static final IMonitoringController MONITORING_CONTROLLER;

    static {
        MONITORING_CONTROLLER = MonitoringController.getInstance();
    }

    private final S3Dao dao;

    public S3ImageService(S3Dao dao) {
        this.dao = dao;
    }

    @Override
    public InputStream getImage(String imageName) {

//        final long tin = MONITORING_CONTROLLER.getTimeSource().getTime();
        final long tin = System.currentTimeMillis();
        InputStream imageStream = this.dao.getImage(imageName);
        final long tout = System.currentTimeMillis();
//        final long tout = MONITORING_CONTROLLER.getTimeSource().getTime();
        try {
            final OperationExecutionRecord e = new OperationExecutionRecord("public InputStream " + this.dao.getClass().getName() + ".getImage(InputStream)",
                    OperationExecutionRecord.NO_SESSION_ID,
                    OperationExecutionRecord.NO_TRACE_ID,
                    tin, tout,
                    InetAddress.getLocalHost().getHostName(),
                    3,
                    2);
            MONITORING_CONTROLLER.newMonitoringRecord(e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageStream;
    }
}
