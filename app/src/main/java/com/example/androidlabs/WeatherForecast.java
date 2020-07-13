package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {
    protected static final String URL_WEATHER ="http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";
    protected static final String URL_UV = "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";
    protected static final String URL_IMAGE = "http://openweathermap.org/img/w/";

    ImageView currentWeatherImageView = null;
    TextView currentTempTextView = null;
    TextView minTempTextView = null;
    TextView maxTEmpTextView = null;
    TextView UVRateTextView = null;
    ProgressBar progress = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        ForecastQuery networkThread = new ForecastQuery();
        networkThread.execute(URL_WEATHER);

        currentWeatherImageView = findViewById(R.id.currentWeather);
        currentTempTextView = findViewById(R.id.currentTemp);
        minTempTextView = findViewById(R.id.minTemp);
        maxTEmpTextView = findViewById(R.id.maxTemp);
        UVRateTextView = findViewById(R.id.UVRating);
        progress = findViewById(R.id.progressBar);
        progress.setVisibility(View.VISIBLE);
    }

    public class ForecastQuery extends AsyncTask <String, Integer, String> {
        private String UV = "";
        private String minTemp = "";
        private String maxTemp = "";
        private String currentTemp = "";
        private Bitmap bitMap;
        @Override
        protected String doInBackground(String... strings) {

            try {
                //get the string url:
                String myUrl = strings[0];
                //create a URL object of what server to contact:
                URL url = new URL(myUrl);
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //From part 3: slide 19
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");

                //From part 3, slide 20
                String iconName = null;

                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                while(eventType != XmlPullParser.END_DOCUMENT) {

                    if(eventType == XmlPullParser.START_TAG) {
                        //If you get here, then you are pointing at a start tag
                        if(xpp.getName().equalsIgnoreCase("Temperature")) {
                            currentTemp = xpp.getAttributeValue(null, "value");
                            publishProgress(25);
                            Thread.sleep(500);
                            minTemp = xpp.getAttributeValue(null, "min");
                            publishProgress(50);
                            Thread.sleep(500);
                            maxTemp = xpp.getAttributeValue(null, "max");
                            publishProgress(75);
                        }
                        else if(xpp.getName().equalsIgnoreCase("Weather")) {
                            iconName = xpp.getAttributeValue(null, "icon");
                            if(fileExistence(iconName + ".png")){
                                Log.i(iconName, "Weather image exists, read from file");
                                FileInputStream fis = null;
                                try {    fis = openFileInput(iconName + ".png");   }
                                catch (FileNotFoundException e) { e.printStackTrace();  }
                                bitMap = BitmapFactory.decodeStream(fis);

                            }else {
                                Log.i(iconName, "Weather image does not exist, download from URL");
                                URL imageUrl = new URL(URL_IMAGE + iconName + ".png");
                                urlConnection = (HttpURLConnection) imageUrl.openConnection();
                                urlConnection.connect();
                                int responseCode = urlConnection.getResponseCode();
                                if (responseCode == 200) {
                                    bitMap = BitmapFactory.decodeStream(urlConnection.getInputStream());
                                }

                                FileOutputStream outputStream  = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                                bitMap.compress(Bitmap.CompressFormat.PNG, 80, outputStream );
                                outputStream .flush();
                                outputStream .close();
                            }
                            publishProgress(100);
                        }
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable

                }
                //JSON CODE STARTS
                URL UVUrl = new URL(URL_UV);
                HttpURLConnection UVConnection = (HttpURLConnection) UVUrl.openConnection();
                response = UVConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line).append("\n");
                }
                String result = sb.toString();

                JSONObject jObject = new JSONObject(result);
                float value = (float) jObject.getDouble("value");
                UV = String.valueOf(value);
                Log.e("AsyncTask", "Found UV: " + UV);
            }


            catch (Exception e) {
                Log.e("AsyncTask", "Error");
            }
            return "Done";
        }

        //Type 2
        public void onProgressUpdate(Integer ... args)
        {
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(args[0]);
        }
        //Type3
        public void onPostExecute(String fromDoInBackground) {
            Log.i("HTTP", fromDoInBackground);
            progress.setVisibility(View.INVISIBLE);
            currentTempTextView.setText("Current: " + currentTemp + "C");
            minTempTextView.setText("Min: " + minTemp + "C");
            maxTEmpTextView.setText("Max: " + maxTemp + "C");
            UVRateTextView.setText("UV rating: " + UV );
            currentWeatherImageView.setImageBitmap(bitMap);
        }
        public boolean fileExistence(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();   }


    }
}