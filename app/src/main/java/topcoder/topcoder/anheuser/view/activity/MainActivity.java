package topcoder.topcoder.anheuser.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.salesforce.androidsdk.rest.RestClient;

import java.net.UnknownHostException;
import java.util.ArrayList;

import butterknife.Bind;
import rx.functions.Action1;
import topcoder.topcoder.anheuser.R;
import topcoder.topcoder.anheuser.constant.CommonConstants;
import topcoder.topcoder.anheuser.util.MapUtil;
import topcoder.topcoder.anheuser.util.ModelHandler;
import topcoder.topcoder.anheuser.util.ViewExpandCollapseUtil;
import topcoder.topcoder.anheuser.view.adapter.GridAdapter;
import topcoder.topcoder.anheuser.view.data.common.Order;
import topcoder.topcoder.anheuser.view.data.main.MainTile;
import topcoder.topcoder.anheuser.view.data.main.MainViewData;
import topcoder.topcoder.anheuser.view.data.main.Overview;

public class MainActivity extends BaseActivity<MainViewData> {

    private static final int MINIMUM_LOADING_TIME = 2500; // cosmetic

    //================================================================================
    // VIEW OBJECT
    //================================================================================
    @Bind(R.id.gv_main_tile)
    GridView vMainTileGV;

    @Bind(R.id.layout_sync_progress)
    LinearLayout vSyncProgressLayout;

    GridAdapter gridAdapter;

    boolean isSyncLayoutShown;
    boolean isDataFetched;

    //================================================================================
    // LIFE CYCLE
    //================================================================================
    @Override
    protected void onAdjustProperties() {
        super.onAdjustProperties();
        mContentViewId = R.layout.activity_main;
        mLeftButtonType = CommonConstants.LeftButton.SYNC;
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
    public void onResume(RestClient client) {
        super.onResume(client);
        loadCachedData();
        trySync(false);
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
                trySync(true);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //================================================================================
    // INIT
    //================================================================================
    protected void initViewState() {
        isSyncLayoutShown = false;

        if(mViewData == null) {
            setViewData(new MainViewData());
        }

        if(gridAdapter == null) {
            gridAdapter = new GridAdapter(this, new ArrayList<>());
        }

        gridAdapter = new GridAdapter(this, new ArrayList<>());
        gridAdapter.setOnItemClicked(itemClickListener);
        gridAdapter.setOnItemActionClicked(itemActionButtonClicked);
        vMainTileGV.setAdapter(gridAdapter);
    }

    protected void initListener() {

    }

    //================================================================================
    // LISTENER
    //================================================================================
    private Action1<MainTile> itemClickListener = mainTile -> {
        if (mainTile instanceof Order) {
            if (ModelHandler.setSelectedOrder(((Order)mainTile).getId())) {
                Intent intent = new Intent(this, OrderDetailActivity.class);
                startActivity(intent);
            }
        } else if(mainTile instanceof Overview) {
            Intent intent = new Intent(this, FullMapActivity.class);
            startActivity(intent);
        }
    };

    private Action1<MainTile> itemActionButtonClicked = mainTile -> {
        if(mainTile instanceof Order) {

            double lat = ((Order) mainTile).getLatitude();
            double lng = ((Order) mainTile).getLongitude();
            String label = ((Order) mainTile).getName();

            MapUtil.launchMaps(this, lat, lng, label);
        }
    };

    //================================================================================
    // VIEW DATA ORGANIZER
    //================================================================================
    @Override
    protected void onViewDataChanged() {
        super.onViewDataChanged();

        if(gridAdapter != null) {
            gridAdapter.clear();
            gridAdapter.addAll(mViewData.getTileList());
        }
    }

    private void trySync(boolean forceRefresh) {
        if(!isDataFetched || forceRefresh) {
            if(isSyncLayoutShown) {
                // Do nothing.
            } else {
                ViewExpandCollapseUtil.animateExpanding(vSyncProgressLayout);
                isSyncLayoutShown = true;

                // Check for internet connection
                // Read from SmartStore -> Check if there's any dirty order
                //  yes -> Update it first -> notify user
                // Update order list

                requestOrderList();
                isDataFetched = true;
            }
        } else {
            onViewDataChanged();
        }
    }

    private void loadCachedData() {
        mViewData.setTileList(ModelHandler.OrderRequestor.getStoredOrder());
        onViewDataChanged();
    }

    private void requestOrderList() {
        long startTime = System.currentTimeMillis();

        ModelHandler.OrderRequestor.requestActiveOrder(client, (titleList) -> {
            long diff = MINIMUM_LOADING_TIME - (System.currentTimeMillis() - startTime);
            diff = (diff < 0 ? 0 : diff);

            new Handler().postDelayed(() -> {
                ViewExpandCollapseUtil.animateCollapsing(vSyncProgressLayout);
                isSyncLayoutShown = false;

                mViewData.setTileList(titleList);
                onViewDataChanged();

                Snackbar.make(vMainTileGV, "Order data updated", Snackbar.LENGTH_SHORT).show();
            }, diff);

        }, error -> {
            ViewExpandCollapseUtil.animateCollapsing(vSyncProgressLayout);
            isSyncLayoutShown = false;

            String message = error.getMessage();

            if (error instanceof NoConnectionError) {
                message = getString(R.string.message_no_connection);
            } else if(error instanceof ServerError || error instanceof TimeoutError) {
                message = getString(R.string.message_server_error);
            }

            Snackbar.make(vMainTileGV, message, Snackbar.LENGTH_LONG).show();
        });
    }
}
