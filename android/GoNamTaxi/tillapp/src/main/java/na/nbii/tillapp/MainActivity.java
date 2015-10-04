package na.nbii.tillapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.vision.barcode.Barcode;

import na.nbii.netcomm.NetMethods;
import na.nbii.netcomm.NetRequestQueue;
import vision.BarcodeCaptureActivity;

public class MainActivity extends AppCompatActivity {
    private static final int RC_BARCODE_CAPTURE = 9001;
    private NetPath netPath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            Log.e("TillApp","Null Intent returned to onActivityResult");
            return;
        }

        Bundle bundle = data.getExtras();
        Barcode barcode = (Barcode) bundle.get(BarcodeCaptureActivity.BarcodeObject);
        String rawValue = "null!";
        
        if (barcode != null) {
            rawValue = barcode.rawValue;
        }

        if (requestCode == RC_BARCODE_CAPTURE) {
            NetRequestQueue.getInstance(getApplicationContext())
                    .addRequest(NetMethods.stringRequest(
                            netPath.getUrl("/till/received/coupon/"+rawValue),
                            new NetMethods.StringResponseHandler() {
                                @Override
                                public void onString(String content) {
                                }

                                @Override
                                public void onError(String error) {
                                }
                            }
                    ));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String serverAddress = sharedPref.getString(SettingsActivity.SERVER_ADDRESS, "undefined");
        String serverPort = sharedPref.getString(SettingsActivity.SERVER_PORT, "undefined");
        netPath = new NetPath(serverAddress, serverPort);
        sharedPref.registerOnSharedPreferenceChangeListener(netPath);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.cash_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                NetRequestQueue.getInstance(getApplicationContext())
                        .addRequest(NetMethods.stringRequest(
                                netPath.getUrl("/till/received/cash"),
                                new NetMethods.StringResponseHandler() {
                                    @Override
                                    public void onString(String content) {
                                        Snackbar.make(view, content, Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }

                                    @Override
                                    public void onError(String error) {
                                        Snackbar.make(view, error, Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();

                                    }
                                }
                        ));
            }
        });

        FloatingActionButton coupon = (FloatingActionButton) findViewById(R.id.coupon_button);
        coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // launch barcode activity.
                Intent intent = new Intent(getApplication().getApplicationContext(), BarcodeCaptureActivity.class);
                //intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked());
                //intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                intent.putExtra(BarcodeCaptureActivity.UseFlash, false);

                startActivityForResult(intent, RC_BARCODE_CAPTURE);

            }
        });

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
}
