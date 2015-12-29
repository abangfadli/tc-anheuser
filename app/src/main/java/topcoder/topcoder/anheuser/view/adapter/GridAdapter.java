package topcoder.topcoder.anheuser.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import topcoder.topcoder.anheuser.R;
import topcoder.topcoder.anheuser.constant.CommonConstants;
import topcoder.topcoder.anheuser.view.data.common.Order;
import topcoder.topcoder.anheuser.view.data.main.MainTile;

/**
 * Created by ahmadfadli on 12/29/15.
 */
public class GridAdapter extends ArrayAdapter<MainTile> {

    private static final int NO_PREDIFINED_LAYOUT = 0;

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
        if(getItem(position) instanceof Order) {
            if(convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.item_grid_main_tile, parent, false);
                mViewHolder = new ViewHolder(mContext, convertView);

                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder)convertView.getTag();
            }

            mViewHolder.init((Order)getItem(position));
        }

        return convertView;
    }

    public static class ViewHolder {
        View rootView;

        @Bind(R.id.tv_order_name)
        TextView vOrderNameTV;
        @Bind(R.id.tv_order_address)
        TextView vOrderAddressTV;

        @Bind(R.id.tv_tile_title)
        TextView vTileTitleTV;


        public ViewHolder(Context context, View view) {
            ButterKnife.bind(this, view);
        }

        public void init(Order order) {
            if(CommonConstants.OrderStatus.isCompleted(order.getStatus())) {
                // TODO Completed layout
            } else {
                vOrderNameTV.setText(order.getName());
                vOrderAddressTV.setText(order.getAddress());
            }
        }

        public View getRootView() {
            return rootView;
        }
    }
}
