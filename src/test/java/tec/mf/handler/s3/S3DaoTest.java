package tec.mf.handler.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.io.FileInputStream;

import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.*;

public class S3DaoTest {

    @Ignore
    @Test
    public void getImage() throws Exception {

        ArgumentCaptor<String> bucketArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> imageArg = ArgumentCaptor.forClass(String.class);

        S3ObjectInputStream inputStream =  new S3ObjectInputStream(
                new FileInputStream(new File("src/test/resources/51ca22dd0cf293ac67bb394a-295xh.jpg")),
                new HttpRequestBase() {
                    @Override
                    public String getMethod() {
                        return "GET";
                    }
                }
        );

        AmazonS3 amazonS3 = mock(AmazonS3.class);
        S3Object s3Object = mock(S3Object.class);
        given(amazonS3.getObject(anyString(), anyString())).willReturn(s3Object);
        given(s3Object.getObjectContent()).willReturn(inputStream);

        S3Dao s3Dao = new S3Dao(amazonS3, "originals");
        Object actual = s3Dao.getImage("image.jpg");

        verify(amazonS3, times(1)).getObject(bucketArg.capture(), imageArg.capture());
        verify(s3Object, times(1)).getObjectContent();

        assertThat(bucketArg.getValue()).isEqualTo("originals");
        assertThat(imageArg.getValue()).isEqualTo("image.jpg");

        assertThat(actual).isNotNull();
        assertThat(actual).isSameAs(inputStream);
    }

}
