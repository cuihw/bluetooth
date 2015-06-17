package com.szch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bluetooth.connect.BlueToothConnect;
import com.szch.data.PreferencesData;

public class TestActivity extends Activity {

    private String[] arrayData = {
            "  _", "  _", "  _", "  _", 
    		"  _", "  _", "  _", "  _",
    		"  _", "  _", "  _", "  _",
            "  _", "  _", "  _", "  _"};

    GridView mGridview;

    SimpleAdapter mSimpleAdapter;
    GridViewAdpter mGridViewAdpter;
    
    private List<Integer> testdata = new ArrayList<Integer>();
    
    TextView mTextViewTestArea;

    private String mConstructionName;

    private int mTestNumber;

    private int mTestAngle = 90;

    private String mPostion;

    private boolean isMachine;

    private String mDesginStrength = "C15";

    private float mCarbonizeStrength = 0;

    private int mFixStrength;

    private String mDate;
    
    private int mGridItemSelected = 0;

    ArrayList<HashMap<String, Object>> lstItem = new ArrayList<HashMap<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_activity);

        Intent intent = getIntent();

        getTestParam(intent);

        initView();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mBlueToothConnect.close();
    }

    @Override
    protected void onResume() {

        super.onResume();
        connectBluetoothDevice ();
    }

    BlueToothConnect mBlueToothConnect;

    private void connectBluetoothDevice() {
        mBlueToothConnect = BlueToothConnect.getInstence(this, mHandler);
        mBlueToothConnect.init();
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case BlueToothConnect.READ_DATA:
                    int data = msg.arg1;
                    fillData(data);
                    break;
                case BlueToothConnect.NO_PAIRED_DEVICES:
                    showNopairedDevices();
                    break;
            }

            super.handleMessage(msg);
        }
        
    };

    private void getTestParam(Intent intent) {
        mConstructionName = intent.getStringExtra(PreferencesData.CONSTRUCTION_NAME);
        mTestNumber = intent.getIntExtra(PreferencesData.TEST_AREA_NUMBER, 10);
        mTestAngle = intent.getIntExtra(PreferencesData.TEST_ANGLE, 90);
        mPostion = intent.getStringExtra(PreferencesData.TEST_POSTION);
        isMachine = intent.getBooleanExtra(PreferencesData.IS_MACHINE, true);
        mDesginStrength = intent.getStringExtra(PreferencesData.DESGIN_STRENGTH);
        mCarbonizeStrength = intent.getFloatExtra(PreferencesData.CARBONIZE, 0.0f);
        mFixStrength = intent.getIntExtra(PreferencesData.FIX_STRENGTH, 0);
        mDate = intent.getStringExtra(PreferencesData.DATE);
    }

    protected void showNopairedDevices() {

        Toast toast = Toast.makeText(getApplicationContext(), "没有匹配的蓝牙设备！请打开蓝牙设备并匹配",
                Toast.LENGTH_SHORT);

        LinearLayout toastView = (LinearLayout) toast.getView();
        ImageView imageCodeProject = new ImageView(getApplicationContext());
        imageCodeProject.setImageResource(R.drawable.bluetooth);
        toastView.addView(imageCodeProject, 0);
        toast.show();
    }

    protected void fillData(int data) {

        if (testdata.size() > 15) {
            testdata.remove(15);
        }

        if (mGridItemSelected < testdata.size()) {

            testdata.add(mGridItemSelected, data);

            if (mGridItemSelected < 15) {
                mGridItemSelected ++;   
            }

        } else {
            testdata.add(data);

            mGridItemSelected = testdata.size();
            if (mGridItemSelected > 15) {
                testdata.add(mGridItemSelected, data);
                mGridItemSelected = 15;
            }
        }
        setSelectItem(mGridItemSelected);
    }

    private void initView() {

        TextView testparam = (TextView) findViewById(R.id.test_param);

        testparam.setText("构件名称： " + mConstructionName + "\n测区数：" + mTestNumber + "    测试角度： " + mTestAngle
                + "   测试方位：" + mPostion + "\n是否泵送：" + (isMachine ? "是" : "否") + "    设计强度：" + mDesginStrength
                + "\n碳化修正：" + mCarbonizeStrength + "    强度修正量：" + mFixStrength + "\n 测试日期：" + mDate);

        mGridview = (GridView) findViewById(R.id.gridView1);

        mGridViewAdpter = new GridViewAdpter(arrayData);

        mTextViewTestArea = (TextView) findViewById(R.id.test_area_index);

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
    public class GridViewAdpter  extends BaseAdapter  {
        private LayoutInflater li;
        
        private int selected = 0; 

        public GridViewAdpter(String[] arrayData) {
            li = LayoutInflater.from(TestActivity.this);
        }

        List<View> viewGridList = new ArrayList<View>();

        HashMap<Integer, View> viewGridMap = new HashMap<Integer, View> ();

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

            ViewHolder vh = (ViewHolder) convert.getTag();
            vh.textview.setBackgroundResource(R.drawable.item_selected);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = li.inflate(R.layout.grid_item, null);
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

            return convertView;
        }

        class ViewHolder {
            TextView textview;
        }

    }
}
