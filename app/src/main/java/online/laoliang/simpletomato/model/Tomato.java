package online.laoliang.simpletomato.model;

/**
 * Created by lpy on 17/12/7.
 */

public class Tomato {

    private int id;
    private String tomatoStatus;
    private int durationMin;
    private long nonceTimestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTomatoStatus() {
        return tomatoStatus;
    }

    public void setTomatoStatus(String tomatoStatus) {
        this.tomatoStatus = tomatoStatus;
    }

    public int getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(int durationMin) {
        this.durationMin = durationMin;
    }

    public long getNonceTimestamp() {
        return nonceTimestamp;
    }

    public void setNonceTimestamp(long nonceTimestamp) {
        this.nonceTimestamp = nonceTimestamp;
    }
}
