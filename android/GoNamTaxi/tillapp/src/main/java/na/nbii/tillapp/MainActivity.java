package na.nbii.tillapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;

import backend.Backend;
import backend.NetPath;
import na.nbii.netcomm.NetRequestQueue;
import vision.BarcodeCaptureActivity;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int RC_BARCODE_CAPTURE = 9001;
    private Backend backend;

    ArrayList<String> listItems=new ArrayList<>();
    ArrayAdapter<String> adapter;
    SoundPool soundPool;
    int successSound;
    int failureSound;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            Log.e("TillApp","Null Intent returned to onActivityResult");
            return;
        }

        Bundle bundle = data.getExtras();
        Barcode barcode = (Barcode) bundle.get(BarcodeCaptureActivity.BarcodeObject);
        final String rawValue;
        
        if (barcode != null) {
            rawValue = barcode.rawValue;
        }
        else {
            rawValue = "error in barcode";
        }

        if (requestCode == RC_BARCODE_CAPTURE) {
            backend.validateCoupon(rawValue, new Backend.CouponValidationResultHandler() {
                @Override
                public void onCouponValidationResult(boolean isValid) {
                    if (isValid) {
                        adapter.insert("Ticket Validated:" + rawValue, 0);
                        soundPool.play(successSound, 1, 1, 0, 0, 1);
                    } else {
                        adapter.insert("TICKET NOT VALID:" + rawValue, 0);
                        soundPool.play(failureSound, 1, 1, 0, 0, 1);
                    }
                }

                @Override
                public void onCouponValidationError(String error) {
                    ShowError(error);
                }
            });
        }
    }

    public void ShowError(String errorMessage) {
        View view = findViewById(R.id.listView);
        Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        backend = new Backend(NetRequestQueue.getInstance(this), new NetPath(sharedPref));
        sharedPref.registerOnSharedPreferenceChangeListener(this);
        onSharedPreferenceChanged(sharedPref, SettingsActivity.TAXI_NUMBER);

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        failureSound = soundPool.load(this, R.raw.failure, 1);
        successSound = soundPool.load(this, R.raw.success, 2);

        Button cashButton = (Button)findViewById(R.id.btn_pay_cash);
        cashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                backend.submitCash(new Backend.CashTransactionResultHandler() {
                    @Override
                    public void onCashTransactionResult(boolean isValid) {
                        adapter.insert("cash payment acknowledged", 0);
                    }

                    @Override
                    public void onCashTransactionError(String error) {
                        ShowError(error);
                    }
                });
            }
        });

        Button ticketButton = (Button)findViewById(R.id.btn_scan_ticket);
        ticketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // launch barcode activity.
                Intent intent = new Intent(
                        getApplication().getApplicationContext(),
                        BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);

            }
        });

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    private void setToolbarTitle(String taxiNumber) {
        String completeTitle = String.format("Till for Taxi %s", taxiNumber);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(completeTitle);
        }
        else {
            toolbar.setTitle(completeTitle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClassName(this, "na.nbii.tillapp.SettingsActivity");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(SettingsActivity.TAXI_NUMBER)) {
            String taxiNumber = sharedPreferences.getString(key, "undefined");
            setToolbarTitle(taxiNumber);
        }
    }
}
