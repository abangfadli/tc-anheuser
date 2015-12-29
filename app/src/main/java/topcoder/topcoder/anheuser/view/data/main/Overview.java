package topcoder.topcoder.anheuser.view.data.main;

import java.util.ArrayList;
import java.util.List;

import topcoder.topcoder.anheuser.view.data.common.Coordinate;

/**
 * Created by ahmadfadli on 12/27/15.
 */
public class Overview extends MainTile{
    private Coordinate centerCoordinate;
    private List<Coordinate> coordinateList;

    public Overview() {
        coordinateList = new ArrayList<>();
    }

    public Coordinate getCenterCoordinate() {
        return centerCoordinate;
    }

    public Overview setCenterCoordinate(Coordinate centerCoordinate) {
        this.centerCoordinate = centerCoordinate;
        return this;
    }

    public List<Coordinate> getCoordinateList() {
        return coordinateList;
    }

    public Overview setCoordinateList(List<Coordinate> coordinateList) {
        this.coordinateList = coordinateList;
        return this;
    }
}
