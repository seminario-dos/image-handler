package tec.mf.handler;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import tec.mf.handler.io.HandlerResponseWriter;
import tec.mf.handler.io.ImageHandlerResponseWriter;
import tec.mf.handler.io.ImageRequestParser;
import tec.mf.handler.io.InputEventParser;
import tec.mf.handler.s3.S3Dao;
import tec.mf.handler.service.ImageService;
import tec.mf.handler.service.S3ImageService;

import static java.lang.System.getenv;
import static java.util.Optional.ofNullable;

public class AppConfig {

    private static final AppConfig INSTANCE = new AppConfig();
    private static final String ORIGINAL_IMAGES_BUCKET = "IMAGES_BUCKET";
    private static final String ACTIVE_PROFILE = "PROFILE";
    private static final String PROD_PROFILE = "PROD";

    private final InputEventParser inputEventParser;
    private final ImageHandlerResponseWriter imageHandlerResponseWriter;
    private final ImageService imageService;
    private final S3Dao s3Dao;
    private final String originalImagesBucket;

    private AppConfig() {
        System.out.println("PROFILE: " + getenv(ACTIVE_PROFILE));

        this.inputEventParser = new ImageRequestParser();
        this.imageHandlerResponseWriter = new HandlerResponseWriter();
        this.originalImagesBucket = getenv(ORIGINAL_IMAGES_BUCKET);
        this.s3Dao = s3Dao();
        this.imageService = new S3ImageService(this.s3Dao);
    }

    public static AppConfig getInstance() {
        return INSTANCE;
    }

    public InputEventParser getInputEventParser() {
        return this.inputEventParser;
    }

    public ImageHandlerResponseWriter getImageHandlerResponseWriter() {
        return this.imageHandlerResponseWriter;
    }

    public ImageService getImageService() {
        return this.imageService;
    }

    private S3Dao s3Dao() {
        if (ofNullable(getenv(ACTIVE_PROFILE)).orElse("").equals(PROD_PROFILE)) {
            return new S3Dao(this.originalImagesBucket);
        } else {
            AWSCredentials credentials = null;
            try {
                credentials = new ProfileCredentialsProvider().getCredentials();
            } catch (Exception e) {
                throw new AmazonClientException(
                        "Cannot load the credentials from the credential profiles file. " +
                                "Please make sure that your credentials file is at the correct " +
                                "location (~/.aws/credentials), and is in valid format.", e);
            }
            AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion("us-west-2").build();
            return new S3Dao(amazonS3, this.originalImagesBucket);
        }
    }

}
