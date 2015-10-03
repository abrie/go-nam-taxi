package na.nbii.tillapp;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by abrie on 15-10-03.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
