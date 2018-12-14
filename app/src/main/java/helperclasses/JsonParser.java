package helperclasses;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.widget.Toast;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shashank.sony.fancytoastlib.FancyToast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.net.URL;
import java.util.*;
import javax.net.ssl.HttpsURLConnection;

public class JsonParser extends AsyncTask<Void,Void,Void> {

    private static List<Place> sPlaceList=new ArrayList<>();
    private String result;
    private String option;
    private String queryString;
    private JSONObject baseObject;
    private JSONObject plusCodeJson;
    private JSONArray resultsJson;
    private static Activity mActivity;
    private List<Marker> mMarkerList=new ArrayList<>();

    public JsonParser (String result, String option, Activity thisActivity,String queryString,String searchTerm,Context context){
        this.queryString=queryString;
        this.result=result;
        this.option=option;
        mActivity=thisActivity;
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        try {
            parser();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Void parser() throws JSONException
    {
        baseObject=new JSONObject(result);

        if (baseObject==null)
        {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FancyToast.makeText(mActivity,"No result",Toast.LENGTH_SHORT,FancyToast.INFO,false).show();
                }
            });
        }
        else
        {
            switch(option)
            {
                case "1":
                    plusCodeJson=baseObject.getJSONObject("plus_code");
                    resultsJson=baseObject.getJSONArray("results");//captures "results" array
                    if (!sPlaceList.isEmpty())//starts list from scratch
                        sPlaceList.clear();
                    Place place=new Place();//creates object PLACE
                    String globalCode=(String)plusCodeJson.get("global_code");
                    place.setGlobalCode(globalCode);
                    JSONObject addressComponentObject=resultsJson.getJSONObject(0); //gets object "address_component" from "results" object
                    String formattedAddress=resultsJson.getJSONObject(1).get("formatted_address").toString(); //gets object "formatted_address" from "results" object
                    place.setFormatted_address(formattedAddress);
                    JSONObject geometryObject=resultsJson.getJSONObject(2); //gets object "geometry" from "results" object
                    JSONObject geometryArray=geometryObject.getJSONObject("geometry");//gets geometry array
                    JSONObject locationObject=geometryArray.getJSONObject("location");//gets object "location" from geometry array
                    String latitude=locationObject.get("lat").toString();
                    place.setLatitude(latitude);
                    String longitude=locationObject.get("lng").toString();
                    place.setLongitude(longitude);
                    String placeId=resultsJson.getJSONObject(3).getString("place_id"); //gets object "address_component" from "results" object
                    place.setPlaceId(placeId);
                    //parses array address_components
                    JSONArray addressComponentArray=addressComponentObject.getJSONArray("address_components");
                    JSONObject addressComponent=addressComponentArray.getJSONObject(0);

                     if (addressComponent.get("types").toString().contains("route"))
                         place.setName(addressComponent.get("short_name").toString());
                     else
                         place.setName(addressComponent.get("long_name").toString());

                     String postCode = addressComponentArray.getJSONObject(5).get("long_name").toString();
                     place.setPostalCode(postCode);
                    sPlaceList.add(place);
                    break;
                case "2":
                    resultsJson=baseObject.getJSONArray("results");//captures "results" array
                    if (!sPlaceList.isEmpty())//starts list from scratch
                        sPlaceList.clear();
                    sPlaceList=getMyList(resultsJson);
                    break;
                case "3":
                    if (!sPlaceList.isEmpty())//starts list from scratch
                        sPlaceList.clear();
                    JSONArray predictionArray=baseObject.getJSONArray("predictions");//this is the base array gotten from base object

                    //we will capture only the IDS of the places, then perform a new search to retrieve the new json updated with all the details
                    String[] idArray=new String[predictionArray.length()];
                    for (int i=0;i<predictionArray.length();i++)
                    {
                        JSONObject obj=(JSONObject)predictionArray.get(i);
                        String id=obj.getString("place_id");
                        idArray[i]=id;
                    }
                    String[] apiKey=queryString.split("&key=");
                    String key=apiKey[1].substring(0,apiKey[1].length()-1);
                    //perform search with
                    for (int i=0;i<idArray.length;i++)
                    {
                        String searchKey = "https://maps.googleapis.com/maps/api/place/details/json?placeid="+idArray[i]+"&key="+key;
                        String newPlaceJson=null;
                        try {
                            newPlaceJson=getUrlString(searchKey);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        sPlaceList.add(getMyPlace(newPlaceJson));
                    }
                    break;
                case "4":
                    if (!sPlaceList.isEmpty())//starts list from scratch
                        sPlaceList.clear();
                    resultsJson=baseObject.getJSONArray("results");//captures "results" array

                    sPlaceList=getNarrowResults(resultsJson);

                    break;
            }

            Singleton.setPlaces(sPlaceList);
            ///////////////////////////////////////////////////THIS OPERATES THE MAP/////////////////////UNCOMMENT!!!!!!!!!!!!
            final GoogleMap map;
            GoogleMap map2=Singleton.getGoogleMap();//changing this for a method that gets a LOCATION instead of a map, or a default if its null, its better
            while(map2==null){
                map2=Singleton.getGoogleMap();
            }
            map=map2;


            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    map.clear();
                    mMarkerList.clear();
                    //Your own marker
                    LatLng meLatLng;
                    if (Singleton.isMarkerOn()==false)
                    {
                        meLatLng=new LatLng(map.getMyLocation().getLatitude(),map.getMyLocation().getLongitude());

                    }
                    else
                    {
                        meLatLng=new LatLng(Singleton.getLocation().getLatitude(),Singleton.getLocation().getLongitude());

                    }

                    Marker markerMe=map.addMarker(new MarkerOptions().position(meLatLng).snippet("YOUR ARE HERE").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)).visible(true));
                    markerMe.setTag(new String("-1"));

