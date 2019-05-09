package tec.mf.handler.io;

import java.io.InputStream;

public interface InputEventParser {

    ImageRequest processInputEvent(InputStream inputStream);
}
