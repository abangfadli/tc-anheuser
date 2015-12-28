package topcoder.topcoder.anheuser.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import topcoder.topcoder.anheuser.R;
import topcoder.topcoder.anheuser.constant.CommonConstants;
import topcoder.topcoder.anheuser.util.ViewExpandCollapseUtil;
import topcoder.topcoder.anheuser.view.data.main.MainTile;
import topcoder.topcoder.anheuser.view.data.main.MainViewData;
import topcoder.topcoder.anheuser.view.data.common.Order;

public class MainActivity extends BaseActivity<MainViewData> implements AdapterView.OnItemClickListener {

    //================================================================================
    // VIEW OBJECT
    //================================================================================
    @Bind(R.id.gv_main_tile)
    GridView vMainTileGV;

    @Bind(R.id.layout_sync_progress)
    LinearLayout vSyncProgressLayout;

    boolean isSyncLayoutShown;

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
                sync();
                Toast.makeText(this, "Sync", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //================================================================================
    // INIT
    //================================================================================
    protected void initState() {
        isSyncLayoutShown = false;
        setViewData(new MainViewData());
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

    //================================================================================
    // DUMMY DATA GENERATOR
    //================================================================================
    private void generateDummy() {

        mViewData.getTileList().add(new Order());
        mViewData.getTileList().add(new Order());
        mViewData.getTileList().add(new Order());
        mViewData.getTileList().add(new Order());
        mViewData.getTileList().add(new Order());
        mViewData.getTileList().add(new Order());
        mViewData.getTileList().add(new Order());

        onViewDataChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.equals(vMainTileGV)) {
            Intent intent = new Intent(this, OrderDetailActivity.class);
            startActivity(intent);
        }
    }


    public static class GridAdapter extends ArrayAdapter<MainTile> {

        private static final int NO_PREDIFINED_LAYOUT = 0;
        private static final int VIEW_HOLDER_TAG = 5;

        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private ViewHolder mViewHolder;

        public GridAdapter (Context context, List<MainTile> provinceList) {
            super(context, NO_PREDIFINED_LAYOUT, provinceList);

            mContext = context;
            mLayoutInflater = LayoutInflater.from(mContext);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.item_grid_main_tile, parent, false);
                mViewHolder = new ViewHolder();

                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder)convertView.getTag();
            }

            return convertView;
        }

        private class ViewHolder {

        }
    }
}
