package cadesus.co.cadesus;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by Fraps on 10/07/2016.
 */
public class MainApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
