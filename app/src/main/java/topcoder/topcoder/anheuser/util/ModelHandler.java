package topcoder.topcoder.anheuser.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.smartstore.app.SmartStoreSDKManager;
import com.salesforce.androidsdk.smartstore.store.IndexSpec;
import com.salesforce.androidsdk.smartstore.store.QuerySpec;
import com.salesforce.androidsdk.smartstore.store.SmartStore;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action2;
import topcoder.topcoder.anheuser.R;
import topcoder.topcoder.anheuser.constant.CommonConstant;
import topcoder.topcoder.anheuser.converter.OrderConverter;
import topcoder.topcoder.anheuser.model.ModelHolder;
import topcoder.topcoder.anheuser.model.OrderItemModelData;
import topcoder.topcoder.anheuser.model.OrderModelData;
import topcoder.topcoder.anheuser.view.data.common.Order;
import topcoder.topcoder.anheuser.view.data.main.MainTile;
import topcoder.topcoder.anheuser.view.data.main.Overview;

/**
 * Created by ahmadfadli on 12/29/15.
 */
public class ModelHandler {

    private static Context mContext;
    private static OrderModelData selectedOrder;

    public static void init(Context context) {
        mContext = context;
    }

    protected void sendQueryRequest(RestClient client, String sql) throws UnsupportedEncodingException {
        sendQueryRequest(client, sql, null, null);
    }

    protected void sendQueryRequest(RestClient client, String sql, Action2<RestRequest, RestResponse> onSuccess) throws UnsupportedEncodingException {
        sendQueryRequest(client, sql, onSuccess, null);
    }

