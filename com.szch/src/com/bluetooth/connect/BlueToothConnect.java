package com.bluetooth.connect;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BlueToothConnect {

    public static BlueToothConnect mInstence;

    private Context mContext;

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter
            .getDefaultAdapter();

    private static Handler mHandler;

    private static final UUID MY_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
                       //00001101-0000-1000-8000-00805F9B34FB"

    private static final String NAME_SECURE = "name_secure";

    protected static final String TAG = "BlueToothConnect";

    public static final int READ_DATA = 1;

    public static final int NO_PAIRED_DEVICES = 2;
    
    private BluetoothSocket mBluetoothSocket;

    BluetoothServerSocket mBluetoothServerSocket;
    
    private void registerBluetooth() {
        IntentFilter intent = new IntentFilter();  
        intent.addAction(BluetoothDevice.ACTION_FOUND);  
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);  
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);  
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        mContext.registerReceiver(broadcastReceiver, intent);
    }

    public void init() {

        registerBluetooth();

        new Thread(new Runnable() {

            @Override
            public void run() {

                if (!bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.enable();
                }

                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }

                Set<BluetoothDevice> pairedList = bluetoothAdapter.getBondedDevices();

                Log.d(TAG, "paired devices is " + pairedList.size());

                if (pairedList.size() == 0) {
                    Message msg = mHandler.obtainMessage(NO_PAIRED_DEVICES);
                    mHandler.sendMessage(msg);
                }

                Iterator iter = pairedList.iterator();

                while(iter.hasNext()){

                    BluetoothDevice bluetoothDevice = (BluetoothDevice) iter.next();

                    Log.d(TAG, "BluetoothDevice " + bluetoothDevice + ", name:" + bluetoothDevice.getName());

                    if (bluetoothDevice != null) {
                        connectToDevices (bluetoothDevice);
                        break;
                    }
                }
            }
        }).start();
    }

    private void connectToDevices(BluetoothDevice bluetoothDevice) {

        Log.d(TAG, "connectToDevices()....... ");
        mBluetoothSocket = null;
        try {
            mBluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);

            mBluetoothSocket.connect();
         
         startReadingThread(mBluetoothSocket);

        } catch (IOException e) {
            e.printStackTrace();
            try {
                mBluetoothSocket.close();
            } catch (IOException e1) {

                e1.printStackTrace();
            }
        }
    }

    private void listenConnectDevices() {

        Log.d(TAG, "listenConnectDevices()....... ");
        mBluetoothSocket = null;
        
        try {

            mBluetoothServerSocket = bluetoothAdapter
                    .listenUsingRfcommWithServiceRecord(NAME_SECURE, MY_UUID);

            mBluetoothSocket = mBluetoothServerSocket.accept();

            Log.d(TAG, "socket " + mBluetoothSocket);

            if (mBluetoothSocket != null) {
                startReadingThread(mBluetoothSocket);
            }

        } catch (IOException e) {

            Log.d(TAG, "socket ");
            e.printStackTrace();
            try {
                mBluetoothSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    private boolean stop = false;

    private void startReadingThread(final BluetoothSocket socket) {

        new Thread(new Runnable() {

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
                            
                            for (int i =0 ; i < length; i ++) {
                                Log.d(TAG, "buffer [" + i + "] = " + buffer[i]);
                            }

                            Log.d(TAG, "buffer = " + new String(buffer));
                        }
                        
                        notifyNewData();
                    }

                } catch (IOException e) {

                    e.printStackTrace();

                    try {
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

            }

        }).start();

    }

    private void notifyNewData() {
        Message msg = mHandler.obtainMessage(READ_DATA);
        msg.arg1 = 80;
        mHandler.sendMessage(msg);
    }

    public static BlueToothConnect getInstence(Context c, Handler h) {

        //printAllInform(BluetoothAdapter.class);

        if (mInstence == null) {
            mInstence = new BlueToothConnect(c, h);
        }
        mHandler = h;
        return mInstence;
    }

    private BlueToothConnect(Context c, Handler h) {
        mContext = c;
        mHandler = h;
    }
    
    


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            
//            if (BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
//                Log.d(TAG, "ACTION_CONNECTION_STATE_CHANGED");
//                if (BluetoothAdapter.STATE_CONNECTED ==bluetoothAdapter.getState()) {
//                    Log.d(TAG, "STATE_CONNECTED");
//                }
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);  
//                switch (device.getBondState()) {  
//                case BluetoothDevice.BOND_BONDING:  
//                    Log.d("BlueToothTestActivity", "BOND_BONDING......");  
//                    break;  
//                case BluetoothDevice.BOND_BONDED:  
//                    Log.d("BlueToothTestActivity", "BOND_BONDED");  
//                    connectToDevices(device);
//                    break;  
//                case BluetoothDevice.BOND_NONE:  
//                    Log.d("BlueToothTestActivity", "cancel BOND_NONE");  
//                default:  
//                    break;  
//                }  
//            }
            
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.d(TAG, "ACTION_FOUND"); 
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                Log.d(TAG, "ACTION_BOND_STATE_CHANGED"); 
            } else if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {
                Log.d(TAG, "ACTION_SCAN_MODE_CHANGED"); 
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                Log.d(TAG, "ACTION_STATE_CHANGED"); 
            }

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);  
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDED:
                        Log.d(TAG, "BOND_BONDED");  
                        connectToDevices(device);
                        break;  
                    case BluetoothDevice.BOND_NONE:  
                        Log.d(TAG, "cancel BOND_NONE");  
                }
            }
        }
    };

    static public void printAllInform(Class clsShow) {
        try {
            // get all method
            Method[] hideMethod = clsShow.getMethods();
            int i = 0;
            for (; i < hideMethod.length; i++) {
                Log.d("method name", hideMethod[i].getName());
            }
            // get all field
            Field[] allFields = clsShow.getFields();
            for (i = 0; i < allFields.length; i++) {
                Log.d("Field name", allFields[i].getName());
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {

        try {
            mContext.unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mBluetoothSocket !=null) {
            try {
                mBluetoothSocket.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

}
