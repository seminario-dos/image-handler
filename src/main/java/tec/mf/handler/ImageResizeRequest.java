package tec.mf.handler;

import java.io.Serializable;

public class ImageResizeRequest implements Serializable {

    private String imageUrl;
    private int height;
    private int width;

    public ImageResizeRequest() {

    }

    public ImageResizeRequest(String imageUrl, int height, int width) {
        this.imageUrl = imageUrl;
        this.height = height;
        this.width = width;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
