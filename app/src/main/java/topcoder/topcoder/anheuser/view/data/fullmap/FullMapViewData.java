package topcoder.topcoder.anheuser.view.data.fullmap;

import java.util.List;

import topcoder.topcoder.anheuser.view.data.BaseViewData;
import topcoder.topcoder.anheuser.view.data.common.Order;
import topcoder.topcoder.anheuser.view.data.main.Overview;

/**
 * Created by ahmadfadli on 12/30/15.
 */
public class FullMapViewData extends BaseViewData {
    private List<Order> orderList;

    public List<Order> getOrderList() {
        return orderList;
    }

    public FullMapViewData setOrderList(List<Order> orderList) {
        this.orderList = orderList;
        return this;
    }
}
