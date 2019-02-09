package tec.mf.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.Test;
import tec.mf.handler.io.ImageHandlerResponseWriter;
import tec.mf.handler.io.ImageRequest;
import tec.mf.handler.io.ImageRequestParser;
import tec.mf.handler.service.ImageService;

import java.io.*;

import static org.mockito.BDDMockito.*;

public class ImageHandlerTest {


    @Test
    public void resizeImage() throws Exception {

        InputStream inputStream =  new FileInputStream(new File("src/test/resources/image-request-event.json"));
        FileOutputStream fileOutputStream = new FileOutputStream("src/test/resources/image-handler-response.json");
        FileInputStream resizedStream = new FileInputStream(new File("src/test/resources/51ca22dd0cf293ac67bb394a-100xh.jpg"));
        OutputStream outputStream = new ObjectOutputStream(fileOutputStream);

        LambdaLogger lambdaLogger = mock(LambdaLogger.class);
        Context context = mock(Context.class);
        AppConfig appConfig = mock(AppConfig.class);
        ImageRequestParser imageRequestParser = mock(ImageRequestParser.class);
        ImageHandlerResponseWriter imageHandlerResponseWriter = mock(ImageHandlerResponseWriter.class);
        ImageService imageService = mock(ImageService.class);
        ImageRequest imageRequest = mock(ImageRequest.class);

        given(context.getLogger()).willReturn(lambdaLogger);
        given(imageRequest.getFilename()).willReturn("51ca22dd0cf293ac67bb394a-295xh.jpg");
        given(imageRequestParser.processInputEvent(any(InputStream.class), any(LambdaLogger.class))).willReturn(imageRequest);
        given(imageService.resizeImage(anyString(), anyInt(), anyInt())).willReturn(resizedStream);

        given(appConfig.getInputEventParser()).willReturn(imageRequestParser);
        given(appConfig.getImageHandlerResponseWriter()).willReturn(imageHandlerResponseWriter);
        given(appConfig.getImageService()).willReturn(imageService);

        ImageHandler imageHandler = new ImageHandler(appConfig);
        imageHandler.handleRequest(inputStream, outputStream, context);

        verify(appConfig, times(1)).getInputEventParser();
        verify(appConfig, times(1)).getImageService();
        verify(appConfig, times(1)).getImageHandlerResponseWriter();
    }

}
