package topcoder.topcoder.anheuser.view.data.main;

/**
 * Created by ahmadfadli on 12/27/15.
 */
public class Overview extends MainTile{
    private long longitude;
    private long latitude;

    public long getLongitude() {
        return longitude;
    }

    public Overview setLongitude(long longitude) {
        this.longitude = longitude;
        return this;
    }

    public long getLatitude() {
        return latitude;
    }

    public Overview setLatitude(long latitude) {
        this.latitude = latitude;
        return this;
    }
}
