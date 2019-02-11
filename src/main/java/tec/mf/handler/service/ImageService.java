package tec.mf.handler.service;

import tec.mf.handler.io.ImageRequest;

import java.io.InputStream;

public interface ImageService {

//    InputStream getImage(String imageName);
//
//    InputStream resizeImage(String imageName, int width, int height);

    InputStream getImageFrom(ImageRequest imageRequest);
}
