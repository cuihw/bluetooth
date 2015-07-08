package com.szch;

import java.util.List;

import com.szch.TestActivity.GridViewAdpter.ViewHolder;
import com.szch.data.TestData;
import com.szch.data.TestDataTransmit;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TestResultAdapter extends BaseAdapter {

    private String TAG = "TestResultAdapter";

    private LayoutInflater li;

    public List<TestData> dataList;

    private Context mContext;
    
    int[] colors = { Color.WHITE, Color.rgb(219, 238, 244) };//RGB颜色  

    public TestResultAdapter(List<TestData> data, Context context) {
        dataList = data;
        mContext = context;
        li = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {

        if (dataList != null) {
            return dataList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub

        if (dataList != null) {
            return dataList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;
        //Log.d(TAG, "position = " + position);

        if (convertView == null) {

            vh = new ViewHolder();
            convertView = li.inflate(R.layout.item_test_result, null);
            vh.textviewid = (TextView) convertView.findViewById(R.id.id);
            vh.textviewaverage = (TextView) convertView.findViewById(R.id.average);
            vh.textviewAngle = (TextView) convertView.findViewById(R.id.angle);
            vh.textviewSurface = (TextView) convertView.findViewById(R.id.ce_mian_fix);
            vh.textviewCarbon = (TextView) convertView.findViewById(R.id.tan_hua_fix);
            vh.textviewResult = (TextView) convertView.findViewById(R.id.result_item);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        TestData data = dataList.get(position);

        if (data != null) {
            if (vh == null) {
                Log.d(TAG, "ViewHolder is null");
            }
            vh.textviewid.setText("" + (position + 1));
            vh.textviewaverage.setText( "" + data.average);
            vh.textviewAngle.setText("" + data.angleFix);
            vh.textviewSurface.setText( "" + data.surfaceFix);
            vh.textviewCarbon.setText("" + data.carbonizeFix);
            vh.textviewResult.setText("" + data.result);
        }

        convertView.setBackgroundColor(colors[(position +1) % 2]);  
        return convertView;
    }

    class ViewHolder {
        TextView textviewid;
        TextView textviewaverage;
        TextView textviewAngle;
        TextView textviewSurface;
        TextView textviewCarbon;
        TextView textviewResult;
    }
}
