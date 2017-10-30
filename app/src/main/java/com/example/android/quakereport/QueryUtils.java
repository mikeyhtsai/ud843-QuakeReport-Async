package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.android.quakereport.EarthquakeActivity.LOG_TAG;


/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {

          Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    static ArrayList<Earthquake> extractEarthquakes(String urls) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        // Create URL object
        URL url = createUrl(urls);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject jsonObj = new JSONObject(jsonResponse);

            // Getting JSON Array node
            JSONArray features = jsonObj.getJSONArray("features");
            // build up a list of Earthquake objects with the corresponding data.
            for (int i=0; i<features.length();i++) {
                JSONObject c = features.getJSONObject(i);
                JSONObject p = c.getJSONObject("properties");
                // Extract the value for the key called "url"
                String url1 = p.getString("url");
                String mag = String.valueOf(p.getDouble("mag"));
                String location = p.getString("place");
             //   String time = String.valueOf(p.getInt("time"));
                long timeInMilliseconds = p.getInt("time");
                Date dateObj = new Date(timeInMilliseconds);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy");
                String dateToDisplay = dateFormatter.format(dateObj);
                earthquakes.add(new Earthquake(mag, location, dateToDisplay, url1));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
         //   QuakeShowErrorMsg("Failed to get JSON results from Server");
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
            return null;
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    static int getMagnitudeColor(String magnitude) {
        int iMagnitude = (int) Double.parseDouble(magnitude);
        int backColor = R.color.magnitude10plus;
        switch (iMagnitude){
            case 0:case 1:
                backColor = R.color.magnitude1;
                break;
            case 2:
            backColor = R.color.magnitude2;
            break;
            case 3:
            backColor = R.color.magnitude3;
            break;
            case 4:
            backColor = R.color.magnitude4;
            break;
            case 5:
            backColor = R.color.magnitude5;
            break;
            case 6:
            backColor = R.color.magnitude6;
            break;
            case 7:
            backColor = R.color.magnitude7;
            break;
            case 8:
            backColor = R.color.magnitude8;
            break;
            case 9:
            backColor = R.color.magnitude9;
            break;

        }

        return(backColor);
    }
}
