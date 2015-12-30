package topcoder.topcoder.anheuser.view.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.ui.SalesforceActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import topcoder.topcoder.anheuser.R;
import topcoder.topcoder.anheuser.constant.CommonConstant;
import topcoder.topcoder.anheuser.view.data.BaseViewData;

/**
 * Created by ahmadfadli on 12/27/15.
 */
public abstract class BaseActivity<VD extends BaseViewData> extends SalesforceActivity {

    protected final String TAG = this.getClass().getSimpleName();


    //================================================================================
    // ACTIVITY PROPERTIES
    //================================================================================

    @LayoutRes
    protected int mContentViewId;

    @CommonConstant.LeftButtonType
    protected int mLeftButtonType;

    //================================================================================
    // VIEW OBJECT
    //================================================================================
    @Bind(R.id.toolbar)
    Toolbar vToolbar;

    //================================================================================
    // INSTANCE OBJECT
    //================================================================================
    protected RestClient client;
    protected VD mViewData;

    //================================================================================
    // LIFE CYCLE
    //================================================================================
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Adjust properties of the activity (layout, back button type, etc)
        onAdjustProperties();
        setContentView(mContentViewId);
        ButterKnife.bind(this);

        initViewState();
        initListener();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setupToolbar();
    }

    /**
     * Set the properties of the activity (ContentView layout resId, back button type, etc)
     */
    protected void onAdjustProperties() {
        // Default State
        mContentViewId = R.layout.activity_main;
        mLeftButtonType = CommonConstant.LeftButton.SYNC;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onResume(RestClient client) {
        this.client = client;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() != android.R.id.home) {
            // Two buttons in app bar is not implemented yet
            Snackbar snackbar =  Snackbar.make(vToolbar, R.string.message_not_implemented, Snackbar.LENGTH_SHORT);
            snackbar.setAction(R.string.text_close, v -> {
                snackbar.dismiss();
            });
            snackbar.show();
        }
        return super.onOptionsItemSelected(item);
    }

    //================================================================================
    // SET UPS
    //================================================================================

    /**
     * Init View State (default state, visibility, variables of an activity screen
     */
    protected abstract void initViewState();

    /**
     * Init listener between view and activitiy
     */
    protected abstract void initListener();

    /**
     * Setup top left button and icon in toolbar
     */
    private void setupToolbar() {
        if(vToolbar != null) {
            switch (mLeftButtonType) {
                case CommonConstant.LeftButton.BACK:
                    vToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
                    break;
                case CommonConstant.LeftButton.NONE:
                    break;
                case CommonConstant.LeftButton.SYNC:
                    vToolbar.setNavigationIcon(R.drawable.ic_sync_black_24dp);
                    break;
            }

            vToolbar.setTitle("");
            setSupportActionBar(vToolbar);
        }
    }

    /**
     * Set ViewData binded with the Activity.
     * @param viewData the binded object
     */
    protected void setViewData(VD viewData) {
        this.mViewData = viewData;
        onViewDataChanged();
    }

    //================================================================================
    // GENERIC EVENTS
    //================================================================================
    public void onLogout() {
        SalesforceSDKManager.getInstance().logout(this);
    }


    /**
     * Would be overridden with each Activity to implement changes whenever ViewData changed/replaced
     */
    protected void onViewDataChanged() {

    }
}
