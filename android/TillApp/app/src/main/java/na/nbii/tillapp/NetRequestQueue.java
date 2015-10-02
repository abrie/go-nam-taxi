// EXAMPLE TAKEN FROM: https://developer.android.com/training/volley/requestqueue.html

package na.nbii.tillapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class NetRequestQueue {
    private static NetRequestQueue mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private NetRequestQueue(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized NetRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetRequestQueue(context);
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addRequest(Request<T> req) {
        getRequestQueue().add(req);
    }
}
