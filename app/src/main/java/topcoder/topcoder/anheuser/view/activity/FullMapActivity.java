package topcoder.topcoder.anheuser.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.salesforce.androidsdk.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.functions.Action1;
import topcoder.topcoder.anheuser.R;
import topcoder.topcoder.anheuser.constant.CommonConstants;
import topcoder.topcoder.anheuser.model.ModelHolder;
import topcoder.topcoder.anheuser.util.MapUtil;
import topcoder.topcoder.anheuser.util.ModelHandler;
import topcoder.topcoder.anheuser.util.ViewExpandCollapseUtil;
import topcoder.topcoder.anheuser.view.adapter.GridAdapter;
import topcoder.topcoder.anheuser.view.data.common.Coordinate;
import topcoder.topcoder.anheuser.view.data.common.Order;
import topcoder.topcoder.anheuser.view.data.fullmap.FullMapViewData;
import topcoder.topcoder.anheuser.view.data.main.MainTile;
import topcoder.topcoder.anheuser.view.data.main.MainViewData;
import topcoder.topcoder.anheuser.view.data.main.Overview;
import topcoder.topcoder.anheuser.view.data.orderdetail.OrderDetailViewData;

public class FullMapActivity extends BaseActivity<FullMapViewData> implements OnMapReadyCallback {

    //================================================================================
    // VIEW OBJECT
    //================================================================================
    SupportMapFragment vSupportMapFragment;

    GoogleMap mGoogleMap;
    List<Marker> markerList;


    //================================================================================
    // LIFE CYCLE
    //================================================================================
    @Override
    protected void onAdjustProperties() {
        super.onAdjustProperties();
        mContentViewId = R.layout.activity_full_map;
        mLeftButtonType = CommonConstants.LeftButton.BACK;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vSupportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        vSupportMapFragment.getMapAsync(this);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onResume(RestClient client) {
        super.onResume(client);
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
    protected void initViewState() {
        FullMapViewData viewData = new FullMapViewData();
        viewData.setOrderList(ModelHandler.OrderRequestor.getOrderList());
        setViewData(viewData);
    }

    protected void initListener() {
    }

    //================================================================================
    // LISTENER
    //================================================================================

    private Action1<MainTile> itemActionButtonClicked = mainTile -> {
        if(mainTile instanceof Order) {

            double lat = -6.9932000;
            double lng = 110.4203000;
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
        updateMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        updateMap();
    }

    private void updateMap() {
        if(mViewData == null || mGoogleMap == null || mViewData.getOrderList().size() == 0) {
            return;
        }

        markerList = new ArrayList<>();

        for(int i = 0; i < mViewData.getOrderList().size(); i++) {
            Order order = mViewData.getOrderList().get(i);

            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(order.getLatitude(), order.getLongitude()))
                    .title(order.getName()));

            marker.showInfoWindow();
            markerList.add(marker);
        }

        mGoogleMap.setOnMarkerClickListener(marker -> {
            int index = markerList.indexOf(marker);
            if(index != -1) {
                ModelHandler.setSelectedOrder(mViewData.getOrderList().get(index).getId());
                Intent intent = new Intent(this, OrderDetailActivity.class);
                startActivity(intent);
            }

            return false;
        });

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markerList) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        mGoogleMap.setOnMapLoadedCallback(() -> mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20)));

    }
}