    public static void sendQueryRequest(RestClient client, String soql, Action2<RestRequest, RestResponse> onSuccess, Action1<Exception> onError) {
        RestRequest restRequest = null;
        try {
            restRequest = RestRequest.getRequestForQuery(mContext.getString(R.string.api_version), soql);
        } catch (UnsupportedEncodingException e) {
            if(onError != null) {
                onError.call(e);
            }
        }
        Log.v("SalesForceSDK Query", soql);

        client.sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
            @Override
            public void onSuccess(RestRequest request, RestResponse result) {
                Log.v("SalesForceSDK", result.toString());
                if(onSuccess != null) {
                    onSuccess.call(request, result);
                }
            }

            @Override
            public void onError(Exception exception) {
                if(onError != null) {
                    onError.call(exception);
                } else {
                    // TODO Need to show snackbar
//                    Toast.makeText(MainActivity.this,
//                            MainActivity.this.getString(SalesforceSDKManager.getInstance().getSalesforceR().stringGenericError(), exception.toString()),
//                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static void sendUpdateRequest(RestClient client, String objectType, String objectId, HashMap<String, Object> fields, Action2<RestRequest, RestResponse> onSuccess, Action1<Exception> onError) {
        RestRequest restRequest = null;
        try {
            restRequest = RestRequest.getRequestForUpdate(mContext.getString(R.string.api_version), objectType, objectId, fields);
        } catch (IOException e) {
            if(onError != null) {
                onError.call(e);
            }
        }

//        Log.v("SalesForceSDK Query", sql);
        client.sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
            @Override
            public void onSuccess(RestRequest request, RestResponse result) {
                Log.v("SalesForceSDK", result.toString());
                if(onSuccess != null) {
                    onSuccess.call(request, result);
                }
            }

            @Override
            public void onError(Exception exception) {
                if(onError != null) {
                    onError.call(exception);
                } else {
                    // TODO Need to show snackbar
//                    Toast.makeText(MainActivity.this,
//                            MainActivity.this.getString(SalesforceSDKManager.getInstance().getSalesforceR().stringGenericError(), exception.toString()),
//                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static class OrderRequestor {

        private static SmartStore smartStore;

        public static SmartStore getSmartStore() {
            if(smartStore == null) {
                SmartStoreSDKManager sdkManager = SmartStoreSDKManager.getInstance();
                smartStore = sdkManager.getSmartStore();
                IndexSpec[] ORDER_INDEX_SPEC = {
                        new IndexSpec("Id", SmartStore.Type.string),
                        new IndexSpec("isDirtyCompleted", SmartStore.Type.string)
                };
                smartStore.registerSoup("ORDER", ORDER_INDEX_SPEC);
            }

            return smartStore;
        }

        public static void wipeAndSaveOrder(List<OrderModelData> orderModelDataList) {
            Gson gson = new Gson();
            SmartStore smartStore = getSmartStore();
            smartStore.clearSoup("ORDER");
            for(int i = 0; i < orderModelDataList.size(); i++) {
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(orderModelDataList.get(i)));
                    smartStore.upsert("ORDER", jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        public static void markAsComplete(String id, boolean dirty) {
            OrderModelData modelData = ModelHolder.getInstance().findOrderModelById(id);
            if(modelData == null) {
                return;
            }

            modelData.setStatus(CommonConstant.OrderStatus.COMPLETED);
            modelData.setDirtyCompleted(dirty);
            updateStoredOrderData(modelData);
        }

        private static void updateStoredOrderData(OrderModelData modelData) {
            SmartStore smartStore = getSmartStore();
            try {
                JSONObject jsonObject = new JSONObject(new Gson().toJson(modelData));
                smartStore.upsert("ORDER", jsonObject, "Id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        public static List<MainTile> getStoredOrder() {
            List<OrderModelData> orderModelDataList = getAllStoredOrder();
            ModelHolder.getInstance().setOrderModelList(orderModelDataList);

            List<MainTile> tileList = new ArrayList<>(OrderConverter.convertOrder(orderModelDataList));
            tileList.add(0, OverviewRequestor.getOverviewFromOrder(orderModelDataList));

            return tileList;
        }

        public static List<OrderModelData> getDirtyOrderList() {
            List<OrderModelData> orderModelDataList = new ArrayList<>();

            List<OrderModelData> allOrder = getAllStoredOrder();
            for(int i = 0; i < allOrder.size(); i++) {
                if(allOrder.get(i).isDirtyCompleted()) {
                    orderModelDataList.add(allOrder.get(i));
                }
            }

            return orderModelDataList;
        }

        private static List<OrderModelData> getAllStoredOrder() {
            List<OrderModelData> result = new ArrayList<>();
            JSONArray jsonArray;
            try {
                jsonArray = getSmartStore().query(QuerySpec.buildAllQuerySpec("ORDER", "Id", QuerySpec.Order.ascending, 2000), 0);
                Gson gson = new Gson();
                result = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<OrderModelData>>(){}.getType());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return result;
        }

        public static void requestActiveOrder(RestClient client, Action1<List<MainTile>> onSuccess, Action1<Exception> onError) {
            List<String> fields = Arrays.asList(
                    "Id",
                    "Account.Name",
                    "AccountId",
                    "ShippingAddress",
                    "Status",
                    "OrderNumber",
                    "TotalAmount"
            );
            String fieldJoined = TextUtils.join(", ", fields);

            String query = "SELECT %s From Order Where Status != 'DRAFT'";
            sendQueryRequest(client, String.format(query, fieldJoined), (request, response) -> {

                try {
                    String records = response.asJSONObject().getString("records");
                    Gson gson = new Gson();
                    List<OrderModelData> orderModelDataList = gson.fromJson(records, new TypeToken<ArrayList<OrderModelData>>(){}.getType());

                    // Store the data: Memory and persistent
                    ModelHolder.getInstance().setOrderModelList(orderModelDataList);
                    wipeAndSaveOrder(orderModelDataList);

                    // Add Overview Tile (The first tile)
                    List<MainTile> tileList = new ArrayList<>(OrderConverter.convertOrder(orderModelDataList));
                    tileList.add(0, OverviewRequestor.getOverviewFromOrder(orderModelDataList));
                    ActionUtil.tryCall(onSuccess, tileList);

                } catch (JSONException | ParseException | IOException e) {
                    e.printStackTrace();
                    ActionUtil.tryCall(onError, e);
                }
            }, error -> ActionUtil.tryCall(onError, error));
        }

        public static void requestOrderDetail(RestClient client, String orderId, Action1<Order> onSuccess, Action1<Exception> onError) {
            List<String> fields = Arrays.asList(
                    "Id",
                    "Quantity",
                    "unitprice",
                    "OrderItemNumber",
                    "Pricebookentry.Product2.Name"
            );
            String fieldJoined = TextUtils.join(", ", fields);

            String query = "SELECT %s FROM OrderItem Where OrderId = '%s'";
            sendQueryRequest(client, String.format(query, fieldJoined, orderId), (request, response) -> {
                try {
                    String records = response.asJSONObject().getString("records");
                    Gson gson = new Gson();
                    List<OrderItemModelData> dataModelList = gson.fromJson(records, new TypeToken<ArrayList<OrderItemModelData>>(){}.getType());
                    OrderModelData updatedModelData = ModelHolder.getInstance().setOrderItemListByOrderId(orderId, dataModelList);

                    if (updatedModelData != null) {
                        updateStoredOrderData(updatedModelData);
                        ActionUtil.tryCall(onSuccess, OrderConverter.convertOrder(updatedModelData));
                    }

                } catch (JSONException | ParseException | IOException e) {
                    e.printStackTrace();
                    ActionUtil.tryCall(onError, e);
                }
            }, error -> ActionUtil.tryCall(onError, error));

        }

        public static void putCompletedOrder(RestClient client, String orderId, Action0 onSuccess, Action1<Exception> onError) {
            HashMap<String, Object> fields = new HashMap<>();
            fields.put("Status", CommonConstant.OrderStatus.COMPLETED); // TODO Move hardcoded string to a specific class.
            String objectType = "Order";

            sendUpdateRequest(client, objectType, orderId, fields, (request, response) -> {
                try {
                    String records = response.asString();
                    markAsComplete(orderId, false);
                    ActionUtil.tryCall(onSuccess);

                } catch (ParseException | IOException e) {
                    markAsComplete(orderId, true);
                    e.printStackTrace();
                    ActionUtil.tryCall(onError, e);
                }
            }, error -> {
                markAsComplete(orderId, true);
                ActionUtil.tryCall(onError, error);
            });
        }

        public static List<Order> getOrderList() {
            return OrderConverter.convertOrder(ModelHolder.getInstance().getOrderModelList());
        }

        public static int totalUpdatedOrder;

        public static void updateDirtyOrder(RestClient client, Action1<Integer> onSuccess, Action1<Exception> onError) {
            List<OrderModelData> dirtyOrderList = getDirtyOrderList();
            totalUpdatedOrder = 0;
            if(dirtyOrderList.size() > 0) {
                for(int i = 0; i < dirtyOrderList.size(); i++) {
                    OrderModelData orderModelData = dirtyOrderList.get(i);
                    putCompletedOrder(client, orderModelData.getId(), () -> {
                        orderModelData.setDirtyCompleted(false);
                        updateStoredOrderData(orderModelData);
                        totalUpdatedOrder++;
                        if(totalUpdatedOrder == dirtyOrderList.size()) {
                            ActionUtil.tryCall(onSuccess, totalUpdatedOrder);
                            totalUpdatedOrder = 0;
                        }
                    }, error -> ActionUtil.tryCall(onError, error));
                }
            } else {
                ActionUtil.tryCall(onSuccess, 0);
            }
        }
    }

    public static class OverviewRequestor {
        public static Overview getOverviewFromExistingOrder() {
            List<OrderModelData> orderList = ModelHolder.getInstance().getOrderModelList();
            return getOverviewFromOrder(orderList);
        }

        public static Overview getOverviewFromOrder(List<OrderModelData> orderList) {
            return OrderConverter.generateMapOverviewTile(orderList);
        }
    }

    public static Order getSelectedOrder() {
        return selectedOrder == null ? null : OrderConverter.convertOrder(selectedOrder);
    }

    public static boolean setSelectedOrder(String id) {
        selectedOrder = ModelHolder.getInstance().findOrderModelById(id);
        return selectedOrder != null;
    }
}
