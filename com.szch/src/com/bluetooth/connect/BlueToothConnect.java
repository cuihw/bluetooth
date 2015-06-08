package com.bluetooth.connect;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Set;
import java.util.UUID;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BlueToothConnect{

    public static BlueToothConnect mInstence;
    
    private Context mContext;
    
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private static Handler mHandler;

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static final String NAME_SECURE = "name_secure";

    public static final int NO_PAIRED_DEVICES = 1;

    protected static final String TAG = null;
    
    BluetoothServerSocket mBluetoothServerSocket;
    
    private void init() {
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        
        Set<BluetoothDevice> pairedList = bluetoothAdapter.getBondedDevices();

        if (pairedList.size() == 0) {
            Message msg = mHandler.obtainMessage(NO_PAIRED_DEVICES);
            mHandler.sendMessage(msg);
        }
        
        connectDevices();


    }

    private void connectDevices() {
        try {
            mBluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE, MY_UUID);
            
            BluetoothSocket socket = mBluetoothServerSocket.accept();
            
            if (socket != null) {
                startReadingThread(socket);                
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private boolean stop = false;
    
    private void startReadingThread(final BluetoothSocket socket) {

        new Thread (new Runnable () {

            @Override
            public void run() {
                InputStream is = null;
                
                try {
                    is = socket.getInputStream();
                    while (true) {
                        if (stop) {
                            break;
                        }
                        byte[] buffer = new byte[256];
                        
                        int length = is.read();

                        if (length > 0) {
                            is.read(buffer);
                            Log.d(TAG, "buffer = " + new String(buffer));
                        }
                    }


                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
            
        }).start();
        
        
    }

    public static BlueToothConnect getInstence (Context c, Handler h) {
        if (mInstence == null) {
            mInstence = new BlueToothConnect(c, h);
        }
        mHandler = h;
        return mInstence;
    }

    private BlueToothConnect (Context c, Handler h) {
        mContext = c;
        mHandler = h;
        
        new Thread(new Runnable() {

            @Override
            public void run() {

                init();

            }}).start();
    }

    //广播接收器
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothTools.ACTION_NOT_FOUND_SERVER.equals(action)) {
                //未发现设备
                //serversText.append("not found device\r\n");
                
            } else if (BluetoothTools.ACTION_FOUND_DEVICE.equals(action)) {
                //获取到设备对象
                BluetoothDevice device = (BluetoothDevice)intent.getExtras().get(BluetoothTools.DEVICE);
                //deviceList.add(device);
                //serversText.append(device.getName() + "\r\n");
                
            } else if (BluetoothTools.ACTION_CONNECT_SUCCESS.equals(action)) {
                //连接成功
                
            } else if (BluetoothTools.ACTION_DATA_TO_GAME.equals(action)) {
                //接收数据
                TransmitBean data = (TransmitBean)intent.getExtras().getSerializable(BluetoothTools.DATA);
                String msg = "from remote " + new Date().toLocaleString() + " :\r\n" + data.getMsg() + "\r\n";

            } 
        }
     };
}
