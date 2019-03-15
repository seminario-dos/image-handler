package tec.mf.handler.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

import java.io.InputStream;
import java.net.InetAddress;

public class S3Dao {

    private static final IMonitoringController MONITORING_CONTROLLER = MonitoringController.getInstance();

    private final AmazonS3 amazonS3;
    private final String bucket;

    public S3Dao(String bucket) {
        this(AmazonS3ClientBuilder.defaultClient(), bucket);
    }

    public S3Dao(AmazonS3 amazonS3, String bucket) {
        this.amazonS3 = amazonS3;
        this.bucket = bucket;
    }

    public InputStream getImage(String name) {
//        final long tin = MONITORING_CONTROLLER.getTimeSource().getTime();
        final long tin = System.currentTimeMillis();
        S3Object s3Object = this.amazonS3.getObject(bucket, "originals/" + name);
        final long tout = System.currentTimeMillis();
//        final long tout = MONITORING_CONTROLLER.getTimeSource().getTime();
        System.out.println("4,3 Diff: " + (tout - tin));
        try {
            final OperationExecutionRecord e = new OperationExecutionRecord(
                    "public S3Object " + this.amazonS3.getClass().getName() + ".getObject(String, String)",
                    OperationExecutionRecord.NO_SESSION_ID,
                    OperationExecutionRecord.NO_TRACE_ID,
                    tin, tout,
                    InetAddress.getLocalHost().getHostName(),
                    4,
                    3);
            MONITORING_CONTROLLER.newMonitoringRecord(e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return s3Object.getObjectContent();
    }

    public static S3Dao newInstance() {
        AWSCredentials credentials = null;
        try{
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(e.getMessage(), e);
        }
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion("us-west-2")
                .build();
        return new S3Dao(amazonS3, "images-repo.dev");
    }
}
