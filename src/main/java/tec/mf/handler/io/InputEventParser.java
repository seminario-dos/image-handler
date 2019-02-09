package tec.mf.handler.io;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.io.InputStream;

public interface InputEventParser {

    //TODO: Change this signature. LambdaLogger could be not needed
    ImageRequest processInputEvent(InputStream inputStream, LambdaLogger lambdaLogger);
}
