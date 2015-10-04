package na.nbii.tillapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

/**
 * Created by abrie on 15-10-03.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        onSharedPreferenceChanged(sp, SettingsActivity.SERVER_ADDRESS);
        onSharedPreferenceChanged(sp, SettingsActivity.SERVER_PORT);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Change summary
        if (key.equals(SettingsActivity.SERVER_ADDRESS)) {
            Preference username = findPreference(key);
            username.setSummary(sharedPreferences.getString(key, "undefined"));
        }
        else if (key.equals(SettingsActivity.SERVER_PORT)) {
            Preference username = findPreference(key);
            username.setSummary(sharedPreferences.getString(key, "undefined"));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}
