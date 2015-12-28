package topcoder.topcoder.anheuser.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by ahmadfadli on 12/27/15.
 */
public class CommonConstants {

    private CommonConstants(){}

    @IntDef(value = {LeftButton.NONE, LeftButton.SYNC, LeftButton.BACK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LeftButtonType {}

    public static class LeftButton {
        public static final int NONE = 0;
        public static final int SYNC = 1;
        public static final int BACK = 2;
    }
}
