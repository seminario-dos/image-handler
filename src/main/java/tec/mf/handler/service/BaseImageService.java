package tec.mf.handler.service;

import com.amazonaws.services.lambda.runtime.Context;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public abstract class BaseImageService implements ImageService {


    @Override
    public InputStream resizeImage(String imageName, int width, int height) {
        try {
            InputStream imageStream = this.getImage(imageName);
            BufferedImage bufferedImage = ImageIO.read(imageStream);

            BufferedImage resizedImage = Scalr.resize(bufferedImage,
                    Scalr.Method.BALANCED,
                    Scalr.Mode.AUTOMATIC,
                    width,
                    height,
                    Scalr.OP_ANTIALIAS);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpeg", os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            return is;
        } catch (Exception e) {
            System.err.println("Image Resizing failed : " + " " + e.getMessage());
            return null;
        }
    }
}
