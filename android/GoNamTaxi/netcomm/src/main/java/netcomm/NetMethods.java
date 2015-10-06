package netcomm;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

/**
 * Created by abrie on 15-10-02.
 */
public class NetMethods {

    public interface StringResponseHandler {
        void onString(String content);
        void onError(String error);
    }

    public interface JsonResponseHandler {
        void onJson(JSONObject content);
        void onError(String error);
    }

    static public StringRequest stringRequest(String url, final StringResponseHandler handler) {
        Log.i("TillApp", "StringRequest:"+url);
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handler.onString(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handler.onError(error.toString());
                    }
                });
    }

    static public JsonObjectRequest jsonRequest(String url, final JsonResponseHandler handler) {
        Log.i("TillApp", "JsonRequest:" + url);
        return new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handler.onJson(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handler.onError(error.toString());
                    }
                });
    }
}
