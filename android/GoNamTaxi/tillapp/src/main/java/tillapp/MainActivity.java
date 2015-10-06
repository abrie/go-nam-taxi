package tillapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.vision.barcode.Barcode;

import backend.Backend;
import backend.PathBuilder;
import na.nbii.netcomm.NetRequestQueue;
import na.nbii.tillapp.R;
import vision.BarcodeCaptureActivity;

public class MainActivity extends AppCompatActivity {

    private Backend backend;
    private TransactionLog transactionLog;
    private SoundEffects soundEffects;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (intent != null && requestCode == BarcodeCaptureActivity.RC_BARCODE_CAPTURE) {
            final Bundle bundle = intent.getExtras();
            final String couponCode = getRawBarcodeContents(bundle);
            if (couponCode != null) {
                validateCouponCode(couponCode);
            }
        }
    }

    private void startLocationListener() {
        LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, backend);
    }

    private void validateCouponCode(final String couponCode) {
        backend.validateCoupon(couponCode, new Backend.CouponTransactionResultHandler() {
            @Override
            public void onCouponValidationResult(boolean isValid, long age, long timeStamp) {
                if (isValid) {
                    transactionLog.logValidCoupon(timeStamp);
                    soundEffects.signalSuccess();
                } else {
                    transactionLog.logInvalidCoupon(age, timeStamp );
                    soundEffects.signalError();
                }
            }

            @Override
            public void onCouponValidationError(String error) {
                showError(error);
            }
        });
    }

    public String getRawBarcodeContents(Bundle bundle) {
        Barcode barcode = (Barcode) bundle.get(BarcodeCaptureActivity.BarcodeObject);

        if (barcode != null) {
            return barcode.rawValue;
        }
        else {
            return null;
        }
    }

    public void showError(String errorMessage) {
        View view = findViewById(R.id.content_main);
        Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        setToolbarTitle();

        backend = new Backend(NetRequestQueue.getInstance(this), new PathBuilder(sharedPref));
        soundEffects = new SoundEffects(this);
        transactionLog = new TransactionLog(this);
        startLocationListener();

        connectListview();
        connectButtons();
    }

    private void connectListview() {
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(transactionLog.getAdapter());
    }

    private void connectButtons() {
        Button cashButton = (Button)findViewById(R.id.btn_pay_cash);
        cashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                backend.submitCash(new Backend.CashTransactionResultHandler() {
                    @Override
                    public void onCashTransactionResult(long timeStamp) {
                        transactionLog.logCash(timeStamp);
                    }

                    @Override
                    public void onCashTransactionError(String error) {
                        showError(error);
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
                startActivityForResult(intent, BarcodeCaptureActivity.RC_BARCODE_CAPTURE);

            }
        });
    }

    private void setToolbarTitle() {
        String completeTitle = String.format("Till for Taxi");
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
            intent.setClassName(this, "tillapp.SettingsActivity");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
