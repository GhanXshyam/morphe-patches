import android.content.Context;
import android.content.Intent;
import com.poe.util.j2;

public final class DownloadCompleteReceiver {

    private DownloadCompleteReceiver() {
    }

    public static void onReceive(j2 receiver, Context context, Intent intent) {
        long downloadId = intent.getLongExtra("extra_download_id", -1L);
        Object expected = receiver.a.element;
        if (expected == null) {
            return;
        }
        long expectedId = ((Number) expected).longValue();
        if (downloadId != expectedId) {
            return;
        }
        receiver.b.invoke();
        receiver.c.d(Long.valueOf(expectedId));
    }

}
