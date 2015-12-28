package topcoder.topcoder.anheuser.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import topcoder.topcoder.anheuser.R;
import topcoder.topcoder.anheuser.constant.CommonConstants;
import topcoder.topcoder.anheuser.view.data.BaseViewData;

/**
 * Created by ahmadfadli on 12/27/15.
 */
public abstract class BaseActivity<VD extends BaseViewData> extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();


    //================================================================================
    // ACTIVITY PROPERTIES
    //================================================================================
    protected int mContentViewId;
    protected int mLeftButtonType;

    //================================================================================
    // VIEW OBJECT
    //================================================================================
    @Bind(R.id.toolbar)
    Toolbar vToolbar;

    //================================================================================
    // INSTANCE OBJECT
    //================================================================================
    protected VD mViewData;

    //================================================================================
    // LIFE CYCLE
    //================================================================================
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onAdjustProperties();
        setContentView(mContentViewId);
        ButterKnife.bind(this);

        initState();
        initListener();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setupToolbar();
    }


    protected void onAdjustProperties() {
        // Default State
        mContentViewId = R.layout.activity_main;
        mLeftButtonType = CommonConstants.LeftButton.SYNC;
    }

    /*---------------------------------------------------------------------
     |  SET UPS
     *-------------------------------------------------------------------*/

    protected abstract void initState();
    protected abstract void initListener();

    private void setupToolbar() {
        if(vToolbar != null) {
            switch (mLeftButtonType) {
                case CommonConstants.LeftButton.BACK:
                    vToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
                    break;
                case CommonConstants.LeftButton.NONE:
                    break;
                case CommonConstants.LeftButton.SYNC:
                    vToolbar.setNavigationIcon(R.drawable.ic_sync_black_24dp);
                    break;
            }

            setSupportActionBar(vToolbar);
            vToolbar.setTitle("");
        }
    }

    protected void setViewData(VD viewData) {
        this.mViewData = viewData;
        onViewDataChanged();
    }

    protected void onViewDataChanged() {
        // Implement if needed
    }
}
