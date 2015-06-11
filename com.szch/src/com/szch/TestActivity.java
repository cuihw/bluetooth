package com.szch;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

public class TestActivity extends Activity {

    private String[] arrayData = {  
            "_", "_","_","_",
            "_", "_","_","_",
            "_", "_","_","_",
            "_", "_","_","_"};

    GridView mGridview;
    
    ArrayAdapter mArrayAdapter; 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_activity);
        
        initView();
    }

    private void initView() {
        mGridview = (GridView) findViewById(R.id.gridView1);

        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayData);

        mGridview.setAdapter(mArrayAdapter);
    }

}
