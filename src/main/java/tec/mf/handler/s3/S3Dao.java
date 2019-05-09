package tec.mf.handler.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;

import java.io.InputStream;

public class S3Dao {
//    private static final String KEY_PREFIX = "originals/";
    private static final String KEY_PREFIX = "cluster-a/";
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
        S3Object s3Object = this.amazonS3.getObject(bucket, KEY_PREFIX + name);

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
