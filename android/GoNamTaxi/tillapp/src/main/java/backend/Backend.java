package backend;

import org.json.JSONException;
import org.json.JSONObject;

import na.nbii.netcomm.NetMethods;
import na.nbii.netcomm.NetRequestQueue;

/**
 * Created by abrie on 15-10-04.
 */
public class Backend {
    private NetRequestQueue requestQueue;
    private PathBuilder pathBuilder;

    public interface CouponTransactionResultHandler {
        void onCouponValidationResult(boolean isValid);
        void onCouponValidationError(String error);
    }

    public interface CashTransactionResultHandler {
        void onCashTransactionResult(boolean isValid);
        void onCashTransactionError(String error);
    }

    public Backend(NetRequestQueue netRequestQueue, PathBuilder pathBuilder) {
        this.requestQueue = netRequestQueue;
        this.pathBuilder = pathBuilder;
    }

    public void validateCoupon(String rawValue, final CouponTransactionResultHandler handler) {
        requestQueue.addRequest(NetMethods.jsonRequest(
                pathBuilder.getUrl("/till/received/coupon/" + pathBuilder.getTaxiNumber() + "/" + rawValue),
                new NetMethods.JsonResponseHandler() {
                    @Override
                    public void onJson(JSONObject content) {
                        boolean isValid;
                        try {
                            isValid = content.getBoolean("is_valid");
                        } catch (JSONException e) {
                            isValid = false;
                        }
                        handler.onCouponValidationResult(isValid);
                    }

                    @Override
                    public void onError(String error) {
                        handler.onCouponValidationError(error);
                    }
                }));
    }

    public void submitCash(final CashTransactionResultHandler handler) {
        requestQueue.addRequest(NetMethods.jsonRequest(
                pathBuilder.getUrl("/till/received/cash/" + pathBuilder.getTaxiNumber()),
                new NetMethods.JsonResponseHandler() {
                    @Override
                    public void onJson(JSONObject content) {
                        handler.onCashTransactionResult(true);
                    }

                    @Override
                    public void onError(String error) {
                        handler.onCashTransactionError(error);
                    }
                }
        ));
    }
}
