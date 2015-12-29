package topcoder.topcoder.anheuser.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.salesforce.androidsdk.rest.RestClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import topcoder.topcoder.anheuser.R;
import topcoder.topcoder.anheuser.constant.CommonConstants;
import topcoder.topcoder.anheuser.util.ModelHandler;
import topcoder.topcoder.anheuser.view.data.common.Order;
import topcoder.topcoder.anheuser.view.data.common.OrderItem;
import topcoder.topcoder.anheuser.view.data.orderdetail.OrderDetailViewData;

public class OrderDetailActivity extends BaseActivity<OrderDetailViewData> {

    //================================================================================
    // VIEW OBJECT
    //================================================================================
    @Bind(R.id.layout_order_item)
    LinearLayout vOrderItemLayout;

    boolean isDataFetched;

    //================================================================================
    // LIFE CYCLE
    //================================================================================
    @Override
    protected void onAdjustProperties() {
        super.onAdjustProperties();
        mContentViewId = R.layout.activity_order_detail;
        mLeftButtonType = CommonConstants.LeftButton.BACK;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onResume(RestClient client) {
        super.onResume(client);
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
        OrderDetailViewData viewData = new OrderDetailViewData();

        Order order = ModelHandler.getSelectedOrder();
        if(order == null) {
            finish(); // Illegal
        }

        viewData.setOrder(order);
        setViewData(viewData);
    }

    protected void initListener() {

    }

    //================================================================================
    // VIEW DATA ORGANIZER
    //================================================================================
    @Override
    protected void onViewDataChanged() {
        super.onViewDataChanged();

        vOrderItemLayout.removeAllViews();
        for(int i = 0; i < mViewData.getOrder().getOrderItemList().size(); i++) {
            OrderItemViewHolder orderItemViewHolder = new OrderItemViewHolder(this, vOrderItemLayout, mViewData.getOrder().getOrderItemList().get(i));
            vOrderItemLayout.addView(orderItemViewHolder.getRootView());
        }
    }

    //================================================================================
    // DUMMY DATA GENERATOR
    //================================================================================
    private void requestOrderDetail() {

        ModelHandler.OrderRequestor.requestOrderDetail(client, mViewData.getOrder().getId(), (order) -> {
            mViewData.setOrder(order);
            onViewDataChanged();
        }, error -> {

        });
    }

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