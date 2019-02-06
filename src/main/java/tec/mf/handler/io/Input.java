package tec.mf.handler.io;

public class Input {

    private String name;
    private String day;
    private String time;

    public Input(){

    }

    public Input(String name, String day, String time) {
        this.name = name;
        this.day = day;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
