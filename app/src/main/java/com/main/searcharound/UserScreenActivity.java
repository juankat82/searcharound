package com.main.searcharound;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import helperclasses.CustomInfoWindowAdapter;
import helperclasses.JsonParser;
import helperclasses.Place;
import helperclasses.Singleton;


public class UserScreenActivity extends AbstractStartingClass implements OnMapReadyCallback, android.location.LocationListener, GoogleMap.OnMarkerClickListener  {

    private static final int GPS_REQUEST_CODE = 150;
    private static final int REQUEST_ERROR = 36;
    private Context mContext;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToogle;
    private static final String TAG = "UserScreenFragment";
    private Intent intent;
    private Toolbar mToolbar;
    private EditText mSearchEditText;
    private final int MY_ACTIVITY_ID = 100;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private ImageButton mPopUpMenuButton;
    private ImageButton mGoSearchButton;
    private Activity thisActivity;
    private LatLng myLatLng;
    private String[] mPermissionString = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
    private SupportMapFragment supportMapFragment;
    private static GoogleMap mGoogleMap;
    private LocationRequest mLocationRequest;
    private boolean isGpsEnabled = false;
    private boolean network_enabled = false;
    private LocationManager mLocationManager;
    private Intent gpsIntent;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private boolean marketVisible;
    private static final String MY_GEOCODING_API_KEY="YOUR GEOCODING API KEY";
    private List<Place> listOfPlaces=new ArrayList<>();
    private static SharedPreferences sSharedPreferences;
    private static SharedPreferences.Editor editor;
    private static Singleton sSingleton;
    private String stringToSend=new String();
    private boolean isMapReady=false;
    private static FrameLayout sFrameLayout;
    private static boolean isFrameLayoutVisible=false;
    private BottomNavigationView mBottomNavigationView;
    private int mBottomNavigationChoice=1;//1 will be default, means WALK
    private String mJsonQueryAnswerDirections;
    private Polyline mPolyLine;
    private boolean isInfoWindowShown = false;
    private CustomInfoWindowAdapter mCustomInfoWindowAdapter;
    private Marker originMark;
    private boolean routeGotTraced=false;


