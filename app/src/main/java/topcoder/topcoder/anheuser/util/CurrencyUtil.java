package topcoder.topcoder.anheuser.util;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by ahmadfadli on 12/29/15.
 */
public class CurrencyUtil {

    /**
     * Convert an amount to a currency in a locale
     * @param currency
     * @param locale
     * @param amount
     * @return String of formatted amount
     */
    public static String formatCurrency(String currency, Locale locale, double amount) {

        DecimalFormat decimalFormat;
        decimalFormat = (DecimalFormat) DecimalFormat.getInstance(locale);
        decimalFormat.setCurrency(Currency.getInstance(currency));

        return decimalFormat.getCurrency().getSymbol() + "" + decimalFormat.format(amount);
    }
}
