package topcoder.topcoder.anheuser.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.salesforce.androidsdk.rest.RestClient;

import java.util.ArrayList;

import butterknife.Bind;
import topcoder.topcoder.anheuser.R;
import topcoder.topcoder.anheuser.constant.CommonConstants;
import topcoder.topcoder.anheuser.model.ModelHolder;
import topcoder.topcoder.anheuser.util.ModelHandler;
import topcoder.topcoder.anheuser.util.ViewExpandCollapseUtil;
import topcoder.topcoder.anheuser.view.adapter.GridAdapter;
import topcoder.topcoder.anheuser.view.data.common.Order;
import topcoder.topcoder.anheuser.view.data.main.MainViewData;

public class MainActivity extends BaseActivity<MainViewData> implements AdapterView.OnItemClickListener {

    //================================================================================
    // VIEW OBJECT
    //================================================================================
    @Bind(R.id.gv_main_tile)
    GridView vMainTileGV;

    @Bind(R.id.layout_sync_progress)
    LinearLayout vSyncProgressLayout;

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

        if(!isDataFetched) {
            requestOrderList();
            isDataFetched = true;
        } else {
            onViewDataChanged();
        }

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
                sync();
                Toast.makeText(this, "Sync", Toast.LENGTH_SHORT).show();
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
    }

    protected void initListener() {
        vMainTileGV.setOnItemClickListener(this);
    }

    //================================================================================
    // VIEW DATA ORGANIZER
    //================================================================================
    @Override
    protected void onViewDataChanged() {
        super.onViewDataChanged();

        GridAdapter g = new GridAdapter(this, new ArrayList<>());
        vMainTileGV.setAdapter(g);
        g.clear();

        g.addAll(mViewData.getTileList());
    }

    private void sync() {
        if(isSyncLayoutShown) {
            ViewExpandCollapseUtil.animateCollapsing(vSyncProgressLayout);
            isSyncLayoutShown = false;
            return;
        }


        ViewExpandCollapseUtil.animateExpanding(vSyncProgressLayout);
        isSyncLayoutShown = true;

    }

    private void requestOrderList() {
        ModelHandler.OrderRequestor.requestActiveOrder(client, (titleList) -> {
            mViewData.setTileList(titleList);
            onViewDataChanged();
        }, error -> {
            Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.equals(vMainTileGV)) {
            Order selected = (Order)vMainTileGV.getItemAtPosition(position);
            if(selected != null) {
                if (ModelHandler.setSelectedOrder(selected.getId())) {
                    Intent intent = new Intent(this, OrderDetailActivity.class);
                    startActivity(intent);
                }
            }
        }
    }
}
