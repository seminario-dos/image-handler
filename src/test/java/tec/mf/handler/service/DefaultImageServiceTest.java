package tec.mf.handler.service;

import org.junit.Test;
import tec.mf.handler.io.ImageRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class DefaultImageServiceTest {

    @Test
    public void whenNoWidthAndHeightInfo_thenReturnOriginalImage() throws Exception {

        FileInputStream original = new FileInputStream(new File("src/test/resources/51ca22dd0cf293ac67bb394a-295xh.jpg"));
        ImageRequest imageRequest = mock(ImageRequest.class);

        given(imageRequest.getFilename()).willReturn("src/test/resources/51ca22dd0cf293ac67bb394a-295xh.jpg");
        given(imageRequest.getWidth()).willReturn(0);
        given(imageRequest.getHeight()).willReturn(0);

        ImageService service = new DefaultImageService();
        InputStream actual = service.getImageFrom(imageRequest);

        verify(imageRequest, times(1)).getFilename();
        verify(imageRequest, times(1)).getWidth();
        verify(imageRequest, times(0)).getHeight();

        assertThat(actual).hasSameContentAs(original);
    }

//    @Test
//    public void whenValidWithAndHeigth_thenReturnResizedImage() throws Exception {
//
//        FileInputStream original = new FileInputStream(new File("src/test/resources/51ca22dd0cf293ac67bb394a-295xh.jpg"));
//        FileInputStream expected = new FileInputStream(new File("src/test/resources/51ca22dd0cf293ac67bb394a-100xh.jpg"));
//        ImageRequest imageRequest = mock(ImageRequest.class);
//
//        given(imageRequest.getFilename()).willReturn("src/test/resources/51ca22dd0cf293ac67bb394a-295xh.jpg");
//        given(imageRequest.getWidth()).willReturn(100);
//        given(imageRequest.getHeight()).willReturn(100);
//
//        ImageService service = new DefaultImageService();
//        InputStream actual = service.getImageFrom(imageRequest);
//
//        verify(imageRequest, times(1)).getFilename();
//        verify(imageRequest, times(2)).getWidth();
//        verify(imageRequest, times(2)).getHeight();
//
//        assertThat(actual).hasSameContentAs(expected);
//    }

}
