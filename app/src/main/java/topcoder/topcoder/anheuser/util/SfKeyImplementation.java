package topcoder.topcoder.anheuser.util;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.security.Encryptor;

/**
 * Created by ahmadfadli on 12/29/15.
 */
public class SfKeyImplementation implements SalesforceSDKManager.KeyInterface{
    @Override
    public String getKey(String name) {
        return Encryptor.hash(name + "12s9adpahk;n12-97sdainkasd=012", name + "12kl0dsakj4-cxh1qewkjasdol8");
    }
}
