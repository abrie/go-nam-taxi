package na.nbii.netcomm;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by abrie on 15-10-02.
 */
public class NetMethods {

    public interface StringResponseHandler {
        void onString(String content);
        void onError(String error);
    }

    static public StringRequest stringRequest(String url, final StringResponseHandler handler) {
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
}
