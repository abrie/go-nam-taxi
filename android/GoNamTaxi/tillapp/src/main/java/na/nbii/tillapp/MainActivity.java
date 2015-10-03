package na.nbii.tillapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import na.nbii.netcomm.NetMethods;
import na.nbii.netcomm.NetRequestQueue;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.cash_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                NetRequestQueue.getInstance(getApplicationContext())
                        .addRequest(NetMethods.stringRequest(
                                "http://localhost:8080/till/received/cash",
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
                NetRequestQueue.getInstance(getApplicationContext())
                        .addRequest(NetMethods.stringRequest(
                                "http://localhost:8080/till/received/coupon",
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}