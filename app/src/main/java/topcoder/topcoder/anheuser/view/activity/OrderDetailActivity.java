package topcoder.topcoder.anheuser.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.salesforce.androidsdk.rest.RestClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import topcoder.topcoder.anheuser.R;
import topcoder.topcoder.anheuser.constant.CommonConstant;
import topcoder.topcoder.anheuser.util.MapUtil;
import topcoder.topcoder.anheuser.util.ModelHandler;
import topcoder.topcoder.anheuser.view.data.common.Order;
import topcoder.topcoder.anheuser.view.data.common.OrderItem;
import topcoder.topcoder.anheuser.view.data.orderdetail.OrderDetailViewData;

public class OrderDetailActivity extends BaseActivity<OrderDetailViewData> {

    //================================================================================
    // VIEW OBJECT
    //================================================================================
    @Bind(R.id.fab_location)
    FloatingActionButton vLocationFAB;

    @Bind(R.id.tv_order_account_name)
    TextView vAccountNameTV;

    @Bind(R.id.tv_order_account_address)
    TextView vAccountAddressTV;

    @Bind(R.id.tv_order_number)
    TextView vOrderNumberTV;

    @Bind(R.id.tv_order_total)
    TextView vOrderTotalTV;

    @Bind(R.id.layout_order_item)
    LinearLayout vOrderItemLayout;

    @Bind(R.id.btn_order_complete)
    Button vOrderCompleteBtn;

    boolean isDataFetched;

    //================================================================================
    // LIFE CYCLE
    //================================================================================
    @Override
    protected void onAdjustProperties() {
        super.onAdjustProperties();
        mContentViewId = R.layout.activity_order_detail;
        mLeftButtonType = CommonConstant.LeftButton.BACK;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onResume(RestClient client) {
        super.onResume(client);

        // If data not fetched yet, request detail of the order
        if(!isDataFetched) {
            requestOrderDetail();
            isDataFetched = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //================================================================================
    // INIT
    //================================================================================
    protected void initViewState() {
        // Obtain the Order object that's previously stored in ModelHandler
        OrderDetailViewData viewData = new OrderDetailViewData();

        Order order = ModelHandler.getSelectedOrder();
        if(order == null) {
            finish(); // Illegal State. Should not happen.
        }

        viewData.setOrder(order);
        setViewData(viewData);
    }

    protected void initListener() {
        // Complete Button
        vOrderCompleteBtn.setOnClickListener(v -> {
            if(!CommonConstant.OrderStatus.isCompleted(mViewData.getOrder().getStatus())) {

                // Show LoadingDialog
                ProgressDialog progressDialog = ProgressDialog.show(this, getString(R.string.text_saving), getString(R.string.text_please_wait), true, false);
                progressDialog.show();

                ModelHandler.OrderRequestor.putCompletedOrder(client, mViewData.getOrder().getId(), () -> {
                    // Hide LoadingDialog
                    progressDialog.hide();
                    onBackPressed();
                }, (error) -> {
                    // Error when marking as complete. Set it to dirty and update next time syncing.
                    progressDialog.hide();
                    String message = error.getMessage();
                    message = getString(R.string.message_offline_dirty_sync);

                    Snackbar snackbar = Snackbar.make(vAccountNameTV, message, Snackbar.LENGTH_INDEFINITE).setAction(R.string.text_ok, (view) -> {
                        onViewDataChanged();
                    });

                    View snackbarView = snackbar.getView();
                    TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setMaxLines(5);

                    snackbar.show();
                });
            }
        });

        // Open maps of current Order.
        vLocationFAB.setOnClickListener(v -> {
            if(mViewData.getOrder() != null) {
                double lat = mViewData.getOrder().getLatitude();
                double lng = mViewData.getOrder().getLongitude();
                String label = mViewData.getOrder().getName();

                MapUtil.launchMapsAddress(this, lat, lng, mViewData.getOrder().getAddress());
            }
        });
    }

    //================================================================================
    // VIEW DATA ORGANIZER
    //================================================================================
    @Override
    protected void onViewDataChanged() {
        super.onViewDataChanged();

        vAccountNameTV.setText(mViewData.getOrder().getName());
        vAccountAddressTV.setText(mViewData.getOrder().getAddress());
        vOrderNumberTV.setText(getString(R.string.order_number_format, mViewData.getOrder().getOrderNumber()));
        vOrderTotalTV.setText(getString(R.string.order_total_format, mViewData.getOrder().getTotal()));

        vOrderItemLayout.removeAllViews();
        for(int i = 0; i < mViewData.getOrder().getOrderItemList().size(); i++) {
            OrderItemViewHolder orderItemViewHolder = new OrderItemViewHolder(this, vOrderItemLayout, mViewData.getOrder().getOrderItemList().get(i));
            vOrderItemLayout.addView(orderItemViewHolder.getRootView());
        }


        if(!CommonConstant.OrderStatus.isCompleted(mViewData.getOrder().getStatus())) {
            vOrderCompleteBtn.setEnabled(true);
            vOrderCompleteBtn.setVisibility(View.VISIBLE);
        } else {
            vOrderCompleteBtn.setEnabled(false);
            vOrderCompleteBtn.setVisibility(View.GONE);
        }
    }

    /**
     * Request Order Items to SDK
     */
    private void requestOrderDetail() {

        ModelHandler.OrderRequestor.requestOrderDetail(client, mViewData.getOrder().getId(), (order) -> {
            mViewData.setOrder(order);
            onViewDataChanged();
        }, this::showErrorMessage);
    }

    private void showErrorMessage(Exception error) {
        String message = error.getMessage();

        if (error instanceof NoConnectionError) {
            message = getString(R.string.message_no_connection);
        } else if(error instanceof ServerError || error instanceof TimeoutError) {
            message = getString(R.string.message_server_error);
        }

        Snackbar.make(vAccountNameTV, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * ViewHolder of each ProductItem
     */
    public class OrderItemViewHolder {
        View rootView;
        Context context;

        @Bind(R.id.tv_order_item_name)
        TextView vOrderItemNameTV;

        @Bind(R.id.tv_order_item_qty)
        TextView vOrderItemQtyTV;

        @Bind(R.id.tv_order_item_unit)
        TextView vOrderItemUnitTV;

        @Bind(R.id.tv_order_item_total)
        TextView vOrderItemTotalTV;

        public OrderItemViewHolder(Context context, ViewGroup parent, OrderItem orderItem) {
            this.context = context;
            rootView = LayoutInflater.from(context).inflate(R.layout.item_list_order_item, parent, false);
            ButterKnife.bind(this, rootView);

            init(orderItem);
        }

        private void init(OrderItem orderItem) {
            vOrderItemNameTV.setText(orderItem.getName());
            vOrderItemQtyTV.setText(context.getString(R.string.order_item_quantity_format, orderItem.getQuantity()));
            vOrderItemUnitTV.setText(context.getString(R.string.order_item_unit_price_format, orderItem.getUnitPrice()));
            vOrderItemTotalTV.setText(context.getString(R.string.order_item_total_format, orderItem.getTotalPrice()));
        }

        public View getRootView() {
            return rootView;
        }
    }
}