package tec.mf.handler.io;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.io.InputStream;

public interface InputEventParser {

    ImageRequest processInputEvent(InputStream inputStream);
}
