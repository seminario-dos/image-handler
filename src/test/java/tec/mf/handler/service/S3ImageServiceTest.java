package tec.mf.handler.service;

import org.junit.Test;
import tec.mf.handler.s3.S3Dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

public class S3ImageServiceTest {

    @Test
    public void getImage() throws Exception {
        FileInputStream expected = new FileInputStream(new File("src/test/resources/51ca22dd0cf293ac67bb394a-295xh.jpg"));
        S3Dao s3Dao = mock(S3Dao.class);

        given(s3Dao.getImage(anyString())).willReturn(expected);

        ImageService service = new S3ImageService(s3Dao);
        InputStream actual = service.getImage("my-image.jpg");

        verify(s3Dao, times(1)).getImage(anyString());

        assertThat(actual).isSameAs(expected);
    }

}
