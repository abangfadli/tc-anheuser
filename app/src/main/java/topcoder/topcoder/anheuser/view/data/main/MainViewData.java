package topcoder.topcoder.anheuser.view.data.main;

import java.util.ArrayList;
import java.util.List;

import topcoder.topcoder.anheuser.view.data.BaseViewData;

/**
 * Created by ahmadfadli on 12/27/15.
 */
public class MainViewData extends BaseViewData {
    private List<MainTile> tileList;

    public MainViewData() {
        tileList = new ArrayList<>();
    }

    public List<MainTile> getTileList() {
        return tileList;
    }

    public MainViewData setTileList(List<MainTile> mOrderList) {
        this.tileList = mOrderList;
        return this;
    }
}
