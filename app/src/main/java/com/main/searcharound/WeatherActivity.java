package com.main.searcharound;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import helperclasses.CurrentFragment;
import helperclasses.ForecastFragment;
import helperclasses.Singleton;
import helperclasses.Weather;
import helperclasses.WeatherJsonParser;

public class WeatherActivity extends AppCompatActivity {

    private static  Context sContext;
    private Toolbar mToolbar;
    private EditText mEditText;
    private ImageButton mImageButton;
    private static final String TAG="WeatherActivity";
    private static final String MY_WEATHER_KEY="YOUR APIXU API KEY";
    private static String asyncResponseString="";
    private Spinner mSpinnerDays;
    private String mChosenSpinnerOption="0";
    private List<Weather> weatherList=new ArrayList<>();
    private double la=0;
    private double lo=0;
    private String latitude="";
    private String longitude="";
    private LinearLayout mPagedLinearLayout;
    private FrameLayout mLinearLayoutForecast;
    private TextView noInternetTextView;
    public Intent newIntent(Context context)
    {
        sContext=context;
        Intent intent=new Intent(WeatherActivity.this,UserScreenActivity.class);
        return intent;

    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTheme(R.style.MyMaterialTheme);
        //catches coordinates to show a default place in case no city name is written
        la=getIntent().getDoubleExtra("lat",0);
        lo=getIntent().getDoubleExtra("lon",0);
        latitude+=la;
        longitude+=lo;
        mToolbar= findViewById(R.id.weather_toolbar);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.orange));
        mToolbar.setTitle(R.string.weather_string);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mEditText= findViewById(R.id.weather_edittext);
        mEditText.setSingleLine(true);
        mEditText.clearComposingText();
        mSpinnerDays= findViewById(R.id.spinner_days);
        mSpinnerDays.setHorizontalScrollBarEnabled(true);
        mPagedLinearLayout= findViewById(R.id. weather_linear_layout_paged);
        mLinearLayoutForecast= findViewById(R.id.weather_linear_layout_forecast);
        noInternetTextView=findViewById(R.id.no_internet_textview);

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,R.layout.spinner_text_view,getResources().getStringArray(R.array.spinners_days_values))
        {
            @Override
            public View getDropDownView(int position,View convertView,ViewGroup parent)
            {
                View view=super.getDropDownView(position,convertView,parent);
                view.setBackgroundColor(getResources().getColor(R.color.white));
                TextView tv=(TextView)view;
                tv.setTextColor(getResources().getColor(R.color.black));
                tv.setBackgroundColor(getResources().getColor(R.color.white));
                return view;
            }

        };
        //R.layout.spinner_text_view);//android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerDays.setDrawingCacheBackgroundColor(getResources().getColor(R.color.white));
        mSpinnerDays.setAdapter(arrayAdapter);
        mSpinnerDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) view).setTextColor(getResources().getColor(R.color.black));

                if (position==0)
                    mChosenSpinnerOption="Today";
                else
                    mChosenSpinnerOption="Tomorrow";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mChosenSpinnerOption="0";
            }
        });

        //DEFAULTS THE CITY WE ARE IN ATM, THEN ITD CHANGE WHEN WE TYPE A NEW ONE OR WE HIT SEARCH BUTTON
        if (isNetworkAvailable()) {

            mLinearLayoutForecast.setVisibility(View.VISIBLE);
            noInternetTextView.setVisibility(View.GONE);

            try {
                asyncResponseString = new FetchWeather().execute("https://api.apixu.com/v1/current.json?key=" + MY_WEATHER_KEY + "&q=" + latitude + "," + longitude).get();
                if (!asyncResponseString.equals("")) {
                    if (!weatherList.isEmpty())
                        weatherList.clear();
                    weatherList = new WeatherJsonParser(1).execute(asyncResponseString).get();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment weatherFragment=CurrentFragment.newFragment(weatherList.get(0));
                    fragmentManager.beginTransaction().replace(R.id.weather_linear_layout_forecast,weatherFragment).commit();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            mImageButton = findViewById(R.id.image_button_weather);
            mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String city = mEditText.getText().toString();
                    if (!city.equals("")) {
                        int option = -1;//means unused,1 for result on first try, 2 for secodn try
                        try {
                            if (mChosenSpinnerOption.equals("Today"))
                            { //if no days are selected
                                asyncResponseString = new FetchWeather().execute("https://api.apixu.com/v1/current.json?key=" + MY_WEATHER_KEY + "&q=" + city).get();

                                    if (asyncResponseString.equals(""))
                                    { //If answer is empty, no city was found. GET THE ANSWER AND GUESS CITY THEN TRY AGAIN!!!
                                        //city wasnt found so we try to use autocomplete
                                        asyncResponseString = new FetchWeather().execute("https://api.apixu.com/v1/search.json?key=" + MY_WEATHER_KEY + "&q=" + city).get();
                                        //if autocomplete didnt work
                                        if (asyncResponseString.equals(""))
                                        {
                                            //show city not found message
                                            WeatherActivity.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(WeatherActivity.this, "City not found", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        else
                                        {
                                            //if city was found, type" city=jsonCity then do the current search again
                                            option = 2;
                                            //this weatherjsonparser instance will only fetch a CITY name to store in Singleton class
                                            new WeatherJsonParser(option).execute(asyncResponseString).get();
                                            option = 1;
                                            String queriedCity = Singleton.getCityName();
                                            //?SEARCH INSTEAD OF CURRENT??
                                            asyncResponseString = new FetchWeather().execute("https://api.apixu.com/v1/current.json?key=" + MY_WEATHER_KEY + "&q=" + queriedCity).get();
                                            weatherList=null;
                                            weatherList = new WeatherJsonParser(option).execute(asyncResponseString).get();
                                            FragmentManager fragmentManager = getSupportFragmentManager();
                                            Fragment currentFragment=CurrentFragment.newFragment(weatherList.get(0));
                                            fragmentManager.beginTransaction().replace(R.id.weather_linear_layout_forecast,currentFragment).commit();
                                            hideKeyboard(WeatherActivity.this);
                                        }
                                    }
                                else
                                {
                                    option = 1;
                                    weatherList=null;
                                    weatherList = new WeatherJsonParser(option).execute(asyncResponseString).get();
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    Fragment currentFragment=CurrentFragment.newFragment(weatherList.get(0));
                                    fragmentManager.beginTransaction().replace(R.id.weather_linear_layout_forecast,currentFragment).commit();
                                    hideKeyboard(WeatherActivity.this);
                                }
                            }
                            else //if number of days is >0
                            {
                                asyncResponseString = new FetchWeather().execute("https://api.apixu.com/v1/forecast.json?key=" + MY_WEATHER_KEY + "&q=" + city + "&days=1").get();
                                    if (asyncResponseString.equals(""))
                                    { //If answer is empty, no city was found. GET THE ANSWER AND GUESS CITY THEN TRY AGAIN!!!
                                        //city wasnt found so we try to use autocomplete
                                        asyncResponseString = new FetchWeather().execute("https://api.apixu.com/v1/search.json?key=" + MY_WEATHER_KEY + "&q=" + city).get();
                                        //if autocomplete didnt work
                                        if (asyncResponseString.equals(""))
                                        {
                                            //show city not found message
                                            WeatherActivity.this.runOnUiThread(new Runnable()
                                            {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(WeatherActivity.this, "City not found", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        else
                                        {
                                            //if city was found, type" city=jsonCity then do the current search again
                                            option = 2;

                                            //this weatherjsonparser instance will only fetch a CITY name to store in Singleton class
                                            new WeatherJsonParser(option).execute(asyncResponseString).get();
                                            option = 3;
                                            String queriedCity = Singleton.getCityName();
                                            asyncResponseString = new FetchWeather().execute("https://api.apixu.com/v1/forecast.json?key=" + MY_WEATHER_KEY + "&q=" + queriedCity + "&days=1").get();
                                            weatherList=null;
                                            weatherList = new WeatherJsonParser(option).execute(asyncResponseString).get();
                                            FragmentManager fragmentManager = getSupportFragmentManager();
                                            Fragment forecastFragment=ForecastFragment.newFragment(weatherList.get(0));
                                            fragmentManager.beginTransaction().replace(R.id.weather_linear_layout_forecast,forecastFragment).commit();
                                            hideKeyboard(WeatherActivity.this);

                                        }
                                }
                                else
                                {
                                    option = 3;
                                    weatherList=null;
                                    weatherList = new WeatherJsonParser(option).execute(asyncResponseString).get();
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    Fragment forecastFragment=ForecastFragment.newFragment(weatherList.get(0));
                                    fragmentManager.beginTransaction().replace(R.id.weather_linear_layout_forecast,forecastFragment).commit();
                                    hideKeyboard(WeatherActivity.this);
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //No city is entered, request the location to the map and use it as q parameter. If not internet available then show "DISCONNECTED message
                        try {
                            asyncResponseString = new FetchWeather().execute("https://api.apixu.com/v1/current.json?key=" + MY_WEATHER_KEY + "&q=" + la + "," + lo).get();
                            if (!asyncResponseString.equals("")) {
                                weatherList=null;
                                weatherList = new WeatherJsonParser(1).execute(asyncResponseString).get();
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                Fragment currentFragment=CurrentFragment.newFragment(weatherList.get(0));
                                fragmentManager.beginTransaction().replace(R.id.weather_linear_layout_forecast,currentFragment).commit();
                                hideKeyboard(WeatherActivity.this);
                            }
                            else
                            {
                                WeatherActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        FancyToast.makeText(WeatherActivity.this, "No default city", Toast.LENGTH_SHORT, FancyToast.CONFUSING, false);
                                    }
                                });
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        }
        else
        {
            mLinearLayoutForecast.setVisibility(View.GONE);
            noInternetTextView.setVisibility(View.VISIBLE);
        }

    }

    //checks theres internet connection
    private boolean isNetworkAvailable() throws NullPointerException{
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private String getUrlString(String url) throws IOException
    {

        return new String(getUrlBytes(url));
    }
    private byte[] getUrlBytes(String urlString) throws IOException
    {


        URL url= new URL(urlString);
        HttpsURLConnection connection=(HttpsURLConnection)url.openConnection();

        try{
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            InputStream inputStream=connection.getInputStream();

            if(connection.getResponseCode()!=HttpsURLConnection.HTTP_OK)
            {
                throw new IOException(connection.getResponseMessage()+": with "+urlString);
            }

            int bytesRead=0;
            byte[] buffer=new byte[1024];
            //while the amount of read data is greater than 0 and its read into buffer[] from the inputStream...
            while ((bytesRead=inputStream.read(buffer))>0)
            {
                //write out by OUT into buffer
                out.write(buffer,0,bytesRead);//bytesread should be 1024. Basically writing blocks of 1024 bytes
            }
            out.close();//no more writing out
            return out.toByteArray();//return data to getUrlString
        }
        //no catch required as the method thrwos IOException whic is required when reading or writing
        finally {
            connection.disconnect();//close connection
        }
    }

    private class FetchWeather extends AsyncTask<String,Void,String>
    {
        String results="";
        int option=0;
        public FetchWeather(){}
        public FetchWeather(int option)
        {
            this.option=option;
        }
        @Override
        protected String doInBackground(String... strings) {
            String queryString=strings[0];
            try {
                results=getUrlString(queryString);
            }catch(IOException e){
            }
            finally
            {
                JSONObject jsonObject=null;

            }
            return results;
        }
    }

    private void hideKeyboard(Activity activity)
    {

        InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(),0);
    }
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        onStartNewActivity();
    }
    @Override
    public void finish() {
        super.finish();
        onLeaveThisActivity();
    }

    protected void onStartNewActivity()
    {
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
    }
    protected void onLeaveThisActivity() {
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }

}
