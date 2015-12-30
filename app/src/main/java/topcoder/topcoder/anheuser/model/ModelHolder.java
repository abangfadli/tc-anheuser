package topcoder.topcoder.anheuser.model;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmadfadli on 12/29/15.
 */
public class ModelHolder {
    //================================================================================
    // SINGLETON BOILERPLATE
    //================================================================================
    private static ModelHolder instance;
    public static ModelHolder getInstance() {
        if(instance == null) {
            instance = new ModelHolder();
        }

        return instance;
    }

    private ModelHolder() {
        orderModelDataList = new ArrayList<>();
    }


    //================================================================================
    // MODELS
    //================================================================================
    /**
     * Order List
     */
    private List<OrderModelData> orderModelDataList;

    public void setOrderModelList(List<OrderModelData> orderModelList) {
        orderModelDataList = orderModelList;
    }

    public List<OrderModelData> getOrderModelList() {
        return orderModelDataList;
    }

    /**
     * Get an OrderModel by Id
     * @param id to be searched
     * @return OrderModel or Null if not found
     */
    @Nullable
    public OrderModelData findOrderModelById(String id) {
        for(int i = 0; i < orderModelDataList.size(); i++) {
            if(orderModelDataList.get(i).getId().equals(id)) {
                return orderModelDataList.get(i);
            }
        }

        return null;
    }

    /**
     * Set the OrderItemModelData in OrderModelData. Since they have different API and need to be merged.
     * @param orderId OrdersId to be searched
     * @param dataModelList OrderItemList
     * @return updated OrderModelData
     */
    public OrderModelData setOrderItemListByOrderId(String orderId, List<OrderItemModelData> dataModelList) {
        OrderModelData orderModelData = findOrderModelById(orderId);
        if(orderModelData != null) {
            orderModelData.setOrderItemList(dataModelList);
            return orderModelData;
        }

        return null;
    }

}
