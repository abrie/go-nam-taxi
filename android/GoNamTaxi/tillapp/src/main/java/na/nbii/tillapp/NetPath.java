package na.nbii.tillapp;

import android.content.SharedPreferences;

/**
 * Created by abrie on 15-10-03.
 */
public class NetPath implements SharedPreferences.OnSharedPreferenceChangeListener {
    private String baseUrl = "http://undefined:xxxx";
    private String serverAddress;
    private String serverPort;

    public String getUrl(String path) {
        return String.format("%s%s", baseUrl, path);
    }

    public NetPath(String serverAddress, String serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        buildBaseUrl();
    }

    public void buildBaseUrl() {
        baseUrl = String.format("http://%s:%s", serverAddress, serverPort);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(SettingsActivity.SERVER_ADDRESS)) {
            serverAddress = sharedPreferences.getString(key, "undefined");
        }
        else if (key.equals(SettingsActivity.SERVER_PORT)) {
            serverPort = sharedPreferences.getString(key, "undefined");
        }

        buildBaseUrl();
    }
}
