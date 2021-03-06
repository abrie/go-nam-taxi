package backend;

import android.content.SharedPreferences;

import tillapp.SettingsActivity;

/**
 * Created by abrie on 15-10-03.
 */
public class PathBuilder implements SharedPreferences.OnSharedPreferenceChangeListener {
    private String baseUrl = "http://undefined:xxxx";
    private String serverAddress;
    private String serverPort;
    private String taxiNumber;

    public String getUrl(String path) {
        return String.format("%s%s", baseUrl, path);
    }

    public String couponUrl(String rawValue, double lon, double lat) {
        return getUrl(String.format(
                "/till/received/coupon/%s/%s/%f/%f", taxiNumber, rawValue, lon, lat));
    }

    public String cashUrl(double lon, double lat) {
        return getUrl(String.format("/till/received/cash/%s/%f/%f", taxiNumber, lon, lat));
    }

    public PathBuilder(SharedPreferences preferences) {
        onSharedPreferenceChanged(preferences, SettingsActivity.SERVER_ADDRESS);
        onSharedPreferenceChanged(preferences, SettingsActivity.SERVER_PORT);
        onSharedPreferenceChanged(preferences, SettingsActivity.TAXI_NUMBER);
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void buildBaseUrl() {
        baseUrl = String.format("http://%s:%s", serverAddress, serverPort);
    }

    public String getTaxiNumber() {
        return taxiNumber;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(SettingsActivity.SERVER_ADDRESS)) {
            serverAddress = sharedPreferences.getString(key, "undefined");
        }
        else if (key.equals(SettingsActivity.SERVER_PORT)) {
            serverPort = sharedPreferences.getString(key, "undefined");
        }
        else if (key.equals(SettingsActivity.TAXI_NUMBER)) {
            taxiNumber = sharedPreferences.getString(key, "undefined");
        }

        buildBaseUrl();
    }
}
