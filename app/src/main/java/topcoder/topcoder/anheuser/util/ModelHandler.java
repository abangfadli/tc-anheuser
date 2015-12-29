package topcoder.topcoder.anheuser.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

import org.apache.http.ParseException;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;
import rx.functions.Action2;
import topcoder.topcoder.anheuser.R;
import topcoder.topcoder.anheuser.converter.OrderConverter;
import topcoder.topcoder.anheuser.model.ModelHolder;
import topcoder.topcoder.anheuser.model.OrderItemModelData;
import topcoder.topcoder.anheuser.model.OrderModelData;
import topcoder.topcoder.anheuser.view.data.common.Order;
import topcoder.topcoder.anheuser.view.data.common.OrderItem;
import topcoder.topcoder.anheuser.view.data.main.MainTile;

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

    public static void sendQueryRequest(RestClient client, String sql, Action2<RestRequest, RestResponse> onSuccess, Action1<Exception> onError) {
        RestRequest restRequest = null;
        try {
            restRequest = RestRequest.getRequestForQuery(mContext.getString(R.string.api_version), sql);
        } catch (UnsupportedEncodingException e) {
            if(onError != null) {
                onError.call(e);
            }
        }
        client.sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
            @Override
            public void onSuccess(RestRequest request, RestResponse result) {
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
        public static void requestActiveOrder(RestClient client, Action1<List<MainTile>> onSuccess, Action1<Exception> onError) {
            String query = "SELECT Id, Account.Name, AccountId, ShippingAddress, Status, OrderNumber From Order";
            sendQueryRequest(client, query, (request, response) -> {

                try {
                    String records = response.asJSONObject().getString("records");
                    Gson gson = new Gson();
                    List<OrderModelData> orderModelDataList = gson.fromJson(records, new TypeToken<ArrayList<OrderModelData>>(){}.getType());
                    ModelHolder.getInstance().setOrderModelList(orderModelDataList);

                    List<MainTile> orderList = OrderConverter.convertOrder(orderModelDataList);
                    if(onSuccess != null) {
                        onSuccess.call(orderList);
                    }
                } catch (JSONException | ParseException | IOException e) {
                    e.printStackTrace();
                    if(onError != null) {
                        onError.call(e);
                    }
                }
            }, error -> {
                if (onError != null) {
                    onError.call(error);
                }
            });
        }

        public static void requestOrderDetail(RestClient client, String orderId, Action1<Order> onSuccess, Action1<Exception> onError) {
            String query = "SELECT Id, Quantity, unitprice, OrderItemNumber, OriginalOrderItemId, Pricebookentry.Product2.Name FROM OrderItem Where OrderId = '%s'";
            sendQueryRequest(client, String.format(query, orderId), (request, response) -> {
                try {
                    String records = response.asJSONObject().getString("records");
                    Gson gson = new Gson();
                    List<OrderItemModelData> dataModelList = gson.fromJson(records, new TypeToken<ArrayList<OrderItemModelData>>(){}.getType());
                    OrderModelData updatedModelData = ModelHolder.getInstance().setOrderItemListByOrderId(orderId, dataModelList);

                    if (updatedModelData != null) {
                        if (onSuccess != null) {
                            onSuccess.call(OrderConverter.convertOrder(updatedModelData));
                        }
                    }

                } catch (JSONException | ParseException | IOException e) {
                    e.printStackTrace();
                    if(onError != null) {
                        onError.call(e);
                    }
                }
            }, error -> {
                if (onError != null) {
                    onError.call(error);
                }
            });

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
