package com.szch;

import com.szch.data.TestData;
import com.szch.data.TestDataTransmit;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class TestResultActivity extends Activity {

    // 平均值
    // 角度修正
    // 侧面修正
    // 碳化修正
    // 换算值
    
    TestResultAdapter mTestResultAdapter;
    
    ListView mResultListView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_result);
        getData();

        initview();
    }

    private void initview() {

        mTestResultAdapter = new TestResultAdapter(TestDataTransmit.dataList, this);
        mResultListView = (ListView)findViewById(R.id.resultlv);
        mResultListView.setAdapter(mTestResultAdapter);

    }

    private void getData() {
        if (TestDataTransmit.dataList != null) {
            for (int i = 0; i < TestDataTransmit.dataList.size(); i++) {
                TestData testdata = TestDataTransmit.dataList.get(i);
                computer(testdata);
            }
        }
    }

    private void computer(TestData testdata) {
        testdata.getAverage();
        //角度修正
        testdata.getAngleFix();
        // 侧面修正
        testdata.getSurfaceFix();
        // 碳化修正
        testdata.getResult();
    }

}
