package tec.mf.handler.service;

import org.apache.http.impl.io.EmptyInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DefaultImageService extends BaseImageService {

    @Override
    public InputStream getImage(String imageName) {
        try {
            File file = new File(imageName);
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return EmptyInputStream.INSTANCE;
        }
    }

}
