package com.szch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bluetooth.connect.BlueToothConnect;
import com.szch.data.PreferencesData;
import com.szch.data.TestData;
import com.szch.data.TestDataTransmit;

public class TestActivity extends Activity {

    protected static final int TSETDATA = 30;

    public static final String TAG = "TestActivity";

    private String[] arrayData = {
            "", "", "", "", "",
            "", "", "", "", "",
            "", "", "", "", "",
            "", "", "", "", ""};

    GridView mGridview;

    SimpleAdapter mSimpleAdapter;
    GridViewAdpter mGridViewAdpter;

    private TestData testdata = new TestData();

    TextView mTextViewTestArea;

    private String mConstructionName;

    private int mTestNumber;

    private int mTestAngle = 0;
    private int[] angleArray = {90,60,45,30,0,-30,-45,-60,-90};

    private String mSurface;

    private boolean isMachine;

    private String mDesginStrength = "C15";

    private float mCarbonizeStrength = 0;

    private int mFixStrength;

    private String mDate;

    private int mGridItemSelected = 0;

    List<TestData> dataList = new ArrayList<TestData>();

    int mSavedNumber;
    
    PowerManager.WakeLock wl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_activity);

        Intent intent = getIntent();

        getTestParam(intent);

        initView();
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        wl.acquire();//申请锁这个里面会调用PowerManagerService里面acquireWakeLock()
    }
    
    public void onClickSave(View view) {
        if (checkValue()) {
            
            for (int i = 0; i < angleArray.length; i ++) {
                if (mTestAngle == angleArray[i]) {
                    testdata.angleIndex = i;
                    Log.d(TAG, "angleIndex = " + i);
                    break;
                }
            }
            
            String[] surface = getResources().getStringArray(R.array.surface);

            for (int i = 0; i < surface.length; i ++) {
                if (mSurface == surface[i]) {
                    testdata.surfaceIndex = i;
                    Log.d(TAG, "surfaceIndex = " + i);
                    break;
                }
            }
            testdata.carbonizeFix = mCarbonizeStrength;
            testdata.isMachine = isMachine;

            dataList.add(testdata);
            mSavedNumber = dataList.size();
            testdata = new TestData();

            if (mSavedNumber == mTestNumber) {
                showTestResult();
            } else {
                // start test
                // for test
                // testData();
            }

            mTextViewTestArea.setText("第" + (mSavedNumber + 1) + "测区");


        } else {
            Toast.makeText(this, "测试数据不完整！", Toast.LENGTH_SHORT).show();
        }

    }

    private void showTestResult() {
        Log.d(TAG, "show test result....");
        setData();
        Intent intent = new Intent(this, TestResultActivity.class);
        startActivity(intent);
    }

    private void setData() {
        TestDataTransmit.dataList = dataList;
    }

    private boolean checkValue() {
        for (int i = 0; i < testdata.data.length; i++) {
            if (testdata.data[i] == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        // for Test();
        stopTest();

        mBlueToothConnect.close();
    }

    @Override
    protected void onResume() {

        super.onResume();
        connectBluetoothDevice();

        if (mSavedNumber == mTestNumber) {
            dataList.clear();
            mSavedNumber = dataList.size();
            mTextViewTestArea.setText("第" + (mSavedNumber + 1) + "测区");
        }

        // for test
        // testData();

    }
    

    @Override
    protected void onDestroy() {

        wl.release();
        super.onDestroy();
    }


    BlueToothConnect mBlueToothConnect;

    private void connectBluetoothDevice() {
        mBlueToothConnect = BlueToothConnect.getInstence(this, mHandler);
        mBlueToothConnect.init();
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BlueToothConnect.READ_DATA:
                    int data = msg.arg1;
                    fillData(data);
                    break;
                case BlueToothConnect.NO_PAIRED_DEVICES:
                    showNopairedDevices();
                    break;

                case TSETDATA:
                    data = msg.arg1;
                    fillData(data);
                    break;
            }

            super.handleMessage(msg);
        }

    };

    private void getTestParam(Intent intent) {
        mConstructionName = intent.getStringExtra(PreferencesData.CONSTRUCTION_NAME);
        mTestNumber = intent.getIntExtra(PreferencesData.TEST_AREA_NUMBER, 10);
        mTestAngle = intent.getIntExtra(PreferencesData.TEST_ANGLE, 0);
        mSurface = intent.getStringExtra(PreferencesData.TEST_POSTION);
        isMachine = intent.getBooleanExtra(PreferencesData.IS_MACHINE, true);
        mDesginStrength = intent.getStringExtra(PreferencesData.DESGIN_STRENGTH);
        mCarbonizeStrength = intent.getFloatExtra(PreferencesData.CARBONIZE, 0.0f);
        mFixStrength = intent.getIntExtra(PreferencesData.FIX_STRENGTH, 0);
        mDate = intent.getStringExtra(PreferencesData.DATE);
        
        Log.d(TAG, "mTestAngle = " + mTestAngle);
    }

    protected void showNopairedDevices() {

        Toast toast = Toast.makeText(getApplicationContext(), "没有匹配的蓝牙设备！请打开蓝牙设备并匹配", Toast.LENGTH_SHORT);

        LinearLayout toastView = (LinearLayout) toast.getView();
        ImageView imageCodeProject = new ImageView(getApplicationContext());
        imageCodeProject.setImageResource(R.drawable.bluetooth);
        toastView.addView(imageCodeProject, 0);
        toast.show();
    }

    protected void fillData(int data) {

        for (int i = 0; i < testdata.data.length; i++) {
            if ((testdata.data[i] == 0)) {
                if (mGridItemSelected <= i) {
                    testdata.data[mGridItemSelected] = data;
                    if (mGridItemSelected < (testdata.data.length-1 )) {
                        mGridItemSelected ++;
                    }
                    break;
                } else {
                    mGridItemSelected = i;
                    testdata.data[mGridItemSelected] = data;
                    if (mGridItemSelected < (testdata.data.length-1 )) {
                        mGridItemSelected ++;
                    }
                    break;
                }
            }
        }

        for (int i = 0; i < testdata.data.length; i++) {
            if (testdata.data[i] != 0) {
                arrayData[i] = "" + testdata.data[i];
            } else {
                arrayData[i] = "_";
            }
        }
        if (mGridItemSelected < 16) {
            setSelectItem(mGridItemSelected);
        }

    }

    private void initView() {

        TextView testparam = (TextView) findViewById(R.id.test_param);

        testparam.setText("构件名称： " + mConstructionName + "\n测区数：" + mTestNumber + "    测试角度： " + mTestAngle
                + "   测试方位：" + mSurface + "\n是否泵送：" + (isMachine ? "是" : "否") + "    设计强度：" + mDesginStrength
                + "\n碳化修正：" + mCarbonizeStrength + "    强度修正量：" + mFixStrength + "\n 测试日期：" + mDate);

        mGridview = (GridView) findViewById(R.id.gridView1);

        mGridViewAdpter = new GridViewAdpter(arrayData);

        mTextViewTestArea = (TextView) findViewById(R.id.test_area_index);
        mTextViewTestArea.setText("第" + (mSavedNumber + 1) + "测区");

        mGridview.setAdapter(mGridViewAdpter);

        mGridview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                setSelectItem(arg2);
            }
        });
    }

    protected void setSelectItem(int index) {

        mGridViewAdpter.selectItem(index);
        mGridViewAdpter.notifyDataSetChanged();
        mGridItemSelected = index;
    }

    @SuppressLint("UseSparseArrays")
    public class GridViewAdpter extends BaseAdapter {
        private LayoutInflater li;

        private int selected = 0;

        public GridViewAdpter(String[] arrayData) {
            li = LayoutInflater.from(TestActivity.this);
        }

        List<View> viewGridList = new ArrayList<View>();

        HashMap<Integer, View> viewGridMap = new HashMap<Integer, View>();

        @Override
        public int getCount() {
            return arrayData.length;
        }

        @Override
        public Object getItem(int arg0) {
            if (arg0 < arrayData.length) {
                return arrayData[arg0];
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void selectItem(int position) {
            selected = position;

            View convert = viewGridMap.get(position);
            if (convert != null) {

                ViewHolder vh = (ViewHolder) convert.getTag();
                vh.textview.setBackgroundResource(R.drawable.item_selected);
            }

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            //Log.d(TAG, "position = " + position);

            if (convertView == null) {
                vh = new ViewHolder();
                convertView = li.inflate(R.layout.item_grid, null);
                vh.textview = (TextView) convertView.findViewById(R.id.item_view);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            vh.textview.setText(arrayData[position]);

            if (selected == position) {
                vh.textview.setSelected(true);
                vh.textview.setBackgroundResource(R.drawable.item_selected);
            } else {
                vh.textview.setSelected(false);
                vh.textview.setBackgroundResource(R.drawable.item_selector);
            }

            viewGridMap.put(position, convertView);
            if (position > 15) {
                convertView.setVisibility(View.GONE);
            }

            return convertView;
        }

        class ViewHolder {
            TextView textview;
        }
    }

    // for test.
    Thread mTestThread;

    private void testData() {
        mTestThread = new Thread(new Runnable() {

            @Override
            public void run() {
                int count = 16;
                while (0 < count--) {
                    
                    Message msg = mHandler.obtainMessage(TSETDATA);
                    msg.arg1 = generatorTestData();
                    mHandler.sendMessage(msg);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mTestThread.start();
    }

    @SuppressWarnings("deprecation")
    private void stopTest() {
        
        try {
            if (mTestThread.isAlive()) {
                mTestThread.interrupt();
                mTestThread.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int generatorTestData() {
        int Max = 70, Min = 10;
        int ret = (int) Math.round(Math.random() * (Max - Min) + Min);
        return ret;
    }

}
