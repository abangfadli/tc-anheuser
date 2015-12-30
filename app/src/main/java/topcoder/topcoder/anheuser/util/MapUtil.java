package topcoder.topcoder.anheuser.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by ahmadfadli on 12/29/15.
 */
public class MapUtil {

    /**
     * Launch GoogleMaps Intent
     * @param activity current Activity
     * @param lat Latitude of destination
     * @param lng Longitude of destination
     * @param label Label of the place
     */
    public static void launchMaps(Activity activity, double lat, double lng, String label) {
        String uriBegin = String.format("geo:%s,%s", lat, lng);
        String query = String.format("%s,%s(%s)", lat, lng, label);
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery;
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
    }

    public static void launchMapsAddress(Activity activity, double lat, double lng, String queryAddress) {
        String uriBegin = String.format("geo:%s,%s", lat, lng);
        String query = String.format("%s", queryAddress);
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery;
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
    }
}
