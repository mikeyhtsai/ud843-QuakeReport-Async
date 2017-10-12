package com.example.android.quakereport;

/**
 * Created by miketsai on 10/10/2017.
 */

public class Earthquake {
    private String mMagnitude;
    private String mLocation;
    private String mDate;

    public Earthquake(String magnitude, String location, String date)
    {
        mMagnitude = magnitude;
        mLocation = location;
        mDate = date;
    }

    public String getMagnitude(){return mMagnitude;}
    public String getLocation(){return mLocation;}
    public String getDate(){return mDate;}

}
