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
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import topcoder.topcoder.anheuser.R;
import topcoder.topcoder.anheuser.constant.CommonConstants;
import topcoder.topcoder.anheuser.view.data.common.OrderItem;
import topcoder.topcoder.anheuser.view.data.orderdetail.OrderDetailViewData;

public class OrderDetailActivity extends BaseActivity<OrderDetailViewData> {

    //================================================================================
    // VIEW OBJECT
    //================================================================================
    @Bind(R.id.layout_order_item)
    LinearLayout vOrderItemLayout;

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
        generateDummy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
    protected void initState() {
        OrderDetailViewData viewData = new OrderDetailViewData();
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
            OrderItemView orderItemView = new OrderItemView(this, vOrderItemLayout);
        }
    }

    //================================================================================
    // DUMMY DATA GENERATOR
    //================================================================================
    private void generateDummy() {

        mViewData.getOrder().getOrderItemList().add(new OrderItem());
        mViewData.getOrder().getOrderItemList().add(new OrderItem());
        mViewData.getOrder().getOrderItemList().add(new OrderItem());
        mViewData.getOrder().getOrderItemList().add(new OrderItem());
        mViewData.getOrder().getOrderItemList().add(new OrderItem());
        mViewData.getOrder().getOrderItemList().add(new OrderItem());
        mViewData.getOrder().getOrderItemList().add(new OrderItem());
        mViewData.getOrder().getOrderItemList().add(new OrderItem());
        mViewData.getOrder().getOrderItemList().add(new OrderItem());
        mViewData.getOrder().getOrderItemList().add(new OrderItem());
        mViewData.getOrder().getOrderItemList().add(new OrderItem());
        mViewData.getOrder().getOrderItemList().add(new OrderItem());
        mViewData.getOrder().getOrderItemList().add(new OrderItem());
        mViewData.getOrder().getOrderItemList().add(new OrderItem());
        mViewData.getOrder().getOrderItemList().add(new OrderItem());

        onViewDataChanged();
    }

    public class OrderItemView {
        View rootView;

        @Bind(R.id.tv_order_item_name)
        TextView vOrderItemTV;

        @Bind(R.id.tv_order_item_qty)
        TextView vOrderItemQtyTV;

        @Bind(R.id.tv_order_item_unit)
        TextView vOrderItemUnitTV;

        @Bind(R.id.tv_order_item_total)
        TextView vOrderItemTotalTV;

        public OrderItemView(Context context, ViewGroup parent) {
            rootView = LayoutInflater.from(context).inflate(R.layout.item_list_order_item, parent, true);
            ButterKnife.bind(this, rootView);
        }

        public View getRootView() {
            return rootView;
        }
    }
}