                    Singleton.setPlaces(sPlaceList);
                    Singleton.setCenterDot(meLatLng); //establishes an origin point to trace a route on map to the selected point (if needed)

                    for (int i=0;i<sPlaceList.size();i++)
                    {
                        LatLng latLng=new LatLng(Double.valueOf(sPlaceList.get(i).getLatitude()),Double.valueOf(sPlaceList.get(i).getLongitude()));
                        String title=sPlaceList.get(i).getName();

                        Marker marker=map.addMarker(new MarkerOptions().position(latLng).title(title).snippet(sPlaceList.get(i).getPlaceId()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        marker.setTag(new String(sPlaceList.get(i).getName()));
                        mMarkerList.add(marker);

                        CameraPosition cameraPosition1=new CameraPosition.Builder().target(latLng).zoom(5).build();
                        CameraPosition cameraPosition2=new CameraPosition.Builder().target(latLng).zoom(12).build();
                        CameraPosition cameraPosition3=new CameraPosition.Builder().target(latLng).zoom(13).build();
                        if (option.equals("1"))
                            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2),null);
                        else if (option.equals("2"))
                            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2),null);
                        else if (option.equals("4"))
                            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition3),null);
                        else
                        {
                            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition1),null);
                        }
                    }
                    Singleton.setMarkerList(mMarkerList);
                    map.setInfoWindowAdapter(new CustomInfoWindowAdapter(LayoutInflater.from(Singleton.getContext())));
                }
            });
            //////////////////////////////////////////////////
        }
       return null;
    }
    private List<Place> getNarrowResults(JSONArray resultsJson) throws JSONException
    {
        List<Place> list=new ArrayList<>();

        for (int i=0;i<resultsJson.length();i++)
        {
            Place place=new Place();
            JSONObject basicObject=resultsJson.getJSONObject(i);

            //get target location
            JSONObject locationObject=basicObject.getJSONObject("geometry").getJSONObject("location");
            String latitude=locationObject.get("lat").toString();
            String longitude=locationObject.get("lng").toString();
            place.setLatitude(latitude);
            place.setLongitude(longitude);

            //set name
            String name=basicObject.getString("name");
            place.setName(name);

            //set open now
            if (basicObject.has("opening_hours")) {
                String openNow = basicObject.getJSONObject("opening_hours").getString("open_now");
                place.setOpenNow(openNow);
            }

            //Id setup
            if (basicObject.has("place_id"))
            {
                String placeId = basicObject.getString("place_id");
                place.setId(placeId);
            }

            //set global code for maps quick search
            if (basicObject.has("plus_code")) {
                String globalCode = basicObject.getJSONObject("plus_code").getString("global_code");
                place.setGlobalCode(globalCode);
            }

            //Id setup
            if (basicObject.has("price_level")) {
                String priceLevel = basicObject.getString("price_level");
                place.setPricelevel(priceLevel);

            }

            //Id setup
            if (basicObject.has("rating")) {
                String rating = basicObject.getString("rating");
                place.setRating(rating);
            }

            //set vicinity
            if (basicObject.has("vicinity")) {
                String address = basicObject.getString("vicinity");
                place.setFormatted_address(address);
            }

            list.add(place);
        }

        return list;
    }
    private Place getMyPlace(String newPlaceJson) throws JSONException
    {
        Place place=new Place();
        baseObject=new JSONObject(newPlaceJson);
        //get result object
        JSONObject resultObject=baseObject.getJSONObject("result");
        //get address array
        JSONArray addressComponentArray=resultObject.getJSONArray("address_components");
        String formattedAddress="";
        String postalCode="";
        for (int i=0;i<addressComponentArray.length();i++)
        {
            JSONObject addressObject=addressComponentArray.getJSONObject(i);
            if (addressObject.getJSONArray("types").get(0).equals("postal_code"))
            {
                postalCode=addressObject.getString("long_name");
                place.setPostalCode(postalCode);
            }
            else
            {
                formattedAddress += addressObject.getString("long_name")+" ";
            }
        }
        //set formatted address
        place.setFormatted_address(formattedAddress);
        //Set phone number
        if (resultObject.has("formatted_phone_number"))
        {
            String phoneNumber = resultObject.getString("formatted_phone_number");
            place.setPhoneNumber(phoneNumber);
        }
        //geometry object (lat & long)
        JSONObject locationObject=resultObject.getJSONObject("geometry").getJSONObject("location");
        String latitude=locationObject.getString("lat");
        String longitude=locationObject.getString("lng");
        place.setLatitude(latitude);
        place.setLongitude(longitude);
        //set Name
        String name=resultObject.getString("name");
        place.setName(name);
        //set open now
        if (resultObject.has("opening_hours")) {
            String openNow = resultObject.getJSONObject("opening_hours").getString("open_now");
            place.setOpenNow(openNow);
        }
        //set Opening time
        if (resultObject.has("opening_hours")) {
            JSONArray weekdayJsonArray = resultObject.getJSONObject("opening_hours").getJSONArray("weekday_text");
            String[] weekDayArray = new String[weekdayJsonArray.length()];
            for (int i = 0; i < weekdayJsonArray.length(); i++) {
                weekDayArray[i] = weekdayJsonArray.get(i).toString();
            }
            place.setOpeningdays(weekDayArray);
        }
        //set place id
        String placeId=resultObject.getString("place_id");
        place.setPlaceId(placeId);
        //set global code for maps quick search
        if (resultObject.has("global_code")) {
            String globalCode = resultObject.getJSONObject("plus_code").getString("global_code");
            place.setGlobalCode(globalCode);
        }
        //set price level
        if (resultObject.has("price_level"))
        {
            String priceLevel=resultObject.getString("price_level");
            place.setPricelevel(priceLevel);
        }
        //set rating
        if (resultObject.has("rating"))
        {
            String rating=resultObject.getString("rating");
            place.setRating(rating);
        }
        //establish the url required to find place in google maps straight away
        if (resultObject.has("url"))
        {
            String googleSearchUrl=resultObject.getString("url");
            place.setGoogleSearchUrl(googleSearchUrl);
        }
        //establish the own place website
        if (resultObject.has("website"))
        {
            String ownWebsite=resultObject.getString("website");
            place.setGoogleSearchUrl(ownWebsite);
        }
        return place;
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
    private List<Place> getMyList(JSONArray resultsJson) throws JSONException {
        List<Place> myList=new ArrayList<>();

        for (int i=0;i<resultsJson.length();i++)
        {
            Place place2=new Place();//creates object PLACE
            JSONObject obj=(JSONObject)resultsJson.get(i);
            //Address
            String formattedAddress2=obj.getString("formatted_address");
            place2.setFormatted_address(formattedAddress2);
            //Lat & Long
            JSONObject geometryObject2=obj.getJSONObject("geometry").getJSONObject("location");
            String latitude2=geometryObject2.getString("lat");
            String longitude2=geometryObject2.getString("lng");
            place2.setLatitude(latitude2);
            place2.setLongitude(longitude2);
            //Name
            String name2=obj.getString("name");
            place2.setName(name2);
            //OpenNow
            if (obj.has("opening_hours")) {
                String openNow2 = obj.getJSONObject("opening_hours").getString("open_now");
                place2.setOpenNow(openNow2);
            }
            //Place id
            String id2=obj.getString("place_id");
            place2.setPlaceId(id2);
            //Global code to search on google maps
            if (obj.has("global_code"))
            {
                String globalCode2 = obj.getJSONObject("plus_code").getString("global_code");
                place2.setGlobalCode(globalCode2);
            }
            //Rating
            if (obj.has("opening_hours"))
            {
                String rating2 = obj.getString("rating");
                place2.setRating(rating2);
            }
            myList.add(place2);
        }
        return myList;
    }
    public static List<Place> getList()
    {
        return sPlaceList;
    }
}