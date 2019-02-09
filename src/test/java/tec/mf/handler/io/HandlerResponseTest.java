package tec.mf.handler.io;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static org.mockito.BDDMockito.*;

public class HandlerResponseTest {

    @Test
    public void generateOutput() throws Exception {

        ImageRequest imageRequest = mock(ImageRequest.class);
        FileInputStream inputStream = new FileInputStream(new File("src/test/resources/51ca22dd0cf293ac67bb394a-295xh.jpg"));
        FileOutputStream outputStream = new FileOutputStream(new File("src/test/resources/the-response.json"));

        ImageHandlerResponseWriter imageHandlerResponseWriter = new HandlerResponseWriter();
        imageHandlerResponseWriter.writeResponse(inputStream, outputStream, imageRequest);

    }
}
