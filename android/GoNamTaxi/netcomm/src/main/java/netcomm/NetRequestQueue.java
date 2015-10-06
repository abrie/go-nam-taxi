// EXAMPLE TAKEN FROM: https://developer.android.com/training/volley/requestqueue.html

package netcomm;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class NetRequestQueue {
    private static NetRequestQueue instance;
    private RequestQueue requestQueue;
    private static Context context;

    private NetRequestQueue(Context context) {
        NetRequestQueue.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized NetRequestQueue getInstance(Context context) {
        if (instance == null) {
            instance = new NetRequestQueue(context);
        }
        return instance;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addRequest(Request<T> req) {
        getRequestQueue().add(req);
    }
}
