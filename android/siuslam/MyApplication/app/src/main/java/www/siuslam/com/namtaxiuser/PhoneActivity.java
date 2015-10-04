package www.siuslam.com.namtaxiuser;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

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
                label.setText(removeLastChar(label.getText().toString()));
            }
        });
        call = (ImageButton) findViewById(R.id.number_call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ussd ussd = new Ussd(label.getText().toString());
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
        params.put("JSONObject", code);
        client.setTimeout(DEFAULT_TIMEOUT);
        client.post("http://www.phantime.com/hct/insertTest.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }
}
