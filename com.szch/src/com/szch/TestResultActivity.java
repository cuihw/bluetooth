package com.szch;

import java.util.List;

import com.szch.data.TestData;
import com.szch.data.TestDataTransmit;
import com.szch.data.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class TestResultActivity extends Activity {

    // 平均值
    // 角度修正
    // 侧面修正
    // 碳化修正
    // 换算值
    
    TestResultAdapter mTestResultAdapter;
    
    ListView mResultListView;

    TextView mSummaryView;
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

        mSummaryView = (TextView)findViewById(R.id.sum_result);

        // sum the result
        float summaryMin = getMinimum(TestDataTransmit.dataList);
        float summaryAverage = getAverage(TestDataTransmit.dataList);
        float summaryStandardDevition = getStandardDevition(TestDataTransmit.dataList);
        float summaryResult = getResult(TestDataTransmit.dataList);

        mSummaryView.setText("强度最小值: " + summaryMin
                + "\n强度平均值: " + summaryAverage
                + "\n强度标准差: " + summaryStandardDevition
                + "\n强度推定值: " + summaryResult);
    }

    private void getData() {
        if (TestDataTransmit.dataList != null) {
            for (int i = 0; i < TestDataTransmit.dataList.size(); i++) {
                TestData testdata = TestDataTransmit.dataList.get(i);
                computer(testdata);
            }
        }
    }

    private float getResult(List<TestData> dataList) {
        float result = 0;
        if (dataList != null && dataList.size() > 0) {

            for (int i = 0; i < dataList.size(); i++) {
                result += dataList.get(i).result;
            }
            result = result/dataList.size();
        }
        return result;
    }

    private float getStandardDevition(List<TestData> dataList) {
        float standardDevition = 0;
        if (dataList != null && dataList.size() > 0) {
            float[] array = new float[dataList.size()];

            for (int i = 0; i < dataList.size(); i++) {
                array[i] = dataList.get(i).result;
            }
            standardDevition = Utils.getStandardDevition(array);
        }
        return standardDevition;
    }

    private float getAverage(List<TestData> dataList) {
        float average = 0;
        if (dataList != null && dataList.size() > 0) {
            float sum = 0;
            for (int i = 0; i < dataList.size(); i++) {
                sum += dataList.get(i).result;
            }
            average = sum / dataList.size();
        }
        return average;
    }

    private float getMinimum(List<TestData> dataList) {
        float minimum = 100;
        if (dataList != null && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                if (minimum > dataList.get(i).result) {
                    minimum = dataList.get(i).result;
                }                
            }
        } else {
            minimum = 0;
        }
        
        return minimum;
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
