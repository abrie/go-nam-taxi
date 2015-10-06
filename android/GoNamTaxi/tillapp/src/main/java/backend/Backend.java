package backend;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import netcomm.NetMethods;
import netcomm.NetRequestQueue;

/**
 * Created by abrie on 15-10-04.
 */
public class Backend implements LocationListener {
    private final NetRequestQueue requestQueue;
    private final PathBuilder pathBuilder;

    private double positon_lon;
    private double position_lat;

    public void setLocation(Location location) {
        this.positon_lon = location.getLongitude();
        this.position_lat = location.getLatitude();
    }

    public interface CouponTransactionResultHandler {
        void onCouponValidationResult(boolean isValid, long age, long timeStamp);
        void onCouponValidationError(String error);
    }

    public interface CashTransactionResultHandler {
        void onCashTransactionResult(long timeStamp);
        void onCashTransactionError(String error);
    }

    public Backend(NetRequestQueue netRequestQueue, PathBuilder pathBuilder) {
        this.requestQueue = netRequestQueue;
        this.pathBuilder = pathBuilder;


    }

    public void validateCoupon(String rawValue, final CouponTransactionResultHandler handler) {
        requestQueue.addRequest(NetMethods.jsonRequest(
                pathBuilder.couponUrl(rawValue, positon_lon, position_lat),
                new NetMethods.JsonResponseHandler() {
                    @Override
                    public void onJson(JSONObject content) {
                        try {
                            boolean isValid = content.getBoolean("is_valid");
                            long age = content.getLong("age");
                            long timeStamp = content.getLong("time");
                            handler.onCouponValidationResult(isValid, age, timeStamp);
                        } catch (JSONException e) {
                            handler.onCouponValidationError("failed to parse JSON");
                        }
                    }

                    @Override
                    public void onError(String error) {
                        handler.onCouponValidationError(error);
                    }
                }));
    }

    public void submitCash(final CashTransactionResultHandler handler) {
        requestQueue.addRequest(NetMethods.jsonRequest(
                pathBuilder.cashUrl(positon_lon, position_lat),
                new NetMethods.JsonResponseHandler() {
                    @Override
                    public void onJson(JSONObject content) {
                        try {
                            long timestamp = content.getLong("time");
                            handler.onCashTransactionResult(timestamp);
                        } catch(JSONException e) {
                            handler.onCashTransactionError("failed to parse JSON");
                        }
                    }

                    @Override
                    public void onError(String error) {
                        handler.onCashTransactionError(error);
                    }
                }
        ));
    }

    public void onLocationChanged(Location location) {
        setLocation(location);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public void onProviderEnabled(String provider) {}

    public void onProviderDisabled(String provider) {}
}
