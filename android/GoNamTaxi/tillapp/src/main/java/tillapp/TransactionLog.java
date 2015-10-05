package tillapp;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by abrie on 15-10-04.
 */
public class TransactionLog {
    final ArrayList<String> listItems = new ArrayList<>();
    final ArrayAdapter<String> adapter;
    private final SimpleDateFormat dateFormat;

    public TransactionLog(Context context) {
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listItems);
        dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
    }

    public ArrayAdapter<String> getAdapter() {
        return adapter;
    }

    public void logValidCoupon(long timeStamp) {
        String timeString  = dateFormat.format(new Date(timeStamp));
        adapter.insert(String.format("%s:Ticket Valid.", timeString), 0);
    }

    public void logInvalidCoupon(long age, long timeStamp) {
        String timeString  = dateFormat.format(new Date(timeStamp));
        String message = String.format("%s:Ticket Invalid, was used %s", timeString, formatAge(age));
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

    public void logCash(long timeStamp) {
        String timeString  = dateFormat.format(new Date(timeStamp));
        adapter.insert(String.format("%s: cash acknowledged", timeString), 0);
    }
}
