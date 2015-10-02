// EXAMPLE TAKEN FROM: https://developer.android.com/training/volley/requestqueue.html

package na.nbii.tillapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class NetComm {
    private static NetComm mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private NetComm(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized NetComm getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetComm(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
