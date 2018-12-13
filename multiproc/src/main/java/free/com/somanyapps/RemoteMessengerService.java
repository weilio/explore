package free.com.somanyapps;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class RemoteMessengerService extends Service {

    private Messenger mServerMessenger = new Messenger(new MessengerHandler());

    private class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case Constants.MSG_FROM_CLIENT:
                    Bundle data = msg.getData();
                    String clientMsg = data.getString(Constants.MSG_KEY);
                    Toast.makeText(RemoteMessengerService.this, clientMsg, Toast.LENGTH_SHORT).show();

                    Messenger clientMessenger = msg.replyTo;
                    Message replyMsg = Message.obtain(null, Constants.MSG_FROM_SERVER);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.MSG_KEY, "你好，我是服务端，消息已收到");
                    replyMsg.setData(bundle);
                    try {
                        clientMessenger.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mServerMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

}
