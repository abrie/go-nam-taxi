package tillapp;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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

    public void logInvalidCoupon(String couponCode, long age) {
        String message = String.format("Ticket Invalid: %s", formatAge(age));
        adapter.insert(message, 0);
    }

    public String formatAge(long milliseconds) {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
        long days = TimeUnit.MILLISECONDS.toDays(milliseconds);

        if (days > 0) {
            return String.format("%d days ago", days);
        }
        if (hours > 0) {
            return String.format("%d hours ago", hours);
        }
        if (minutes > 0) {
            return String.format("%d minutes ago", minutes);
        }
        if (seconds > 0) {
            return String.format("%d seconds ago", seconds);
        }

        return String.format("%d milliseconds ago", milliseconds);
    }

    public void logCash() {
        adapter.insert("cash payment acknowledged", 0);
    }
}
