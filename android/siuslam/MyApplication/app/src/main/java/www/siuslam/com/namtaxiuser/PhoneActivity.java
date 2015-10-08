package www.siuslam.com.namtaxiuser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class PhoneActivity extends AppCompatActivity {

    private Button one;
    private Button two;
    private Button three;
    private Button four;
    private Button five;
    private Button six;
    private Button seven;
    private Button eight;
    private Button nine;
    private Button star;
    private Button zero;
    private Button hush;
    private ImageButton call;
    private ImageButton erase;
    private TextView label;

    ProgressDialog prgDialog1;

    private String pin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        label = (TextView) findViewById(R.id.label);
        one = (Button) findViewById(R.id.number_one);
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label.append("1");
            }
        });
        two = (Button) findViewById(R.id.number_two);
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label.append("2");
            }
        });
        three = (Button) findViewById(R.id.number_three);
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label.append("3");
            }
        });
        four = (Button) findViewById(R.id.number_four);
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label.append("4");
            }
        });
        five = (Button) findViewById(R.id.number_five);
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label.append("5");
            }
        });
        six = (Button) findViewById(R.id.number_six);
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label.append("6");
            }
        });
        seven = (Button) findViewById(R.id.number_seven);
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label.append("7");
            }
        });
        eight = (Button) findViewById(R.id.number_eight);
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label.append("8");
            }
        });
        nine = (Button) findViewById(R.id.number_nine);
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label.append("9");
            }
        });
        star = (Button) findViewById(R.id.number_star);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label.append("*");
            }
        });
        zero = (Button) findViewById(R.id.number_zero);
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label.append("0");
            }
        });
        hush = (Button) findViewById(R.id.number_hash);
        hush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label.append("#");
            }
        });
        erase = (ImageButton) findViewById(R.id.number_delete);
        erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(label.getText().toString().length()>0)
                label.setText(removeLastChar(label.getText().toString()));
            }
        });
        call = (ImageButton) findViewById(R.id.number_call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                String number = tMgr.getSimSerialNumber();
                String operator = tMgr.getNetworkOperatorName();
                Ussd ussd = new Ussd(label.getText().toString(), number, operator, "Khomasdal 28");
                Gson gson = new GsonBuilder().create();
                //Use GSON to serialize Array List to JSON
                Log.e("json:", gson.toJson(ussd));
                sendUssdToServer(gson.toJson(ussd));
            }
        });
    }

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    private void sendUssdToServer(String code) {
        final int DEFAULT_TIMEOUT = 7000 * 1000;
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        prgDialog1 = new ProgressDialog(this);
        prgDialog1.setMessage("USSD Running...");
        prgDialog1.setCancelable(true);
        prgDialog1.show();
        params.put("JSON", code);
        client.setTimeout(DEFAULT_TIMEOUT);
        client.post("http://xlab/logic/api.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                final String[] response = new String[1];
                try {
                    prgDialog1.dismiss();
                    response[0] = new String(bytes, "UTF-8");  // Best way to decode using "UTF-8"
                    System.out.println("Text Decryted using UTF-8 : " + response[0]);
                    label.setText(response[0]);
                    JSONObject res = new JSONObject(response[0]);
                    if (res.getInt("code") == 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PhoneActivity.this, R.style.AppCompatAlertDialogStyle);

                        final EditText input = new EditText(getApplicationContext());
                        builder.setMessage(res.getString("msg"));
                        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                        builder.setView(input);

                        builder.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pin = input.getText().toString();
                                TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                                String number = tMgr.getSimSerialNumber();
                                String operator = tMgr.getNetworkOperatorName();
                                Ussd ussd = new Ussd(pin, number, operator, "Khomasdal 28");
                                Gson gson = new GsonBuilder().create();
                                //Use GSON to serialize Array List to JSON
                                Log.e("json:", gson.toJson(ussd));
                                sendPin(gson.toJson(ussd));

                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.create();
                        builder.show();
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PhoneActivity.this, R.style.AppCompatAlertDialogStyle);

                        // Setting Dialog Message
                        alertDialog.setMessage(res.getString("msg"));

                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        // Showing Alert Message
                        alertDialog.show();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                prgDialog1.dismiss();
                if (i == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (i == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void sendPin(String pin) {
        final int DEFAULT_TIMEOUT = 7000 * 1000;
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        prgDialog1 = new ProgressDialog(this);
        prgDialog1.setMessage("USSD Running...");
        prgDialog1.setCancelable(true);
        prgDialog1.show();
        params.put("JSON", pin);
        client.setTimeout(DEFAULT_TIMEOUT);
        client.post("http://xlab/logic/api.php", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }
}
