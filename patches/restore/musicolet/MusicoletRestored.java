import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.drawable.IconCompat;
import android.database.sqlite.SQLiteDatabase;
import in.krosbits.musicolet.MediaMountedReceiver;
import in.krosbits.musicolet.MusicActivity;
import in.krosbits.musicolet.MyApplication;
import in.krosbits.musicolet.RemoteMediaControlReceiver;
import in.krosbits.musicolet.fh;
import java.util.ArrayList;
import java.util.HashSet;

public final class MusicoletRestored {

    private MusicoletRestored() {
    }

    public static Object a61DoInBackground(a61 task, Object[] params) {
        Void[] arguments = (Void[]) params;
        ArrayList queue = task.a;
        task.c = true;
        while (!a61.isEmpty$001(queue)) {
            z51 job = (z51) a61.remove$002(queue, 0);
            task.b = job;
            a61.run$003(job);
            task.b = null;
        }
        task.c = false;
        return null;
    }

    public static Object on1DoInBackground(on1 task, Object[] params) {
        SQLiteDatabase database = on1.getWritableDatabase$001(task.c);
        ContentValues values = new ContentValues(1);
        on1.put$002(values, "COL_LASTPOS", Long.valueOf(task.a));
        on1.update$004(database, "TABLE_SONGS", values, "COL_PATH=?",
                new String[] { on1.toString$003(task.b.c.f) });
        return null;
    }

    public static Object nn1DoInBackground(nn1 task, Object[] params) {
        SQLiteDatabase database = nn1.getWritableDatabase$001(task.g);
        ContentValues values = new ContentValues(5);
        nn1.put$002(values, "COL_LAST_PLAYED", Long.valueOf(task.a));
        nn1.put$003(values, "COL_NUM_PLAYED", Integer.valueOf(task.b));
        nn1.put$004(values, "COL_NUM_PLAYED_W", Integer.valueOf(task.c));
        nn1.put$005(values, "COL_NUM_PLAYED_M", Integer.valueOf(task.d));
        nn1.put$006(values, "COL_NUM_PLAYED_Y", Integer.valueOf(task.e));
        nn1.update$008(database, "TABLE_SONGS", values, "COL_PATH=?",
                new String[] { nn1.toString$007(task.f.c.f) });
        return null;
    }

    public static Object ht0DoInBackground(ht0 task, Object[] params) {
        if (task.a == 0) {
            MediaMountedReceiver.b = task;
            ht0.isCancelled$001(task);
            try {
                Thread.sleep(5000L);
                ht0.isCancelled$002(task);
                MyApplication.q();
                ht0.isCancelled$003(task);
                Thread.sleep(4000L);
                ht0.isCancelled$004(task);
                MyApplication.q();
            } catch (InterruptedException interruption) {
                ht0.printStackTrace$005(interruption);
            }
            MediaMountedReceiver.b = null;
        } else if (task.a == 1) {
            s71.i();
        }
        return null;
    }

    public static Object jm1DoInBackground(jm1 task, Object[] params) {
        if (!di0.m(jm1.getApplicationContext$001(MyApplication.i))) {
            return null;
        }
        Intent intent = new Intent(task.a);
        jm1.setClass$003(intent, jm1.getApplicationContext$002(MyApplication.i), MusicActivity.class);
        jm1.putExtra$004(intent, "extshjp_", task.b);
        Context context = jm1.getApplicationContext$005(MyApplication.i);
        StringBuilder identifier = new StringBuilder();
        jm1.append$007(identifier, jm1.getAction$006(intent));
        jm1.append$009(identifier, jm1.getStringExtra$008(intent, "extshjp_"));
        hm1 shortcut = new hm1();
        shortcut.a = context;
        shortcut.b = jm1.toString$010(identifier);
        shortcut.c = new Intent[] { intent };
        shortcut.d = new ComponentName(jm1.getApplicationContext$011(MyApplication.i), MusicActivity.class);
        shortcut.e = task.c;
        Drawable launcherIcon = context.getResources().getDrawable(context.getApplicationInfo().icon);
        Bitmap bitmap = Bitmap.createBitmap(168, 168, Bitmap.Config.ARGB_8888);
        launcherIcon.setBounds(0, 0, 168, 168);
        launcherIcon.draw(new Canvas(bitmap));
        IconCompat icon = new IconCompat(1);
        icon.b = bitmap;
        shortcut.f = icon;
        di0.s(context, shortcut);
        return null;
    }

    public static Object kbDoInBackground(kb task, Object[] params) {
        return Boolean.FALSE;
    }

    public static Object c51DoInBackground(c51 task, Object[] params) {
        return null;
    }

    public static Object ez0DoInBackground(ez0 task, Object[] params) {
        task.c = new HashSet();
        return task.c;
    }

    public static void onReceiveFh(fh receiver, Context context, Intent intent) {
        try {
            fh.getApplicationContext$001(MyApplication.i);
            Class.forName(fh.getStringExtra$002(intent, "tclass"));
        } catch (Throwable throwable) {
            fh.printStackTrace$007(throwable);
        }
    }

    public static void onReceiveRemoteMediaControlReceiver(RemoteMediaControlReceiver receiver, Context context,
            Intent intent) {
        RemoteMediaControlReceiver.a(context, intent);
    }

    public static void onReceiveMediaMountedReceiver(MediaMountedReceiver receiver, Context context, Intent intent) {
        MediaMountedReceiver.isInitialStickyBroadcast$001(receiver);
        StringBuilder text = new StringBuilder("MMR OR ");
        MediaMountedReceiver.append$002(text, intent);
        v82.y(MediaMountedReceiver.toString$003(text));
        MediaMountedReceiver.a();
    }

}
