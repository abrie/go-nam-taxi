package na.nbii.tillapp;

import android.content.SharedPreferences;

/**
 * Created by abrie on 15-10-03.
 */
public class NetPath implements SharedPreferences.OnSharedPreferenceChangeListener {
    private String baseUrl = "http://undefined:xxxx";
    private String serverAddress;
    private String serverPort;
    private String taxiNumber;

    public String getUrl(String path) {
        return String.format("%s%s", baseUrl, path);
    }

    public NetPath(SharedPreferences preferences) {
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
