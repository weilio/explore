package free.com.somanyapps;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class LocalService extends Service {

    private LocalServiceBinder mBinder = new LocalServiceBinder();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class LocalServiceBinder extends Binder {
        public LocalService getLocalService() {
            return LocalService.this;
        }
    }

    public void callLocalServiceMethod() {
        Toast.makeText(this, "你好 我是本地服务的方法。", Toast.LENGTH_LONG).show();
    }

}
