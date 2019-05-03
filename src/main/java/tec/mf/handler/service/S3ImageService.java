package tec.mf.handler.service;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import org.imgscalr.Scalr;
import tec.mf.handler.io.ImageRequest;
import tec.mf.handler.s3.S3Dao;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.function.Function;

public class S3ImageService implements ImageService {

    private final S3Dao dao;

    public S3ImageService(S3Dao dao) {
        this.dao = dao;
    }


    @Override
    public InputStream getImageFrom(ImageRequest imageRequest) {
        InputStream original = this.getImage(imageRequest.getFilename());
        //TODO: modify the following validation. Do some like dimensionsAreValid(width, height)
        if(imageRequest.getWidth() > 0 && imageRequest.getHeight() > 0) {

            return AWSXRay.createSubsegment("resize operation", new Function<Subsegment, InputStream>() {
                @Override
                public InputStream apply(Subsegment subsegment) {
                    return resizeImage(original, imageRequest.getWidth(), imageRequest.getHeight());
                }
            });
        }
//        return original;

        try {
            BufferedImage bufferedImage = ImageIO.read(original);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpeg", os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            return is;
        } catch (Exception e) {
            return original;
        }
    }

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


    public InputStream getImage(String imageName) {

        InputStream imageStream = this.dao.getImage(imageName);

        return imageStream;
    }
}
