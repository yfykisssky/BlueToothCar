package car.bluetooth.com.bluetoothcar.xxxcar.util;

public class DataOrderCenter {

    private String avoid = "";
    private String find = "";

    public String getAvoid() {
        synchronized (avoid) {
            return avoid;
        }
    }

    public void setAvoid(String avoid) {
        synchronized (avoid) {
            this.avoid = avoid;
        }
    }

    public String getFind() {
        synchronized (find) {
            return find;
        }
    }

    public void setFind(String find) {
        synchronized (find) {
            this.find = find;
        }
    }
}
