package com.szch;

import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.app.Activity;
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

    String mConstructionName;

    int mTestNumber;

    Spinner mAngleSpinner;

    int mTestAngle = 90;
    
    Spinner mPostionSpinner;

    String mPostion;
    
    Spinner mMachineSpinner;
    
    boolean isMachine;
    
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
        
    }
    
    OnItemSelectedListener mAngleSpinnerListener = new OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            String[] angle = getResources().getStringArray(R.array.angle);
            mTestAngle = Integer.parseInt(angle[arg2]);
            Log.d(TAG, ".....mTestAngle = " + mTestAngle);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            
        }};
        
     OnItemSelectedListener mPostionSpinnerListener  = new OnItemSelectedListener(){

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

        if (mTestNumber > 4 && mTestNumber < 26) {
            errorString = "请出入正确的测区数。";
            return errorString;
        }


        return errorString;
    }


}
