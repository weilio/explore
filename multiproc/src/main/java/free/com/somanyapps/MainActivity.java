package free.com.somanyapps;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button local;
    private Button messenger;
    private Button aidl;

    private LocalService localService;

    private Messenger mGetReplyMessenger = new Messenger(new MessageHandler());
    private Messenger mServerMessenger;

    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case Constants.MSG_FROM_SERVER:
                    Bundle data = msg.getData();
                    String serverMsg = data.getString(Constants.MSG_KEY);
                    Toast.makeText(MainActivity.this, serverMsg, Toast.LENGTH_LONG).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private ServiceConnection mMessengerConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServerMessenger = new Messenger(service);

            Message message = Message.obtain(null, Constants.MSG_FROM_CLIENT);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.MSG_KEY, "你好，我是客户端, 收到请回复！");
            message.setData(bundle);
            message.replyTo = mGetReplyMessenger;
            try {
                mServerMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private ServiceConnection mLocalConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            android.util.Log.d("liwei", "onserviceconnect ibinder:" + iBinder);
            localService = ((LocalService.LocalServiceBinder)iBinder).getLocalService();
            localService.callLocalServiceMethod();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            localService = null;
        }
    };

    private ServiceConnection mAidlConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IUserManager manager = IUserManager.Stub.asInterface(service);

            try {
                List<User> users = manager.getAllUsers();
                android.util.Log.d("liwei", "users size:" + users.size());
                for (User user : users) {
                    Toast.makeText(MainActivity.this, user.toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        local = (Button) findViewById(R.id.local);
        messenger = (Button) findViewById(R.id.messenger);
        aidl = (Button) findViewById(R.id.aidl);

        local.setOnClickListener(this);
        messenger.setOnClickListener(this);
        aidl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.local:
                callLocal();
                break;
            case R.id.messenger:
                callMessengerService();
                break;
            case R.id.aidl:
                callAidlService();
                break;
            default:
                break;

        }
    }

    private void callLocal() {
        Intent service = new Intent(this, LocalService.class);
        bindService(service, mLocalConnection, Context.BIND_AUTO_CREATE);

        if (localService != null) {
            localService.callLocalServiceMethod();
        }
    }

    private void callMessengerService() {
        Intent service = new Intent(this, RemoteMessengerService.class);
        bindService(service, mMessengerConnection, Context.BIND_AUTO_CREATE);
    }

    private void callAidlService() {
        Intent service = new Intent(this, ServiceUserManager.class);
        bindService(service, mAidlConnection, Context.BIND_AUTO_CREATE);
    }
}
