package topcoder.topcoder.anheuser.util;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by ahmadfadli on 12/29/15.
 */
public class CurrencyUtil {
    public static String formatCurrency(String currency, Locale locale, double amount) {

        DecimalFormat decimalFormat;
        decimalFormat = (DecimalFormat) DecimalFormat.getInstance(locale);
        decimalFormat.setCurrency(Currency.getInstance(currency));

        return decimalFormat.format(amount);
    }
}
