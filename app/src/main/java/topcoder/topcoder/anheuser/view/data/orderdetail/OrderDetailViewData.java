package topcoder.topcoder.anheuser.view.data.orderdetail;

import topcoder.topcoder.anheuser.view.data.BaseViewData;
import topcoder.topcoder.anheuser.view.data.common.Order;

/**
 * Created by ahmadfadli on 12/27/15.
 */
public class OrderDetailViewData extends BaseViewData {
    private Order order;

    public OrderDetailViewData() {
        order = new Order();
    }

    public Order getOrder() {
        return order;
    }

    public OrderDetailViewData setOrder(Order order) {
        this.order = order;
        return this;
    }
}
