package topcoder.topcoder.anheuser;

import android.app.Application;

import com.salesforce.androidsdk.app.SalesforceSDKManager;

import topcoder.topcoder.anheuser.util.ModelHandler;
import topcoder.topcoder.anheuser.util.SfKeyImplementation;
import topcoder.topcoder.anheuser.view.activity.MainActivity;

/**
 * Created by ahmadfadli on 12/27/15.
 */
public class AnheuserApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SalesforceSDKManager.initNative(getApplicationContext(), new SfKeyImplementation(), MainActivity.class);
        ModelHandler.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
