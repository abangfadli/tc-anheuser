package topcoder.topcoder.anheuser.constant;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by ahmadfadli on 12/27/15.
 */
public class CommonConstant {

    private CommonConstant(){}

    @IntDef(value = {LeftButton.NONE, LeftButton.SYNC, LeftButton.BACK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LeftButtonType {}

    public static class LeftButton {
        public static final int NONE = 0;
        public static final int SYNC = 1;
        public static final int BACK = 2;
    }

    @StringDef(value = {OrderStatus.DRAFT, OrderStatus.ACTIVATED, OrderStatus.COMPLETED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrderStatusMark {}

    public static class OrderStatus {
        public static final String DRAFT = "DRAFT";
        public static final String ACTIVATED = "ACTIVATED";
        public static final String COMPLETED = "COMPLETED";

        public static boolean isDraft(String status) {
            return DRAFT.equalsIgnoreCase(status);
        }

        public static boolean isActivated(String status) {
            return ACTIVATED.equalsIgnoreCase(status);
        }

        public static boolean isCompleted(String status) {
            return COMPLETED.equalsIgnoreCase(status);
        }
    }
}
