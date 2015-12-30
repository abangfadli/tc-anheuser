package topcoder.topcoder.anheuser;

import android.app.Application;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.smartstore.app.SmartStoreSDKManager;

import topcoder.topcoder.anheuser.util.ModelHandler;
import topcoder.topcoder.anheuser.util.SfKeyImplementation;
import topcoder.topcoder.anheuser.view.activity.MainActivity;
import topcoder.topcoder.anheuser.view.activity.SplashActivity;

/**
 * Created by ahmadfadli on 12/27/15.
 */
public class AnheuserApplication extends Application {

    @Override
    public void onCreate() {
        SmartStoreSDKManager.initNative(getApplicationContext(), new SfKeyImplementation(), SplashActivity.class);
        super.onCreate();
        ModelHandler.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
