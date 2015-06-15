package com.szch;

import java.text.SimpleDateFormat;

import com.szch.data.PreferencesData;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private EditText mEditText_date;

    private EditText mEditText_Construct;

    private EditText mEditTextTestAreaNumber;

    private EditText mEditTextTanhua;

    private Spinner mAngleSpinner;

    private Spinner mPostionSpinner;

    private Spinner mMachineSpinner;

    private Spinner mStrengthSpiner;

    private EditText mEditTextFixStrength;

    private EditText mEditTextDate;

    private String mConstructionName;

    private int mTestNumber;

    private int mTestAngle = 90;

    private String mPostion;

    private boolean isMachine;

    private String mDesginStrength = "C15";

    private float mCarbonizeStrength = 0;

    private int mFixStrength;

    private String mDate;

    private void saveDataPreferences() {
        PreferencesData.setStringData(this, PreferencesData.CONSTRUCTION_NAME, mConstructionName);
        PreferencesData.setIntData(this, PreferencesData.TEST_AREA_NUMBER, mTestNumber);
        PreferencesData.setIntData(this, PreferencesData.TEST_ANGLE, mTestAngle);
        PreferencesData.setStringData(this, PreferencesData.TEST_POSTION, mPostion);
        PreferencesData.setBooleanData(this, PreferencesData.IS_MACHINE, isMachine);
        PreferencesData.setStringData(this, PreferencesData.DESGIN_STRENGTH, mDesginStrength);
        PreferencesData.setfloatData(this, PreferencesData.CARBONIZE, mCarbonizeStrength);
        PreferencesData.setIntData(this, PreferencesData.FIX_STRENGTH, mFixStrength);
        PreferencesData.setStringData(this, PreferencesData.DATE, mDate);
    }


    private void setData(Intent intent) {

        intent.putExtra(PreferencesData.CONSTRUCTION_NAME, mConstructionName);
        intent.putExtra(PreferencesData.TEST_AREA_NUMBER, mTestNumber);
        intent.putExtra(PreferencesData.TEST_ANGLE, mTestAngle);
        intent.putExtra(PreferencesData.TEST_POSTION, mPostion);
        intent.putExtra(PreferencesData.IS_MACHINE, isMachine);
        intent.putExtra(PreferencesData.DESGIN_STRENGTH, mDesginStrength);
        intent.putExtra(PreferencesData.CARBONIZE, mCarbonizeStrength);
        intent.putExtra(PreferencesData.FIX_STRENGTH, mFixStrength);
        intent.putExtra(PreferencesData.DATE, mDate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrollview);

        initView();
    }

    private void initView() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sDateFormat.format(new java.util.Date());

        mEditText_date = (EditText) findViewById(R.id.edittext_date);

        mEditText_date.setText(date);

        mEditTextTestAreaNumber = (EditText) findViewById(R.id.edittext_testnumber);

        mEditText_Construct = (EditText) findViewById(R.id.editText_construct);

        mAngleSpinner = (Spinner) findViewById(R.id.spinner_angle);

        mAngleSpinner.setOnItemSelectedListener(mAngleSpinnerListener);


        mPostionSpinner = (Spinner) findViewById(R.id.spinner_test_postion);
        mPostion = getResources().getStringArray(R.array.postion)[0];
        mPostionSpinner.setOnItemSelectedListener(mPostionSpinnerListener);

        mMachineSpinner = (Spinner) findViewById(R.id.spinner_machine);
        mMachineSpinner.setOnItemSelectedListener(mMachineSpinnerListener);

        mStrengthSpiner = (Spinner) findViewById(R.id.spinner_strength);
        mStrengthSpiner.setOnItemSelectedListener(mStrengthSpinnerListener);

        mEditTextTanhua = (EditText) findViewById(R.id.edittext_tanhua);

        mEditTextFixStrength = (EditText) findViewById(R.id.edittext_fix_strength);

        mEditTextDate = (EditText) findViewById(R.id.edittext_date);

    }

    private OnItemSelectedListener mMachineSpinnerListener = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            if (arg2 == 0) {
                isMachine = true;
            } else {
                isMachine = false;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

    };


    private OnItemSelectedListener mStrengthSpinnerListener = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            String[] strengh = getResources().getStringArray(R.array.strength);
            mDesginStrength = strengh[arg2];
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }

    };


    private OnItemSelectedListener mAngleSpinnerListener = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            String[] angle = getResources().getStringArray(R.array.angle);
            mTestAngle = Integer.parseInt(angle[arg2]);
            Log.d(TAG, ".....mTestAngle = " + mTestAngle);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    };

    OnItemSelectedListener mPostionSpinnerListener = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            String[] postion = getResources().getStringArray(R.array.postion);

            if (postion.length > arg2) {
                mPostion = postion[arg2];
            }

            Log.d(TAG, ".....mPostion = " + mPostion);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    };


    public void start_test(View view) {
        Log.d(TAG, "start_test");

        String errorString = checkValue();

        if (!TextUtils.isEmpty(checkValue())) {

            Log.d(TAG, errorString);
            Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_LONG).show();
            return;
        }

        saveDataPreferences();


        Intent intent = new Intent(this, TestActivity.class);

        setData(intent);

        startActivity(intent);
    }


    private String checkValue() {

        String errorString = null;

        mConstructionName = mEditText_Construct.getText().toString().trim();

        if (TextUtils.isEmpty(mConstructionName)) {

            errorString = "请出入正确的构件名称。";
            return errorString;
        }

        String strNumber = mEditTextTestAreaNumber.getText().toString().trim();

        mTestNumber = 0;

        try {
            mTestNumber = Integer.parseInt(strNumber);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.d(TAG, "NumberFormatException");
        }

        if (mTestNumber < 5 || mTestNumber > 25) {
            errorString = "请出入正确的测区数。";
            return errorString;
        }

        String strTanHua = mEditTextTanhua.getText().toString().trim();

        mCarbonizeStrength = 0.0f;
        try {

            mCarbonizeStrength = Float.parseFloat(strTanHua);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.d(TAG, "NumberFormatException");
        }

        if (mCarbonizeStrength > 6.0 || mCarbonizeStrength < 0.0f) {
            errorString = "请出入正确的碳化强度参数";
            return errorString;
        }

        String fixStrength = mEditTextFixStrength.getText().toString().trim();

        mFixStrength = 0;

        try {
            mFixStrength = Integer.parseInt(fixStrength);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.d(TAG, "NumberFormatException");
            errorString = "请出入正确的“强度修正量”参数";
            return errorString;
        }

        mDate = mEditTextDate.getText().toString().trim();
        if (TextUtils.isEmpty(mDate)) {

            errorString = "请出入正确的“日期”";
            return errorString;
        }
        return errorString;
    }


}
