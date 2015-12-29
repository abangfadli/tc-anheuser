package topcoder.topcoder.anheuser.util;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;

import rx.functions.Action;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action2;

/**
 * Created by ahmadfadli on 12/29/15.
 */
public class ActionUtil {
    public static void tryCall(Action0 action) {
        if(action != null) {
            action.call();
        }
    }

    public static <T> void tryCall(Action1<T> action1, T object) {
        if(action1 != null) {
            action1.call(object);
        }
    }

    public static <T, R> void tryCall(Action2<T, R> action2, T obj1, R obj2) {
        if(action2 != null) {
            action2.call(obj1, obj2);
        }
    }
}
