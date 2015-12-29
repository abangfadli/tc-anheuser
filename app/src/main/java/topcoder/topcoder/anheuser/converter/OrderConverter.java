package topcoder.topcoder.anheuser.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import topcoder.topcoder.anheuser.model.OrderItemModelData;
import topcoder.topcoder.anheuser.model.OrderModelData;
import topcoder.topcoder.anheuser.util.CurrencyUtil;
import topcoder.topcoder.anheuser.view.data.common.Coordinate;
import topcoder.topcoder.anheuser.view.data.common.Order;
import topcoder.topcoder.anheuser.view.data.common.OrderItem;
import topcoder.topcoder.anheuser.view.data.main.MainTile;
import topcoder.topcoder.anheuser.view.data.main.Overview;

/**
 * Created by ahmadfadli on 12/29/15.
 */
public class OrderConverter {
    public static Order convertOrder(OrderModelData modelData) {
        Order order = new Order();
        order.setId(modelData.getId());
        order.setName(modelData.getAccount().getName());
        order.setOrderNumber(modelData.getOrderNumber());
        order.setStatus(modelData.getStatus());
        order.setTotal(CurrencyUtil.formatCurrency("USD", Locale.US, modelData.getTotalAmount()));

        if(modelData.getShippingAddress() != null) {
            order.setAddress(modelData.getShippingAddress().getStreet());
            order.setLatitude(modelData.getShippingAddress().getLatitude());
            order.setLongitude(modelData.getShippingAddress().getLongitude());
        }

        if(modelData.getOrderItemList() != null) {
            order.setOrderItemList(convertOrderItem(modelData.getOrderItemList()));
        }
        return order;
    }

    public static List<Order> convertOrder(List<OrderModelData> modelDataList) {
        List<Order> orderList = new ArrayList<>();
        for(int i = 0; i < modelDataList.size(); i++) {
            orderList.add(convertOrder(modelDataList.get(i)));
        }

        return orderList;
    }


    public static Overview generateMapOverviewTile(List<OrderModelData> orderModelDataList) {
        Overview mainTile = new Overview();
        for(int i = 0; i < orderModelDataList.size(); i++) {
            OrderModelData orderModelData = orderModelDataList.get(i);
            if(orderModelData.getShippingAddress() != null) {
                mainTile.getCoordinateList().add(new Coordinate(orderModelData.getShippingAddress().getLatitude(), orderModelData.getShippingAddress().getLongitude()));
            }
        }

        if(mainTile.getCoordinateList().size() > 0) {
            mainTile.setCenterCoordinate(mainTile.getCoordinateList().get(0));
        }

        return mainTile;
    }

    public static OrderItem convertOrderItem(OrderItemModelData modelData) {
        OrderItem orderItem = new OrderItem();
        orderItem.setName(modelData.getPricebookEntry().getProduct().getName());
        orderItem.setQuantity(modelData.getQuantity());
        orderItem.setUnitPrice(CurrencyUtil.formatCurrency("USD", Locale.US, modelData.getUnitPrice()));
        orderItem.setTotalPrice(CurrencyUtil.formatCurrency("USD", Locale.US, modelData.getUnitPrice() * modelData.getQuantity()));


        return orderItem;
    }

    public static List<OrderItem> convertOrderItem(List<OrderItemModelData> modelDataList) {
        List<OrderItem> orderItemList = new ArrayList<>();
        for (int i = 0; i < modelDataList.size(); i++) {
            orderItemList.add(convertOrderItem(modelDataList.get(i)));
        }

        return orderItemList;
    }
}
