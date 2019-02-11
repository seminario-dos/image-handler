package tec.mf.handler.service;

import org.junit.Test;
import tec.mf.handler.io.ImageRequest;
import tec.mf.handler.s3.S3Dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

public class S3ImageServiceTest {


    @Test
    public void whenNoWidthAndHeightInfo_thenReturnOriginalImage() throws Exception {

        FileInputStream original = new FileInputStream(new File("src/test/resources/51ca22dd0cf293ac67bb394a-295xh.jpg"));
        ImageRequest imageRequest = mock(ImageRequest.class);
        S3Dao s3Dao = mock(S3Dao.class);

        given(imageRequest.getFilename()).willReturn("my-image.jpeg");
        given(imageRequest.getWidth()).willReturn(0);
        given(imageRequest.getHeight()).willReturn(0);
        given(s3Dao.getImage(anyString())).willReturn(original);

        ImageService service = new S3ImageService(s3Dao);
        InputStream actual = service.getImageFrom(imageRequest);

        verify(imageRequest, times(1)).getFilename();
        verify(imageRequest, times(1)).getWidth();
        verify(imageRequest, times(0)).getHeight();
        verify(s3Dao, times(1)).getImage(anyString());

        assertThat(actual).isSameAs(original);
    }

    @Test
    public void whenValidWithAndHeigth_thenReturnResizedImage() throws Exception {

        FileInputStream original = new FileInputStream(new File("src/test/resources/51ca22dd0cf293ac67bb394a-295xh.jpg"));
        FileInputStream expected = new FileInputStream(new File("src/test/resources/51ca22dd0cf293ac67bb394a-100xh.jpg"));
        ImageRequest imageRequest = mock(ImageRequest.class);
        S3Dao s3Dao = mock(S3Dao.class);

        given(imageRequest.getFilename()).willReturn("my-image.jpeg");
        given(imageRequest.getWidth()).willReturn(100);
        given(imageRequest.getHeight()).willReturn(100);
        given(s3Dao.getImage(anyString())).willReturn(original);

        ImageService service = new S3ImageService(s3Dao);
        InputStream actual = service.getImageFrom(imageRequest);

        verify(imageRequest, times(1)).getFilename();
        verify(imageRequest, times(2)).getWidth();
        verify(imageRequest, times(2)).getHeight();
        verify(s3Dao, times(1)).getImage(anyString());

        assertThat(actual).hasSameContentAs(expected);
    }

}
