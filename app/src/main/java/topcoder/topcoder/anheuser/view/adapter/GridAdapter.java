package topcoder.topcoder.anheuser.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;
import topcoder.topcoder.anheuser.R;
import topcoder.topcoder.anheuser.constant.CommonConstant;
import topcoder.topcoder.anheuser.util.ActionUtil;
import topcoder.topcoder.anheuser.view.data.common.Order;
import topcoder.topcoder.anheuser.view.data.main.MainTile;
import topcoder.topcoder.anheuser.view.data.main.Overview;

/**
 * Created by ahmadfadli on 12/29/15.
 */
public class GridAdapter extends ArrayAdapter<MainTile> implements OnMapReadyCallback {

    private static final int TYPE_OVERVIEW = 0;
    private static final int TYPE_ORDER = 1;


    private static final int NO_PREDIFINED_LAYOUT = 0;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private MainTileViewHolder mMainTileViewHolder;
    private Action1<MainTile> mItemClickListener;
    private Action1<MainTile> mActionItemClickListener;

    public GridAdapter (Context context, List<MainTile> provinceList) {
        super(context, NO_PREDIFINED_LAYOUT, provinceList);

        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MainTile current = getItem(position);

        if(convertView == null) {
            if(current instanceof Order) {
                convertView = mLayoutInflater.inflate(R.layout.item_grid_main_tile, parent, false);
                mMainTileViewHolder = new OrderViewHolder(mContext, convertView);
            } else if (current instanceof Overview) {
                convertView = mLayoutInflater.inflate(R.layout.item_grid_overview_main_tile, parent, false);
                mMainTileViewHolder = new OverviewViewHolder(mContext, convertView);
            }

            if (convertView != null) {
                convertView.setTag(mMainTileViewHolder);
            }
        } else {
            mMainTileViewHolder = (MainTileViewHolder)convertView.getTag();
        }

        if(current instanceof Order) {
            ((OrderViewHolder)mMainTileViewHolder).init((Order)current);
            ((OrderViewHolder) mMainTileViewHolder).vTileTitleTextView.setOnClickListener(v -> {
                ActionUtil.tryCall(mItemClickListener, current);
            });
            ((OrderViewHolder) mMainTileViewHolder).vTileContentLayout.setOnClickListener(v -> {
                ActionUtil.tryCall(mItemClickListener, current);
            });

            ((OrderViewHolder) mMainTileViewHolder).vTileActionImageView.setOnClickListener(v -> {
                ActionUtil.tryCall(mActionItemClickListener, current);
            });
        } else if(current instanceof Overview) {
            ((OverviewViewHolder)mMainTileViewHolder).init((Overview)current);
            ((OverviewViewHolder) mMainTileViewHolder).vTileTitleTextView.setOnClickListener(v -> {
                ActionUtil.tryCall(mItemClickListener, current);
            });
            ((OverviewViewHolder) mMainTileViewHolder).vTileContentLayout.setOnClickListener(v -> {
                ActionUtil.tryCall(mItemClickListener, current);
            });




//            ((OverviewViewHolder) mMainTileViewHolder)ActionUtil.vMapView.getMapAsync(this);
        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(getItem(position) instanceof Overview) {
            return TYPE_OVERVIEW;
        } else {
            return TYPE_ORDER;
        }
    }

    public void setOnItemClicked(Action1<MainTile> selectedItem) {
        mItemClickListener = selectedItem;
    }

    public void setOnItemActionClicked(Action1<MainTile> selectedItem) {
        mActionItemClickListener = selectedItem;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
    }

    public abstract class MainTileViewHolder<T extends MainTile> {
        View rootView;
        T bindedObject;
        Context mContext;

        public MainTileViewHolder(Context context, View view) {
            ButterKnife.bind(this, view);
            mContext = context;
        }

        public abstract void init(T item);

        public View getRootView() {
            return rootView;
        }
    }


    public class OrderViewHolder extends MainTileViewHolder<Order> {

        @Bind(R.id.layout_tile_content)
        ViewGroup vTileContentLayout;

        @Bind(R.id.tv_order_name)
        TextView vOrderNameTextView;
        @Bind(R.id.tv_order_address)
        TextView vOrderAddressTextView;

        @Bind(R.id.tv_tile_title)
        TextView vTileTitleTextView;

        @Bind(R.id.iv_tile_action)
        ImageView vTileActionImageView;

        public OrderViewHolder(Context context, View view) {
            super(context, view);
        }

        @Override
        public void init(Order item) {
            bindedObject = item;
            vOrderNameTextView.setText(item.getName());
            vOrderAddressTextView.setText(item.getAddress());

            if(CommonConstant.OrderStatus.isCompleted(item.getStatus())) {
                vTileTitleTextView.setText(mContext.getString(R.string.completed));
                vTileActionImageView.setVisibility(View.GONE);
            } else {
                vTileTitleTextView.setText(mContext.getString(R.string.view_order));
                vTileActionImageView.setVisibility(View.VISIBLE);
            }
        }
    }


    public class OverviewViewHolder extends MainTileViewHolder<Overview> {

//        @Bind(R.id.map_view)
//        MapView vMapView;

        @Bind(R.id.layout_tile_content)
        ViewGroup vTileContentLayout;

        @Bind(R.id.tv_tile_title)
        TextView vTileTitleTextView;

        public OverviewViewHolder(Context context, View view) {
            super(context, view);
        }

        @Override
        public void init(Overview item) {
            bindedObject = item;
        }
    }
}
