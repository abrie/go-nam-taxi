package na.nbii.tillapp;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by abrie on 15-10-04.
 */
public class TransactionLog {
    final ArrayList<String> listItems = new ArrayList<>();
    final ArrayAdapter<String> adapter;

    public TransactionLog(Context context) {
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listItems);
    }

    public ArrayAdapter<String> getAdapter() {
        return adapter;
    }

    public void logValidCoupon(String couponCode) {
        adapter.insert("Ticket Validated:" + couponCode, 0);
    }

    public void logInvalidCoupon(String couponCode) {
        adapter.insert("TICKET NOT VALID:" + couponCode, 0);
    }

    public void logCash() {
        adapter.insert("cash payment acknowledged", 0);
    }
}
