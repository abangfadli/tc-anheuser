package topcoder.topcoder.anheuser.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

public class SplashActivity extends BaseActivity<OrderDetailViewData> {

    //================================================================================
    // VIEW OBJECT
    //================================================================================

    //================================================================================
    // LIFE CYCLE
    //================================================================================
    @Override
    protected void onAdjustProperties() {
        super.onAdjustProperties();
        mContentViewId = R.layout.activity_splash;
        mLeftButtonType = CommonConstant.LeftButton.NONE;
    }

    //================================================================================
    // INIT
    //================================================================================
    protected void initViewState() {
        OrderDetailViewData viewData = new OrderDetailViewData();
        setViewData(viewData);


        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }, 1500);
    }

    protected void initListener() {

    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }
}