package com.szch.data;

import java.io.Serializable;

import com.szch.cpp.AlgorithmData;

public class TestData implements Serializable {

    public int[] data = new int[16];

    public int currentNumber = 0;
    
    public float average;
    
    public float angleFix;
    
    public int angleIndex;

    public float surfaceFix;

    public int surfaceIndex;

    public float carbonizeFix;
    
    public float  result;

    public boolean isMachine;

    public void getAverage() {
        // TODO Auto-generated method stub
        if (data != null) {
            average = Utils.computerAverage(data);
        }
    }

    public void getAngleFix() {
        if (average > 0.001) {
            angleFix = AlgorithmData.getAngleCor(average, angleIndex);     
        }
    }

    public void getSurfaceFix() {

        if (average > 0.001) {
            surfaceFix = AlgorithmData.getSurfCor(average, surfaceIndex);
        }
    }

    //getRegStrg(float Cardepth, float Modval, boolean Pumpflag, 
    // char ht_jd0, char ht_mian0)
    public void getResult() {
        // TODO Auto-generated method stub
        result = AlgorithmData.getRegStrg(carbonizeFix, average, isMachine,
                angleIndex, surfaceIndex);
    }

    
}