    public static UserScreenActivity getMe(Context context)
    {
        return new UserScreenActivity();
    }
    public UserScreenActivity(){}
    private boolean checkPlayServices() {

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();

        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public Intent newIntent(Context context) {
        mContext=context;
        return new Intent(mContext,UserScreenActivity.class);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (sSingleton==null)
            sSingleton=Singleton.get(mContext);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sSharedPreferences=Singleton.getSharedPreferences();
        while (sSharedPreferences==null)
            sSharedPreferences=Singleton.getSharedPreferences();
        editor=sSharedPreferences.edit();
        if (!sSharedPreferences.contains("isDecimal"))
        {
            editor.putBoolean("isDecimal",false).apply();
        }

        setContentView(R.layout.user_screen_layout);
        mCustomInfoWindowAdapter=new CustomInfoWindowAdapter(getLayoutInflater());

        //Checks google play exists
        checkPlayServices();

        thisActivity = UserScreenActivity.this;
        setGpsStatus();
        gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setTheme(R.style.MyMaterialTheme);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.orange));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sFrameLayout=findViewById(R.id.response_frame_layout);
        mBottomNavigationView=findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.walk_option:
                                mBottomNavigationChoice=1;
                                break;
                            case R.id.drive_option:
                                mBottomNavigationChoice=2;
                                break;
                            case R.id.bus_option:
                                mBottomNavigationChoice=3;
                                break;
                            case R.id.train_option:
                                mBottomNavigationChoice=4;
                                break;
                            case R.id.tube_option:
                                mBottomNavigationChoice=5;
                                break;
                        }
                        Singleton.setWay(mBottomNavigationChoice);
                        if (Singleton.getStartAndEndPoints()!=null)
                            changePolyline(Singleton.getMarkerList().get(Singleton.getMarkerList().size()-1));
                        return true;
                    }
                });
        Singleton.setContext(getApplicationContext());
        /////////////////////////////////////////////////////////////////

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.nav_settings:
                        intent = new Intent(UserScreenActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_weather:
                        intent = new Intent(UserScreenActivity.this, WeatherActivity.class);
                        if (mGoogleMap.getMyLocation()==null)
                            intent.putExtra("lat",51.508530);
                        else
                            intent.putExtra("lat",mGoogleMap.getMyLocation().getLatitude());

                        if (mGoogleMap.getMyLocation()==null)
                            intent.putExtra("lon",-0.076132);
                        else
                            intent.putExtra("lon",mGoogleMap.getMyLocation().getLongitude());
                        startActivity(intent);
                        return true;
                    case R.id.nav_logout:
                        intent = new Intent(UserScreenActivity.this, LogoutActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_about:
                        intent = new Intent(UserScreenActivity.this, AboutActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
        mPopUpMenuButton = findViewById(R.id.narrow_search_image_button);
            mPopUpMenuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LatLng latLng;
                    try {
                        latLng = new LatLng(mGoogleMap.getMyLocation().getLatitude(), mGoogleMap.getMyLocation().getLongitude());
                        isMapReady = true;
                    } catch (NullPointerException e) {
                    }

                    if (isMapReady == true) {
                        final GoogleMap map;
                        GoogleMap map2 = Singleton.getGoogleMap();

                        Intent searchIntent = new Intent(UserScreenActivity.this, NarrowSearchActivity.class);
                        startActivityForResult(searchIntent, MY_ACTIVITY_ID);
                    }
                }
            });


        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        mSearchEditText =  findViewById(R.id.search_edit_text);
        mSearchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    hideKeyboard();
            }
        });
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (mSearchEditText.getText().equals(getResources().getString(R.string.invalid_coordinates)))
                {
                    mSearchEditText.setText("");
                }
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (mSearchEditText.getText()!=null)
                    {
                        String str=mSearchEditText.getText().toString();
                        try {
                            performSearch(str);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mSearchEditText.setText("");
                    }
                }
                return false;
            }
        });

        mGoSearchButton=findViewById(R.id.go_image_button);
        mGoSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOfPlaces.clear();
                if (mSearchEditText.getText()!=null)
                {
                    String str=mSearchEditText.getText().toString();
                    try {
                        performSearch(str);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mDrawerLayout = findViewById(R.id.user_screen_drawer_layout);
        mActionBarDrawerToogle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_string, R.string.close_string) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mActionBarDrawerToogle);
        mActionBarDrawerToogle.syncState();
    }

    public static FrameLayout getFrameLayout()
    {
        return sFrameLayout;
    }
    public static void setFrameLayoutVisible()
    {
        animateViewVisibility(sFrameLayout,View.VISIBLE);
        isFrameLayoutVisible=true;
    }
    public static void setFrameLayoutInVisible()
    {
        animateViewVisibility(sFrameLayout,View.GONE);
        isFrameLayoutVisible=false;
    }

    public static void animateViewVisibility(final View view, final int visibility)
    {
        // cancel runnning animations and remove and listeners
        view.animate().cancel();
        view.animate().setListener(null);

        // animate making view visible
        if (visibility == View.VISIBLE)
        {
            view.animate().alpha(1f).start();
            view.setVisibility(View.VISIBLE);
        }
        // animate making view hidden (HIDDEN or INVISIBLE)
        else
        {
            view.animate().setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    view.setVisibility(visibility);
                }
            }).alpha(0f).start();
        }
    }
    public boolean getFrameLayoutVisibility()
    {
        return isFrameLayoutVisible;
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        while(googleMap==null){}
        if (mGoogleMap == null) {
            mGoogleMap = googleMap;
            Singleton.setMap(mGoogleMap);
        }

        mGoogleMap.setInfoWindowAdapter(mCustomInfoWindowAdapter);//new CustomInfoWindowAdapter(LayoutInflater.from(UserScreenActivity.this)));
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

            }
        });
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            if (ActivityCompat.checkSelfPermission(thisActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(thisActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                         ActivityCompat.requestPermissions(UserScreenActivity.this,mPermissionString,MY_PERMISSIONS_REQUEST_LOCATION);
            else
                checkLocationPermissions();

        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        marketVisible=true;
        //        //left, top, right, and bottom
        mGoogleMap.setPadding(0, mSearchEditText.getBottom() + 120, 0, 0);
        mGoogleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                Location targetLocation = new Location("");//provider name is unnecessary
                targetLocation.setLatitude(latLng.latitude);//your coords of course
                targetLocation.setLongitude(latLng.longitude);
                Singleton.setLocation(targetLocation);
                mGoogleMap.clear();
                myLatLng=latLng;
                Marker markerMe=mGoogleMap.addMarker(new MarkerOptions().position(myLatLng).title("YOU ARE HERE").snippet("YOUR ARE HERE").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)).visible(true));
                markerMe.setTag(new String("-1"));

                Singleton.setIsMarkerOn(true);
                CameraPosition cameraPosition=new CameraPosition.Builder().target(myLatLng).zoom(5).build();
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),null);
                mGoogleMap.setOnMyLocationChangeListener(null);
            }
        });

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                hideKeyboard();
                Singleton.setStartAndEndPoints(null);
                Singleton.setCenterDot(latLng);

                setGpsStatus();
                enableGps();
                mGoogleMap.clear();
                routeGotTraced=false;
                setFrameLayoutInVisible();
                myLatLng=latLng;
                Location targetLocation = new Location("");//provider name is unnecessary
                targetLocation.setLatitude(myLatLng.latitude);//your coords of course
                targetLocation.setLongitude(myLatLng.longitude);
                Singleton.setLocation(targetLocation);
                Singleton.setIsMarkerOn(true);

                Marker marker=mGoogleMap.addMarker(new MarkerOptions().position(myLatLng).snippet("HERE").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                marker.setTag(new String("-1"));
                marketVisible=true;
                CameraPosition cameraPosition=new CameraPosition.Builder().target(myLatLng).zoom(mGoogleMap.getCameraPosition().zoom).build();
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),null);
                marketVisible=true;
            }
        });
        mGoogleMap.setOnMarkerClickListener(this);
        isMapReady=true;


    }

    public void enableGps()
    {
        if(!isGpsEnabled && !network_enabled) {
            new AlertDialog.Builder(UserScreenActivity.this).setTitle(R.string.start_gps_question_title).setMessage(R.string.get_gps_enabled_string)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(gpsIntent, GPS_REQUEST_CODE);
                            isGpsEnabled=true;
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new AlertDialog.Builder(UserScreenActivity.this).setTitle(R.string.app_will_close_string).setMessage(R.string.are_you_sure_string)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enableGps();
                                        }
                                    }).create().show();
                        }

                    }).create().show();
        }
    }
    public void setGpsStatus()
    {
        Log.i(TAG,"IsgpsEnabled: "+isGpsEnabled+"\nIs network enabled: "+network_enabled);
        mLocationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);

        isGpsEnabled= mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    //If permissions were not granted before, do it now
    private void checkLocationPermissions() {
        //Show explanation
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(UserScreenActivity.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                    .setTitle("Location Permission Needed")
                    .setMessage("Accept to enable location service")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(UserScreenActivity.this,mPermissionString,MY_PERMISSIONS_REQUEST_LOCATION);
                            mGoogleMap.setMyLocationEnabled(true);
                        }
                    })
                    .create().show();
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(UserScreenActivity.this,mPermissionString,MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    private void performSearch(String distance, String measure,String[] keyArray,int[] valuesArray ) {
        mSearchEditText.clearFocus();
        hideKeyboard();
    }
    private void performSearch(String string) throws IOException {
        mSearchEditText.clearFocus();
        int decision=0;
        hideKeyboard();
        String latitude="0";
        String longitude="0";
        if (!isGpsEnabled && !network_enabled)
        {
            setGpsStatus();
            enableGps();
        }
        else
        {
            if (marketVisible) //perform search around the marker
            {
                if (!string.equals(""))
                {
                    String[] stringArray=string.split(":");
                    String[] address=string.split(" ");

                    String category=new String();

                    if (myLatLng!=null)
                    {
                        latitude = String.valueOf(myLatLng.latitude);
                        longitude = String.valueOf(myLatLng.longitude);


                    }
                    if (stringArray.length>1) //has divided by : so we assume we entered coordinates
                        decision=1;
                    else{ //...otherwise...
                        if (address.length>1)//..theres more than just one word. Its assumed we divided by " "
                            decision=3;//more than 1 word means a place name
                        else//just one word means we are entering a category to refine search
                        {
                            decision=2;
                            category=new String(string);
                        }
                    }
                    if (decision==1) //search by Coordinates
                    {
                        String[] coordArray = new String[2];
                        try {
                            coordArray[0] = stringArray[0];
                            coordArray[1] = stringArray[1];

                        } catch (Exception e) {

                            Toast.makeText(UserScreenActivity.this, "Invalid Coordinates(Eg: 25:25)", Toast.LENGTH_SHORT).show();
                        }
                        //Send url to get data back from GOOGLE PLACES API
                        stringToSend="https://maps.googleapis.com/maps/api/geocode/json?latlng="+coordArray[0]+","+coordArray[1]+"&key="+MY_GEOCODING_API_KEY+"1";
                    }
                    if(decision==2)//SEARCH BY CATEGORY
                    {
                        //one single result
                        stringToSend="https://maps.googleapis.com/maps/api/place/textsearch/json?query="+string+"&location="+latitude+","+longitude+"&strictbounds&fields=place_id,name,formatted_address&rankedby=distance&key="+MY_GEOCODING_API_KEY+"2";
                        //lots of results, guesses from your input. Textsearch uses your string, wont autocmplete
                    }
                    if (decision==3)//SEARCH BY NAME
                    {
                        String str="";
                        for (String s: address)
                            str+=s+"+";
                        stringToSend="https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+str+"&location="+latitude+","+longitude+"&radius=2000&fields=place_id,name,formatted_address&sessiontoken=12345678&key="+MY_GEOCODING_API_KEY+"3";
                    }
                   new FetchPlacesTask().execute(stringToSend);
                }
            }
            else //perform search around yourself
            {
                if (!string.equals(""))
                {

                    String[] stringArray=string.split(":");
                    String[] address=string.split(" ");
                    String category=new String();

                    if (mGoogleMap.getMyLocation()!=null)
                    {
                        latitude = String.valueOf(mGoogleMap.getMyLocation().getLatitude());
                        longitude = String.valueOf(mGoogleMap.getMyLocation().getLongitude());
                    }

                    if (stringArray.length>1) //has divided by : so we assume we entered coordinates
                        decision=1;
                    else{ //...otherwise...
                        if (address.length>1)//..theres more than just one word. Its assumed we divided by " "
                            decision=3;//more than 1 word means a place name
                        else//just one word means we are entering a category to refine search
                        {
                            decision=2;
                            category=new String(string);
                        }
                    }

                    if (decision==1) //search by Coordinates
                    {
                        String[] coordArray = new String[2];
                        try {
                            coordArray[0] = stringArray[0];
                            coordArray[1] = stringArray[1];

                        } catch (Exception e) {

                            Toast.makeText(UserScreenActivity.this, "Invalid Coordinates(Eg: 25:25)", Toast.LENGTH_SHORT).show();
                        }

                        //Send url to get data back from GOOGLE PLACES API
                        stringToSend="https://maps.googleapis.com/maps/api/geocode/json?latlng="+coordArray[0]+","+coordArray[1]+"&key="+MY_GEOCODING_API_KEY+"1";
                    }
                    if(decision==2)//SEARCH BY CATEGORY
                    {
                        //one single result
                        stringToSend="https://maps.googleapis.com/maps/api/place/textsearch/json?query="+string+"&location="+latitude+","+longitude+"&strictbounds&fields=place_id,name,formatted_address&rankedby=distance&key="+MY_GEOCODING_API_KEY+"2";
                    }
                    if (decision==3)//SEARCH BY NAME
                    {
                        String str="";
                        for (String s: address)
                            str+=s+"+";
                        stringToSend="https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+str+"&location="+latitude+","+longitude+"&radius=2000&fields=place_id,name,formatted_address&sessiontoken=12345678&key="+MY_GEOCODING_API_KEY+"3";
                    }
                    new FetchPlacesTask().execute(stringToSend);
                }
            }
        }
    }

    //Params, Progress, Result
    private class FetchPlacesTask extends AsyncTask<String,Void,Void>
    {
        String option="";
        String results="";
        JsonParser mJsonParser;
        @Override
        protected Void doInBackground(String... params)
        {
            option=String.valueOf(params[0].charAt(params[0].length()-1));
            String queryString=params[0].substring(0,params[0].length()-1);

            try {
                results=getUrlString(queryString);
            }
            catch(Exception e){}

            mJsonParser=new JsonParser(results,option,thisActivity,stringToSend,mSearchEditText.getText().toString(),mContext);
            mJsonParser.execute();
            try
            {
                listOfPlaces=mJsonParser.getList();
            }
            catch(NullPointerException ex)
            {
                thisActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FancyToast.makeText(UserScreenActivity.this,"No result",Toast.LENGTH_SHORT,FancyToast.INFO,false).show();
                    }
                });
            }
            return null;
        }
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

    private void hideKeyboard() {
        InputMethodManager in = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

//        if (resultCode != NarrowSearchActivity.RESULT_CODE) {
//            return;
//        }

        //GETS DATA FROM SEARCHACTIVITY
        if (requestCode == MY_ACTIVITY_ID) {
            String keyArray = (String)data.getStringExtra("prejson");
            new FetchPlacesTask().execute(keyArray);
        }

        if (requestCode==GPS_REQUEST_CODE)
        {
            if (ActivityCompat.checkSelfPermission(thisActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(thisActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    Log.i(TAG,"permissions are granted");
                ActivityCompat.requestPermissions(thisActivity, mPermissionString, MY_PERMISSIONS_REQUEST_LOCATION);
                isGpsEnabled=true;
                network_enabled=true;
            }
        }
    }

    //Top menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToogle.onOptionsItemSelected(item) || item.getItemId() >= 0)
            return true;//true means no more processing
        else
            return mActionBarDrawerToogle.onOptionsItemSelected(item);
    }

    //override activity start and finish for the animations
    @Override
    public void startActivity(Intent intent) {
        String extra=intent.getStringExtra("check");

        if (extra==null)
            extra="0";

        if (extra.equals("1"))
        {

            super.startActivity(intent);
        }
        else
            {
            super.startActivity(intent);
            onStartNewActivity();
        }
    }

    @Override
    public void finish() {
        super.finish();
        onLeaveThisActivity();
    }

    protected void onStartNewActivity() {
        overridePendingTransition(R.anim.enter_from_bottom, R.anim.exit_to_top);
    }

    protected void onLeaveThisActivity() {
        overridePendingTransition(R.anim.enter_from_top, R.anim.exit_to_bottom);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int errorCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = apiAvailability.getErrorDialog(this, errorCode, REQUEST_ERROR, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog)
                {
                    //Leave if service unavailable
                    finish();
                }
            });
            errorDialog.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==MY_PERMISSIONS_REQUEST_LOCATION && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mGoogleMap.setMyLocationEnabled(true);
            }
            if (!isGpsEnabled && !network_enabled)
            {
                setGpsStatus();
                enableGps();
            }
        }
        else
        {
           //Permission disabled
            Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        try{
            mGoogleMap.setMyLocationEnabled(true);
        }catch(SecurityException e){}
        mGoogleMap.clear();
        MarkerOptions markerOptions1=new MarkerOptions();
        markerOptions1.position(new LatLng(location.getLatitude(),location.getLongitude()));
        Singleton.setLocation(location);
        markerOptions1.title(getResources().getString(R.string.you_are_here_string));
        mGoogleMap.addMarker(markerOptions1);
        CameraPosition cameraPosition=new CameraPosition.Builder().target(myLatLng).zoom(5).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),null);
        try{
            mGoogleMap.setMyLocationEnabled(false);
        }catch(SecurityException e){}
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public boolean onMarkerClick(Marker marker) { //here we perform the route tracing on the map, given 2 points
        setFrameLayoutInVisible();
        if (marker.getTag().equals("-1"))
        {
            if (routeGotTraced){
                FancyToast.makeText(UserScreenActivity.this,"Time: "+Singleton.getTimeTravel()+" minutes",Toast.LENGTH_SHORT,FancyToast.INFO,false).show();
            }
            else
                FancyToast.makeText(UserScreenActivity.this,"Type the name of a place to route",Toast.LENGTH_SHORT,FancyToast.INFO,false).show();
        }
        else {
            setFrameLayoutVisible();
            if (!isInfoWindowShown)
            {
                marker.showInfoWindow();
                isInfoWindowShown = true;
            } else {
                marker.hideInfoWindow();
                setFrameLayoutInVisible();
                isInfoWindowShown = false;
            }
        }
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                changePolyline(marker);
            }
        });

        return true;
    }
    public void changePolyline(Marker marker)
    {
        String tag=(String)marker.getTag();
        List<List<LatLng>> polylineList=new ArrayList<>();
        List<LatLng> latLangPoints=new ArrayList<>();
       // List<Marker> markerList=Singleton.getMarkerList();

        if (!latLangPoints.isEmpty())
            latLangPoints.clear();

        if (marker.isVisible() && marker.getTag().equals("-1")) {
            Location targetLocation = new Location("");//provider name is unnecessary
            targetLocation.setLatitude(mGoogleMap.getMyLocation().getLatitude());//your coords of course
            targetLocation.setLongitude(mGoogleMap.getMyLocation().getLongitude());
            Singleton.setLocation(targetLocation);
            myLatLng=new LatLng(mGoogleMap.getMyLocation().getLatitude(),mGoogleMap.getMyLocation().getLongitude());
            marker.remove();
            Singleton.setIsMarkerOn(false);
            marketVisible = false;

            try {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(mGoogleMap.getMyLocation().getLatitude(), mGoogleMap.getMyLocation().getLongitude())).zoom(5).build();
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), null);
            } catch (Exception e) {
                thisActivity.runOnUiThread(() -> FancyToast.makeText(UserScreenActivity.this, "Map isnt ready yet", Toast.LENGTH_SHORT, FancyToast.ERROR, false).show());
            }
        }
        //here is where we perform the call to server and then draw the line, depending on whether we chose car, walk or any other way (walk by default)
        if (!tag.equals("-1"))
        {
            if (mPolyLine!=null)
            {
                mPolyLine.remove();
            }
            List<LatLng> originEndPoints=new ArrayList<>();
            marker.hideInfoWindow();
            UserScreenActivity.setFrameLayoutVisible();
            LatLng initialPoint=Singleton.getCenterDot();
            String initX=String.valueOf(initialPoint.latitude);
            String initX2=String.valueOf(initialPoint.longitude);
            LatLng finalPoint=marker.getPosition();
            String initY=String.valueOf(finalPoint.latitude);
            String initY2=String.valueOf(finalPoint.longitude);

            originEndPoints.add(initialPoint);
            originEndPoints.add(finalPoint);

            if (Singleton.getStartAndEndPoints()==null)
                Singleton.setStartAndEndPoints(originEndPoints);

//            now route
            String transitMode="";
            String travelMode="";
            switch(mBottomNavigationChoice)
            {
                case 1:
                    travelMode="walking";
                    transitMode="";
                    break;
                case 2:
                    travelMode="driving";
                    transitMode="";
                    break;
                case 3:
                    travelMode="";
                    transitMode="bus";
                    break;
                case 4:
                    travelMode="";
                    transitMode="train|tram";
                    break;
                case 5:
                    travelMode="";
                    transitMode="tube";
                    break;
            }
            String units=Singleton.isDecimal()?"metric":"imperial";
            String trafficModel="best_guess";

            String transitRoutingPreferences="";
            if (travelMode.equals("walking"))
                transitRoutingPreferences="less_walking";
            else
                transitRoutingPreferences="fewer_transfers";

            String queryString="https://maps.googleapis.com/maps/api/directions/json?origin="+initX+","+initX2+"&destination="+initY+","+initY2+"&mode="+travelMode
                    +"&units="+units+"&departure_time=now&traffic_model="+trafficModel+"&transit_mode="+transitMode+"&transit_routing_preference="+transitRoutingPreferences
                    +"&key="+MY_GEOCODING_API_KEY;

            try {
                polylineList=null;
                polylineList=new FetchDirections().execute(queryString).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            for (int i=0;i<polylineList.size();i++)
            {
                for (int j=0;j<polylineList.get(i).size();j++)
                {
                    LatLng ltlng=polylineList.get(i).get(j);//new LatLng(polylineList.get(i).get(j).latitude,polylineList.get(i).get(j).longitude);
                    latLangPoints.add(ltlng);
                }
            }

            if (latLangPoints.size()!=0)
            {

                Marker markerClone=marker;
                String destinationTag=new String((String)markerClone.getTag());
                mGoogleMap.clear();

                //destination
                MarkerOptions destinationMarker = new MarkerOptions();
                destinationMarker.position(markerClone.getPosition());
                destinationMarker.title(markerClone.getTitle());
                destinationMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                Marker mark=mGoogleMap.addMarker(destinationMarker);
                mark.setTag(destinationTag);

                List<Marker> destinationMark=new ArrayList<>(1);
                destinationMark.add(mark);
                Singleton.setMarkerList(destinationMark);
                //start point
                MarkerOptions originMarker = new MarkerOptions();
                originMarker.position(latLangPoints.get(0));
                originMarker.title("Origin point");
                originMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                originMark=mGoogleMap.addMarker(originMarker);
                originMark.setTitle("Time: "+Integer.toString(Singleton.getTimeTravel())+" minutes");
                mGoogleMap.setInfoWindowAdapter(null);
                originMark.showInfoWindow();
                routeGotTraced=true;
                mGoogleMap.setInfoWindowAdapter(mCustomInfoWindowAdapter);
                originMark.setTag(new String("-1"));


                mPolyLine = mGoogleMap.addPolyline(new PolylineOptions().geodesic(false).visible(true).clickable(false).width(20f).color(getResources().getColor(R.color.orange)).addAll(latLangPoints));

                //mGoogleMap.setMaxZoomPreference(8);

                CameraPosition cameraPosition2 = new CameraPosition.Builder().target(latLangPoints.get(0)).zoom(5).build();
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2),null);
            }
            else
            {
                routeGotTraced=false;
                FancyToast.makeText(UserScreenActivity.this,"No route available",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
            }
        }
    }
    private class FetchDirections extends AsyncTask<String, Void, List<List<LatLng>>> //decodes lines and gets LatLngs
    {
        JSONObject directionsBaseObject;
        List<String> polylines=new ArrayList<>();
        List<List<LatLng>> polylineList=new ArrayList<>();//this is a bidimensional list
        List<LatLng> latLngList=new ArrayList<>();

        @Override
        protected List<List<LatLng>> doInBackground(String... strings) {
            String value=strings[0];


            if (polylines.size()!=0)
                polylines.clear();
            if (polylineList.size()!=0)
                polylineList.clear();
            if (latLngList.size()!=0)
                latLngList.clear();

            try {
                mJsonQueryAnswerDirections=getUrlString(value);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                directionsBaseObject=new JSONObject(mJsonQueryAnswerDirections);
                polylines.addAll(createPolyline(directionsBaseObject));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (String line: polylines)
            {
                List<LatLng> latLng=PolyUtil.decode(line);
                polylineList.add(latLng);
            }
            return polylineList;
        }
        private List<String> createPolyline(JSONObject directionsBaseObject) throws JSONException
        {
            List<String> pointLine=new ArrayList<>();
            JSONArray routeSteps=directionsBaseObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
            JSONObject legObject=(JSONObject) routeSteps.get(0);
            JSONArray stepsArray=legObject.getJSONArray("steps");
            int minutes=0;

            for (int i=0;i<stepsArray.length();i++)
            {
                JSONObject stepObject=(JSONObject) stepsArray.get(i);
                String time=stepObject.getJSONObject("duration").get("text").toString();
                minutes+=Integer.parseInt(time.split(" ")[0]);
                String points=stepObject.getJSONObject("polyline").get("points").toString();
                pointLine.add(points);
            }
            Singleton.setTimeTravel(minutes);
            return pointLine;
        }
    }
    @Override
    public void onBackPressed()
    {
        if (getFrameLayoutVisibility()==true)
        {
            setFrameLayoutInVisible();
        }
        else
        {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UserScreenActivity.super.onBackPressed();
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }
    @Override
    public void onDestroy()
    {
        System.exit(0);
        super.onDestroy();
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {}
}
