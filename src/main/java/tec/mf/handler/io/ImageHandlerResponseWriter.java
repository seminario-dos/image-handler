package tec.mf.handler.io;

import java.io.InputStream;
import java.io.OutputStream;

public interface ImageHandlerResponseWriter {

    void writeResponse(InputStream inputStream, OutputStream outputStream, ImageRequest imageRequest);
}
