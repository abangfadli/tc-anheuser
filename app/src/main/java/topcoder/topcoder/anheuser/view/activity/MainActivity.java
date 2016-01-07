package topcoder.topcoder.anheuser.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.salesforce.androidsdk.rest.RestClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import rx.functions.Action1;
import topcoder.topcoder.anheuser.R;
import topcoder.topcoder.anheuser.constant.CommonConstant;
import topcoder.topcoder.anheuser.constant.PrefConstant;
import topcoder.topcoder.anheuser.util.MapUtil;
import topcoder.topcoder.anheuser.util.ModelHandler;
import topcoder.topcoder.anheuser.util.ViewExpandCollapseUtil;
import topcoder.topcoder.anheuser.util.pref.StandardPrefManager;
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

    /**
     * Flag for full reloading event i.e FullSync (update dirty and reload list from SDK)
     */
    boolean isReloading;
    boolean isDataFetched;

    //================================================================================
    // LIFE CYCLE
    //================================================================================

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(mConnectionReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onAdjustProperties() {
        super.onAdjustProperties();
        mContentViewId = R.layout.activity_main;
        mLeftButtonType = CommonConstant.LeftButton.SYNC;
    }

    @Override
    public void onResume(RestClient client) {
        super.onResume(client);
        // Load cached data to support offline
        loadCachedData();

        if(!isDataFetched) {
            // Check if user already open the app that day.
            // If yes, then do nothing
            // Else, trigger full Sync

            StandardPrefManager prefManager = new StandardPrefManager(this);
            String lastOpened = prefManager.getString(PrefConstant.LAST_OPENED_DATE_KEY, null);
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            String currentDate = sdf.format(date);

            // If day is not same
            if (!currentDate.equals(lastOpened)) {
                trySync(true);
            }

            prefManager.writeString(PrefConstant.LAST_OPENED_DATE_KEY, currentDate);
        }

        // Only trigger DirtyCompletedOrder instead of FullSync
        updateDirtyOrder(false);
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

    /**
     * Init the initial condition of a View
     */
    protected void initViewState() {
        isReloading = false;

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
    /**
     * ItemClick when an item clicked. Invoked by GridAdapter.
     */
    private Action1<MainTile> itemClickListener = mainTile -> {
        if (mainTile instanceof Order) {
            // Set selected Order to become usable in next Activity
            if (ModelHandler.setSelectedOrder(((Order)mainTile).getId())) {
                Intent intent = new Intent(this, OrderDetailActivity.class);
                startActivity(intent);
            }
        } else if(mainTile instanceof Overview) {
            Intent intent = new Intent(this, FullMapActivity.class);
            startActivity(intent);
        }
    };

    /**
     * ActionItemClick. Typically only happens in Order Tile. Not Overview Tile.
     */
    private Action1<MainTile> itemActionButtonClicked = mainTile -> {
        if(mainTile instanceof Order) {

            double lat = ((Order) mainTile).getLatitude();
            double lng = ((Order) mainTile).getLongitude();
            String label = ((Order) mainTile).getName();

            MapUtil.launchMapsAddress(this, lat, lng, ((Order) mainTile).getAddress());
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

    /**
     * Do Sync to Salesforce SDK
     * @param forceRefresh if set to true then still refresh even if the Activity already have data stored
     */
    private void trySync(boolean forceRefresh) {
        if(!isDataFetched || forceRefresh) {
            if(isReloading) {
                // Do nothing.
            } else {
                ViewExpandCollapseUtil.animateExpanding(vSyncProgressLayout);
                isReloading = true;

                // Run DirtyOrderCompleted with reloading the list
                updateDirtyOrder(true);

                isDataFetched = true;
            }
        } else {
            onViewDataChanged();
        }
    }

    /**
     * Update the DirtyOrderCompleted
     * @param needReload if set to true then it'll automatically continue to reloading the list.
     */
    private void updateDirtyOrder(boolean needReload) {
        ModelHandler.OrderRequestor.updateDirtyOrder(client, (total) -> {
            if(total > 0) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(total + " Completed Order(s)");
                alertDialogBuilder.setMessage(getString(R.string.message_sync_back));
                alertDialogBuilder.setOnDismissListener(dialog -> {
                    if(needReload) {
                        requestOrderList();
                    }
                });
                alertDialogBuilder.setPositiveButton(getString(R.string.text_ok), (dialog, which) -> {

                });
                alertDialogBuilder.show();
            } else {
                if(needReload) {
                    requestOrderList();
                }
            }
        }, (error) -> {
            if(needReload) {
                ViewExpandCollapseUtil.animateCollapsing(vSyncProgressLayout);
                isReloading = false;

                showErrorMessage(error);
            }
        });
    }

    /**
     * Load the Order data stored in SmartStore
     */
    private void loadCachedData() {
        mViewData.setTileList(ModelHandler.OrderRequestor.getStoredOrder());
        onViewDataChanged();
    }

    /**
     * Reload the list (Request new data)
     */
    private void requestOrderList() {
        // Mark start time
        long startTime = System.currentTimeMillis();

        ModelHandler.OrderRequestor.requestActiveOrder(client, (tileList) -> {

            // If loading time is < 3 seconds then show the loading animation for 3 seconds.
            // On a very fast internet connection, it'll make the UI ugly because the layout appear and then immediately disappeared
            long diff = MINIMUM_LOADING_TIME - (System.currentTimeMillis() - startTime);
            diff = (diff < 0 ? 0 : diff);

            new Handler().postDelayed(() -> {
                ViewExpandCollapseUtil.animateCollapsing(vSyncProgressLayout);
                isReloading = false;

                mViewData.setTileList(tileList);
                onViewDataChanged();

                Snackbar.make(vMainTileGV, R.string.message_order_loaded, Snackbar.LENGTH_SHORT).show();
            }, diff);

        }, error -> {
            ViewExpandCollapseUtil.animateCollapsing(vSyncProgressLayout);
            isReloading = false;

            showErrorMessage(error);
        });
    }

    /**
     * Show Snackbar based on error type
     * @param error
     */
    private void showErrorMessage(Exception error) {
        String message = error.getMessage();

        if (error instanceof NoConnectionError) {
            message = getString(R.string.message_no_connection);
        } else if(error instanceof ServerError || error instanceof TimeoutError) {
            message = getString(R.string.message_server_error);
        }

        Snackbar.make(vMainTileGV, message, Snackbar.LENGTH_LONG).show();
    }


    private BroadcastReceiver mConnectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

            if(!noConnectivity) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();

                if(netInfo != null && netInfo.isConnected()) {
                    updateDirtyOrder(false);
                }
            }
        }
    };
}
