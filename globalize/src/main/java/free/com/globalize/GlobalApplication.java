package free.com.globalize;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;

public class GlobalApplication extends Application {

    private ActivityLifecycleCallbacks lifecallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            LogUtils.d("onactivitycreated: " + activity.getLocalClassName());
            showGlobalDialog(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            LogUtils.d("onactivitystarted: " + activity.getLocalClassName());
        }

        @Override
        public void onActivityResumed(Activity activity) {
            LogUtils.d("onactivityresumed: " + activity.getLocalClassName());
        }

        @Override
        public void onActivityPaused(Activity activity) {
            LogUtils.d("onactivitypaused: " + activity.getLocalClassName());
            dismissDialog();
        }

        @Override
        public void onActivityStopped(Activity activity) {
            LogUtils.d("onactivitystopped: " + activity.getLocalClassName());

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            LogUtils.d("onactivitysaveinstancestate: " + activity.getLocalClassName() + ", outbundle: " + outState);
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            LogUtils.d("onactivitydestroyed: " + activity.getLocalClassName());
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(lifecallbacks);
        LogUtils.d("application oncreate");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterActivityLifecycleCallbacks(lifecallbacks);
        LogUtils.d("application onTerminated");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogUtils.d("application onLowMemory");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.d("application onconfigurationchanged");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        LogUtils.d("application ontrimmemory");
    }

    private AlertDialog dialog;
    private void showGlobalDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("当前页面类名");
        builder.setMessage(activity.getLocalClassName());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
