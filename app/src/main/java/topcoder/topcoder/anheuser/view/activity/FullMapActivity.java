package topcoder.topcoder.anheuser.view.activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.salesforce.androidsdk.rest.RestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;
import topcoder.topcoder.anheuser.R;
import topcoder.topcoder.anheuser.constant.CommonConstant;
import topcoder.topcoder.anheuser.util.MapUtil;
import topcoder.topcoder.anheuser.util.ModelHandler;
import topcoder.topcoder.anheuser.view.data.common.Order;
import topcoder.topcoder.anheuser.view.data.fullmap.FullMapViewData;
import topcoder.topcoder.anheuser.view.data.main.MainTile;

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
        mLeftButtonType = CommonConstant.LeftButton.BACK;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vSupportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        vSupportMapFragment.getMapAsync(this);

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

    /**
     * Updating Map.
     * Inserting Markers based on each Order.
     */
    private void updateMap() {
        // If viewData is not ready, or GoogleMap is not ready, return
        if(mViewData == null || mGoogleMap == null || mViewData.getOrderList().size() == 0) {
            return;
        }

        markerList = new ArrayList<>();

        // Add Marker
        for(int i = 0; i < mViewData.getOrderList().size(); i++) {
            Order order = mViewData.getOrderList().get(i);

            Geocoder geocoder = new Geocoder(this);
            List<Address> addresses = new ArrayList<>();
            if (mViewData.getOrderList().get(i).getAddress() != null) {
                try {
                    addresses = geocoder.getFromLocationName(mViewData.getOrderList().get(i).getAddress(), 2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            double latitude = order.getLatitude();
            double longitude = order.getLongitude();
            if(addresses.size() > 0) {
                latitude = addresses.get(0).getLatitude();
                longitude = addresses.get(0).getLongitude();
            }

            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(order.getName()));

            marker.showInfoWindow();
            markerList.add(marker);
        }

        // Set Marker OnClickListener
        mGoogleMap.setOnMarkerClickListener(marker -> {
            int index = markerList.indexOf(marker);
            if(index != -1) {
                ModelHandler.setSelectedOrder(mViewData.getOrderList().get(index).getId());
                Intent intent = new Intent(this, OrderDetailActivity.class);
                startActivity(intent);
            }

            return false;
        });

        // Update Map Position to cover all the markers in one screen
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markerList) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        mGoogleMap.setOnMapLoadedCallback(() -> mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100)));

    }
}
