package na.nbii.tillapp;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by abrie on 15-10-03.
 */
public class SettingsActivity extends Activity {
    public static String SERVER_ADDRESS = "pref_server_address";
    public static String SERVER_PORT = "pref_server_port";
    public static String TAXI_NUMBER = "pref_taxi_number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

    }


}
