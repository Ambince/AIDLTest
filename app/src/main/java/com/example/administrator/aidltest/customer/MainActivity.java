package com.example.administrator.aidltest.customer;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.aidltest.CalcService;
import com.example.administrator.aidltest.ICalcAIDLTest;
import com.example.administrator.aidltest.R;

public class MainActivity extends AppCompatActivity {

    private ICalcAIDLTest mICalcAIDL;

    private int pid = Process.myPid();

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //实例化对象
            mICalcAIDL = ICalcAIDLTest.Stub.asInterface(iBinder);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mICalcAIDL = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //绑定服务
    public void bindService(View view) {
        Intent intent = new Intent(this, CalcService.class);
        intent.setAction("com.amence.aidl");
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void unbindService(View view) {
        unbindService(connection);
    }

    public void addInvoked(View view) {

        Toast.makeText(this,"当前进程："+pid,Toast.LENGTH_SHORT).show();
        if (mICalcAIDL != null) {
            try {
                int sum = mICalcAIDL.add(12, 12);
                Toast.makeText(this, sum+"", Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "进程被杀死", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 点击50-12按钮时调用
     *
     * @param view
     */
    public void minInvoked(View view) throws Exception {

        if (mICalcAIDL != null) {
            int addRes = mICalcAIDL.min(58, 12);
            Toast.makeText(this, addRes + "", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "服务端未绑定或被异常杀死，请重新绑定服务端", Toast.LENGTH_SHORT)
                    .show();
        }

    }


}
