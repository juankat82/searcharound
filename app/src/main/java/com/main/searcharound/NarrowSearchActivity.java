package com.main.searcharound;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.shashank.sony.fancytoastlib.FancyToast;

import helperclasses.Singleton;

public class NarrowSearchActivity extends AppCompatActivity implements LocationListener {

    private static Context sContext;
    public final static int RESULT_CODE = 4;
    private Toolbar mToolbar;
    private Button mCancelButton;
    private Button mSearchButton;
    private RadioGroup mRadioGroup;
    private RadioButton mBankRadioButton;
    private RadioButton mDinningRadioButton;
    private RadioButton mPostOfficeRadioButton;
    private RadioButton mPoliceRadioButton;
    private RadioButton mHospitalRadioButton;
    private RadioButton mCinemaRadioButton;
    private RadioButton mParkingRadioButton;
    private RadioButton mGasStationRadioButton;
    private RadioButton mPharmacyRadioButton;
    private RadioButton mSuperMarketRadioButton;
    private RadioButton mTransportRadioButton;
    private RadioButton mSchoolRadioButton;
    private TextView mTitletextView;
    private Spinner mSpinner;
    private static final String TAG = "NarrowSearchActivity";
    private Spinner mDistanceMeasureSpinner;
    private String mChosenSpinnerOption = "0";//means default distance
    private String mChosenMeasure = "Miles";
    private static String mQueryString =new String();
    private static final String MY_GEOCODING_API_KEY = "YOUR GEOCODING API KEY";
    private LatLng meLatLng;
    private LocationManager locationManager;
    private String provider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.narrow_search_layout);
        setTheme(R.style.MyMaterialTheme);

        mToolbar = (Toolbar) findViewById(R.id.search_toolbar);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.orange));
        mToolbar.setTitle(R.string.refine_search_string);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTitletextView = (TextView) findViewById(R.id.search_text_view);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        meLatLng=new LatLng(Singleton.getLocation().getLatitude(),Singleton.getLocation().getLongitude());
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        // Initialize the location fields
        if (location != null) {
            onLocationChanged(location);
        }
        else
        {
            Location targetLocation = new Location("");//provider name is unnecessary
            targetLocation.setLatitude(51.509865d);//your coords of course
            targetLocation.setLongitude(-0.118092d);
            onLocationChanged(targetLocation);
        }

        //Creates drop down list and adapter containing the data, plus link of spinner-adapter
        mSpinner = (Spinner) findViewById(R.id.spinner);
        mSpinner.setHorizontalScrollBarEnabled(true);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_text_view, getResources().getStringArray(R.array.spinners_values)) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setBackgroundColor(Color.parseColor("#FFFFFF"));
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setDrawingCacheBackgroundColor(getResources().getColor(R.color.white));
        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    mChosenSpinnerOption = "2";
                else
                    mChosenSpinnerOption = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mChosenSpinnerOption = "limitless";
            }
        });
        mDistanceMeasureSpinner = (Spinner) findViewById(R.id.measure_spinner);
        mDistanceMeasureSpinner.setHorizontalScrollBarEnabled(true);
        mDistanceMeasureSpinner.setHorizontalFadingEdgeEnabled(true);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_text_view, getResources().getStringArray(R.array.distance_spinner_values)) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setBackgroundColor(Color.parseColor("#FFFFFF"));
                return view;
            }
        };
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDistanceMeasureSpinner.setDrawingCacheBackgroundColor(getResources().getColor(R.color.white));
        mDistanceMeasureSpinner.setAdapter(adapter2);
        mDistanceMeasureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mChosenMeasure = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mChosenSpinnerOption = "Miles";
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////
        SpannableString spannableString = new SpannableString(mTitletextView.getText().toString());
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);

        mRadioGroup = findViewById(R.id.radio_group);
        mBankRadioButton = (RadioButton) findViewById(R.id.radio_bank);
        mDinningRadioButton = (RadioButton) findViewById(R.id.radio_dinning);
        mPostOfficeRadioButton = (RadioButton) findViewById(R.id.radio_post_office);
        mPoliceRadioButton = (RadioButton) findViewById(R.id.radio_police);
        mHospitalRadioButton = (RadioButton) findViewById(R.id.radio_hospital);
        mCinemaRadioButton = (RadioButton) findViewById(R.id.radio_cinema);
        mParkingRadioButton = (RadioButton) findViewById(R.id.radio_carpark);
        mGasStationRadioButton = (RadioButton) findViewById(R.id.radio_petrol);
        mPharmacyRadioButton = (RadioButton) findViewById(R.id.radio_chemist);
        mSuperMarketRadioButton = (RadioButton) findViewById(R.id.radio_supermarket);
        mTransportRadioButton = (RadioButton) findViewById(R.id.radio_public_transport);
        mSchoolRadioButton = (RadioButton) findViewById(R.id.radio_school);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // checkedId is the RadioButton selected
                View radioButton = mRadioGroup.findViewById(checkedId);

                switch (checkedId) {
                    case R.id.radio_bank:
                        mQueryString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + meLatLng.latitude + "," + meLatLng.longitude + "&radius=&type=bank,atm&key=" + MY_GEOCODING_API_KEY;
                        break;
                    case R.id.radio_dinning:
                        mQueryString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + meLatLng.latitude + "," + meLatLng.longitude + "&radius=&type=restaurant&key=" + MY_GEOCODING_API_KEY;
                        break;
                    case R.id.radio_post_office:
                        mQueryString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + meLatLng.latitude + "," + meLatLng.longitude + "&radius=&type=post_office&key=" + MY_GEOCODING_API_KEY;
                        break;
                    case R.id.radio_police:
                        mQueryString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + meLatLng.latitude + "," + meLatLng.longitude + "&radius=&type=police&key=" + MY_GEOCODING_API_KEY;
                        break;
                    case R.id.radio_hospital:
                        mQueryString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + meLatLng.latitude + "," + meLatLng.longitude + "&radius=&type=hospital&key=" + MY_GEOCODING_API_KEY;
                        break;
                    case R.id.radio_cinema:
                        mQueryString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + meLatLng.latitude + "," + meLatLng.longitude + "&radius=&type=movie_theater&key=" + MY_GEOCODING_API_KEY;
                        break;
                    case R.id.radio_carpark:
                        mQueryString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + meLatLng.latitude + "," + meLatLng.longitude + "&radius=&type=parking&key=" + MY_GEOCODING_API_KEY;
                        break;
                    case R.id.radio_petrol:
                        mQueryString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + meLatLng.latitude + "," + meLatLng.longitude + "&radius=&type=gas_station&key=" + MY_GEOCODING_API_KEY;
                        break;
                    case R.id.radio_chemist:
                        mQueryString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + meLatLng.latitude + "," + meLatLng.longitude + "&radius=&type=pharmacy&key=" + MY_GEOCODING_API_KEY;
                        break;
                    case R.id.radio_supermarket:
                        mQueryString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + meLatLng.latitude + "," + meLatLng.longitude + "&radius=&type=shopping_mall&key=" + MY_GEOCODING_API_KEY;
                        break;
                    case R.id.radio_public_transport:
                        mQueryString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + meLatLng.latitude + "," + meLatLng.longitude + "&radius=&type=subway_station,bus_station,train_station&key=" + MY_GEOCODING_API_KEY;
                        break;
                    case R.id.radio_school:
                        mQueryString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + meLatLng.latitude + "," + meLatLng.longitude + "&radius=&type=school&key=" + MY_GEOCODING_API_KEY;
                        break;
                }
            }
        });



        //Buttons
        mSearchButton = (Button) findViewById(R.id.search_button);
        mCancelButton = (Button) findViewById(R.id.cancel_search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRadioGroup.getCheckedRadioButtonId() != -1) {
                    Intent intent = new Intent(NarrowSearchActivity.this, UserScreenActivity.class);
                    String[] stringToReturn = mQueryString.split("&type");

                    if (mChosenMeasure.equals("Kilometers"))//if measure is in kmts then apply straight away, otherwise pass to miles
                    {
                        stringToReturn[0] += mChosenSpinnerOption;
                    } else {
                        double kmts = 1000 * (Double.parseDouble(mChosenSpinnerOption) / 0.62137);
                        stringToReturn[0] += kmts;
                    }
                    String finalStringToReturn = stringToReturn[0] + "&type" + stringToReturn[1] + "4";
                    Bundle data = new Bundle();
                    data.putString("prejson", finalStringToReturn);
                    intent.putExtras(data);
                    setResult(RESULT_CODE, intent);
                    finish();
                    //now ask new FetchPlacesTask().execute(stringToSend); when this string is captured in onactivityresult in userscrfeenactivity
                } else {
                    FancyToast.makeText(NarrowSearchActivity.this, "No search term was requested. Select a category please",
                            Toast.LENGTH_SHORT, FancyToast.CONFUSING, false).show();
                }

            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,Toast.LENGTH_SHORT).show();
    }
}
