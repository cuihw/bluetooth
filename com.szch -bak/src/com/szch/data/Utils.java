package com.szch.data;

import com.szch.cpp.AlgorithmData;

public class Utils {
    
    static public float computerAverage(int[] data) {
        int sum = 0;
        
        // remove three max value.
        // remove three min value.
        AlgorithmData.sort(data, data.length);

        if (data != null) {
            for (int i = 3; i < data.length - 6; i ++) {
                sum = sum + data[i];
            }
        }
        return ((float)sum / (data.length - 6) );
    }

    // 标准差
    public double getStandardDevition(int[] array){
        int num = array.length;
        double sum = 0;
        float average = computerAverage(array);
        
        for(int i = 0;i < num;i++){
            sum += Math.sqrt(((double)array[i] -average) * (array[i] -average));
        }
        return (sum / (num - 1));
    }

}
