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
    private NetPath netPath;

    public interface CouponValidationResultHandler {
        void onCouponValidationResult(boolean isValid);
        void onCouponValidationError(String error);
    }

    public interface CashTransactionResultHandler {
        void onCashTransactionResult(boolean isValid);
        void onCashTransactionError(String error);
    }

    public Backend(NetRequestQueue netRequestQueue, NetPath netPath) {
        this.requestQueue = netRequestQueue;
        this.netPath = netPath;
    }

    public void validateCoupon(String rawValue, final CouponValidationResultHandler handler) {
        requestQueue.addRequest(NetMethods.jsonRequest(
                netPath.getUrl("/till/received/coupon/" + netPath.getTaxiNumber() + "/" + rawValue),
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
        requestQueue.addRequest(NetMethods.stringRequest(
                netPath.getUrl("/till/received/cash/" + netPath.getTaxiNumber()),
                new NetMethods.StringResponseHandler() {
                    @Override
                    public void onString(String content) {
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
