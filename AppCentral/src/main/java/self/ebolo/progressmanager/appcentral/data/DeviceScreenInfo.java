package self.ebolo.progressmanager.appcentral.data;

import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * Created by YOLO on 8/20/2015.
 */
public class DeviceScreenInfo {
    public float HeightPx, WidthPx;

    public DeviceScreenInfo(AppCompatActivity Act) {
        Display display = Act.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        //float density  = Act.getResources().getDisplayMetrics().density;
        HeightPx = outMetrics.heightPixels;
        WidthPx = outMetrics.widthPixels;
    }
}
