package topcoder.topcoder.anheuser.view.data.common;

/**
 * Created by ahmadfadli on 12/29/15.
 */
public class Coordinate {
    private double latitude;
    private double longitude;

    public Coordinate(double lat, double lng) {
        latitude = lat;
        longitude = lng;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
