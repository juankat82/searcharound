package helperclasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

public class Singleton {

    private static String cityName;
    private static Location sLocation;
    private static final String TAG="Singleton";
    private static Boolean isDecimal=null;
    private static List<Weather> lst=new ArrayList<>();
    private static SharedPreferences sSharedPreferences;
    private static SharedPreferences.Editor editor;
    private static Singleton sSingleton;
    private static Context sContext;
    private static List<Place> sPlaceList=new ArrayList<>();
    private static GoogleMap sGoogleMap;
    private static boolean isMarkerOn;
    private static List<Marker> mMarkerList;
    private static LatLng sCenterDot;
    private static int way;//walk or drive..
    private static int sTimeTravel;
    private static List<LatLng> sListLatLng=new ArrayList<>();

    public static void setCenterDot(LatLng centerDot)
    {
        sCenterDot=centerDot;
    }
    public static LatLng getCenterDot()
    {
        return sCenterDot;
    }

    public static boolean isMarkerOn() {
        return isMarkerOn;
    }

    public static void setIsMarkerOn(boolean isMarkerOn) {
        Singleton.isMarkerOn = isMarkerOn;
    }

    public static Singleton get(Context context)
    {
        if (sSingleton==null)
            sSingleton=new Singleton(context);
        return sSingleton;
    }
    public Singleton(Context context)
    {
        sContext=context;
    }

    public static void setSharedPreferences(SharedPreferences sharedPreferences)
    {
        sSharedPreferences=sharedPreferences;
        editor=sSharedPreferences.edit();
    }
    public static SharedPreferences getSharedPreferences()
    {
        return sSharedPreferences;
    }
    public static boolean isDecimal() {

            isDecimal=sSharedPreferences.getBoolean("isDecimal",false);
            Log.i(TAG,"Is decimal value is: "+isDecimal);
            return isDecimal;
    }

    public static void setDecimal(boolean decimal) {

        Log.i(TAG,"IsDecimal: "+decimal);
        if (sSharedPreferences.contains("isDecimal"))
        {
            editor.remove("isDecimal");
            editor.apply();

        }
        editor.putBoolean("isDecimal",decimal);
        editor.apply();
    }

    public static void setMap(GoogleMap map)
    {
        sGoogleMap=map;
    }
    public static GoogleMap getGoogleMap()
    {
        return sGoogleMap;
    }
    public static String getCityName() {
        return cityName;
    }

    public static void setCityName(String city) {
        cityName = city;
    }


    public static void setLocation(Location location)
    {
        sLocation=location;
    }
    public static Location getLocation()
    {
        return sLocation;
    }
    public static void setPlaces(List<Place> placeList)
    {
        sPlaceList=new ArrayList<Place>(placeList);
    }
    public static List<Place> getPlaceList()
    {
        return sPlaceList;
    }
    public static void setList(List<Weather> list)
    {
        lst=new ArrayList<>();
        lst=list;
    }
    public static List<Weather> getList()
    {
        return lst;
    }

    public static void setContext(Context context) {
        sContext=context;
    }
    public static Context getContext()
    {
        return sContext;
    }


    public static void setMarkerList(List<Marker> markerList) {
        mMarkerList=markerList;
    }
    public static List<Marker> getMarkerList()
    {
        return mMarkerList;
    }

    public static void setWay(int bottomNavigationChoice) {
        way=bottomNavigationChoice;
    }

    public static void setTimeTravel(int timeTravel) {
        sTimeTravel = timeTravel;
    }

    public static int getTimeTravel() {
        return sTimeTravel;
    }

    public static void setStartAndEndPoints(List<LatLng> listLatLng)
    {
        sListLatLng=listLatLng;
    }
    public static List<LatLng> getStartAndEndPoints()
    {
        return sListLatLng;
    }
}
