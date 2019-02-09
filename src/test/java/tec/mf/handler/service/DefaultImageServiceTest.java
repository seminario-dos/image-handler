package tec.mf.handler.service;

import org.apache.http.impl.io.EmptyInputStream;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.*;

public class DefaultImageServiceTest {

    @Test
    public void getImage() throws Exception {
        FileInputStream expected = new FileInputStream(new File("src/test/resources/51ca22dd0cf293ac67bb394a-295xh.jpg"));
        ImageService service = new DefaultImageService();
        InputStream actual = service.getImage("src/test/resources/51ca22dd0cf293ac67bb394a-295xh.jpg");

        assertThat(actual).hasSameContentAs(expected);
    }

    @Test
    public void whenImageIsNotFound_thenEmptyStream() throws Exception {
        ImageService service = new DefaultImageService();
        InputStream actual = service.getImage("not-found.jpg");

        assertThat(actual).isInstanceOf(EmptyInputStream.class);
    }


}
