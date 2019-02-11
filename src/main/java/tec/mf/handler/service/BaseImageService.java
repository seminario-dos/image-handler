package tec.mf.handler.service;

import com.amazonaws.services.lambda.runtime.Context;
import org.imgscalr.Scalr;
import tec.mf.handler.io.ImageRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public abstract class BaseImageService implements ImageService {


//    @Override
//    public InputStream resizeImage(String imageName, int width, int height) {
//        try {
//            InputStream imageStream = this.getImage(imageName);
//            BufferedImage bufferedImage = ImageIO.read(imageStream);
//
//            BufferedImage resizedImage = Scalr.resize(bufferedImage,
//                    Scalr.Method.BALANCED,
//                    Scalr.Mode.AUTOMATIC,
//                    width,
//                    height,
//                    Scalr.OP_ANTIALIAS);
//
//            ByteArrayOutputStream os = new ByteArrayOutputStream();
//            ImageIO.write(resizedImage, "jpeg", os);
//            InputStream is = new ByteArrayInputStream(os.toByteArray());
//            return is;
//        } catch (Exception e) {
//            System.err.println("Image Resizing failed : " + " " + e.getMessage());
//            return null;
//        }
//    }


    @Override
    public InputStream getImageFrom(ImageRequest imageRequest) {
        InputStream original = this.getImage(imageRequest.getFilename());
        //TODO: modify the following validation. Do some like dimensionsAreValid(width, height)
        if(imageRequest.getWidth() > 0 && imageRequest.getHeight() > 0) {
            return this.resizeImage(original, imageRequest.getWidth(), imageRequest.getHeight());
        }
        return original;
    }

    protected abstract InputStream getImage(String filename);

    protected InputStream resizeImage(InputStream originalImageStream, int width, int height) {
        try {
            BufferedImage bufferedImage = ImageIO.read(originalImageStream);

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
