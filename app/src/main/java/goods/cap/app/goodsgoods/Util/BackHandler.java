package goods.cap.app.goodsgoods.Util;

import android.widget.Toast;
import android.app.Activity;

public class BackHandler {

    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;
    private String message;

    public BackHandler(Activity context, String message) {
        this.activity = context;
        this.message = message;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 3000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 3000) {
            activity.finish();
            toast.cancel();
        }
    }

    private void showGuide() {
        toast = Toast.makeText(this.activity, this.message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
