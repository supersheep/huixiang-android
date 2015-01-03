package in.spud.huixiang.util;

import android.content.Context;
import android.content.Intent;

import in.spud.huixiang.R;

/**
 * Created by spud on 15/1/2.
 */
public class Utility {


    public static void share(Context ctx, String content) {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        ctx.startActivity(Intent.createChooser(intent, ctx.getString(R.string.share_intent_title)));
    }
}
