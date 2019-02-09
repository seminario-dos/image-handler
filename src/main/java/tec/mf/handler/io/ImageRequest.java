package tec.mf.handler.io;

public class ImageRequest {

//    private String name;
//    private String day;
//    private String time;

    private int width;
    private int height;
    private String filename;

//    public ImageRequest(){
//
//    }
//
//    public ImageRequest(String name, String day, String time) {
//        this.name = name;
//        this.day = day;
//        this.time = time;
//    }

    public ImageRequest(String filename, int width, int height) {
        this.filename = filename;
        this.width = width;
        this.height = height;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDay() {
//        return day;
//    }
//
//    public void setDay(String day) {
//        this.day = day;
//    }
//
//    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
