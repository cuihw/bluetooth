package com.szch;

import com.szch.data.TestDataTransmit;

import android.app.Activity;
import android.os.Bundle;

public class TestResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_result);
        

        getData();
    }

    private void getData() {
        if (TestDataTransmit.dataList != null) {

        }
    }

    

    

}
