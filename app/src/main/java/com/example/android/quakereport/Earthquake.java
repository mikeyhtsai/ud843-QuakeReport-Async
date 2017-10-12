package com.example.android.quakereport;

/**
 * Created by miketsai on 10/10/2017.
 */

public class Earthquake {
    private String mMagnitude;
    private String mLocation;
    private String mDate;
    /** Website URL of the earthquake */
    private String mUrl;

    public Earthquake(String magnitude, String location, String date, String url)
    {
        mMagnitude = magnitude;
        mLocation = location;
        mDate = date;
        mUrl = url;
    }

    public String getMagnitude(){return mMagnitude;}
    public String getLocation(){return mLocation;}
    public String getDate(){return mDate;}
    public String getUrl(){return mUrl;}

}
