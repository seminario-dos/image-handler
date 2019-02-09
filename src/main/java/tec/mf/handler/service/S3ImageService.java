package tec.mf.handler.service;

import tec.mf.handler.s3.S3Dao;

import java.io.InputStream;

public class S3ImageService extends BaseImageService {

    private final S3Dao dao;

    public S3ImageService(S3Dao dao) {
        this.dao = dao;
    }

    @Override
    public InputStream getImage(String imageName) {
        return this.dao.getImage(imageName);
    }

}
