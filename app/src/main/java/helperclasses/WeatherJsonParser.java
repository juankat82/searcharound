package helperclasses;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class WeatherJsonParser extends AsyncTask <String,Void,List<Weather>> {


    private List<Weather> mWeatherList;
    private int mOption;
    private String jsonString;
    private static final String TAG="WeatherJsonParser";


    public WeatherJsonParser(int option)//option says if itll be today or forecast
    {
        mOption=option;
    }
    @Override
    protected List<Weather> doInBackground(String... strings){
        jsonString=strings[0];

        try {
            parser(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mWeatherList;
    }
    private void parser(String jsonString) throws JSONException
    {
        switch(mOption)
        {
            case 1:

                mWeatherList=new ArrayList<>();
                Weather weather=new Weather();
                JSONObject jsonLocationObject=new JSONObject(jsonString);
                JSONObject locationObject=jsonLocationObject.getJSONObject("location");
                JSONObject currentObject=jsonLocationObject.getJSONObject("current");
                JSONObject currentConditionObject=currentObject.getJSONObject("condition");

                String name=locationObject.get("name").toString();
                String region=locationObject.get("region").toString();
                String country=locationObject.get("country").toString();
                String lat=locationObject.get("lat").toString();
                String lon=locationObject.get("lon").toString();
                String localtime=locationObject.get("localtime").toString();
                String lastUpdated=currentObject.get("last_updated").toString();
                String tempC=currentObject.get("temp_c").toString();
                String tempF=currentObject.get("temp_f").toString();
                String isDay=currentObject.get("is_day").toString();//says if its day or nighttime
                String text=currentConditionObject.getString("text");
                String icon="https:"+currentConditionObject.getString("icon");
                String weatherCodeForText=currentConditionObject.getString("code");
                String weatherText=selectWeatherTextFromCode(weatherCodeForText,isDay);
                String windMph=currentObject.get("wind_mph").toString();
                String windKph=currentObject.get("wind_kph").toString();
                String windWindDegree=currentObject.get("wind_degree").toString();
                String windDir=currentObject.get("wind_dir").toString();
                String pressureMb=currentObject.get("pressure_mb").toString();
                String pressureIn=currentObject.get("pressure_in").toString();
                String precipMm=currentObject.get("precip_mm").toString();
                String precipIn=currentObject.get("precip_in").toString();
                String humidity=currentObject.get("humidity").toString();
                String cloud=currentObject.get("cloud").toString();
                String feelslikeC=currentObject.get("feelslike_c").toString();
                String feelslikeF=currentObject.get("feelslike_f").toString();
                String visKm=currentObject.get("vis_km").toString();
                String visMiles=currentObject.get("vis_miles").toString();
                //set weather fields
                weather.setForecast(false);
                weather.setName(name);
                weather.setRegion(region);
                weather.setCountry(country);
                weather.setLat(lat);
                weather.setLon(lon);
                weather.setLocaltime(localtime);
                weather.setLastUpdated(lastUpdated);
                weather.setTempC(tempC);
                weather.setTempF(tempF);
                weather.setIsDay(isDay);
                weather.setIcon(icon);

                weather.setWeatherText(weatherText);
                weather.setWindKph(windKph);
                weather.setWindMph(windMph);
                weather.setWindDegree(windWindDegree);
                weather.setWindDir(windDir);
                weather.setText(text);
                weather.setPressureMb(pressureMb);
                weather.setPressureIn(pressureIn);
                weather.setPrecipMm(precipMm);
                weather.setPrecipIn(precipIn);
                weather.setHumidity(humidity);
                weather.setCloud(cloud);
                weather.setFeelslikeC(feelslikeC);
                weather.setFeelslikeF(feelslikeF);
                weather.setVisKm(visKm);
                weather.setVisMiles(visMiles);
                mWeatherList.add(weather);
                break;
            case 2:
                JSONArray jsonArray=new JSONArray(jsonString);
                String nameQueried=jsonArray.getJSONObject(0).get("name").toString();
                String[] nameArray=nameQueried.split(", ");
                Singleton.setCityName(nameArray[0]);
                mWeatherList=new ArrayList<>();
                break;
            case 3:
                //////////now get the forecast data////////////////
                mWeatherList=new ArrayList<>();


                JSONObject jsonLocationObject3=new JSONObject(jsonString);
                JSONObject locationObject3=jsonLocationObject3.getJSONObject("location");
                String name4=locationObject3.get("name").toString();

                JSONObject currentObject3=jsonLocationObject3.getJSONObject("current");
                JSONObject jsonLocationObject4=new JSONObject(jsonString);
                JSONObject locationObject4=jsonLocationObject4.getJSONObject("forecast");


                JSONObject jsonObject=locationObject4.getJSONArray("forecastday").getJSONObject(0);
                Weather weather1=new Weather();
                weather1.setName(name4);

                String date=jsonObject.get("date").toString();
                JSONObject dayObject=jsonObject.getJSONObject("day");
                String maxTempC=dayObject.get("maxtemp_c").toString();
                String maxTempF=dayObject.get("maxtemp_f").toString();
                String minTempC=dayObject.get("mintemp_c").toString();
                String minTempF=dayObject.get("mintemp_f").toString();
                String avgTempC=dayObject.get("avgtemp_c").toString();
                String avgTempF=dayObject.get("avgtemp_f").toString();
                String maxWindMph=dayObject.get("maxwind_mph").toString();
                String maxWindKph=dayObject.get("maxwind_kph").toString();
                String totalPrecipMM=dayObject.get("totalprecip_mm").toString();
                String totalPrecipIn=dayObject.get("totalprecip_in").toString();
                String avgVisKm=dayObject.get("avgvis_km").toString();
                String avgVisMiles=dayObject.get("avgvis_miles").toString();
                String avgHumidity=dayObject.get("avghumidity").toString();
                //sub-object condition
                JSONObject conditionObject3=dayObject.getJSONObject("condition");
                String text3=conditionObject3.get("text").toString();
                String icon3=conditionObject3.get("icon").toString();
                String code=conditionObject3.get("code").toString();

                String uv=dayObject.get("uv").toString();

                //astro object
                JSONObject astroObject=jsonObject.getJSONObject("astro");
                String sunrise=astroObject.get("sunrise").toString();
                String sunset=astroObject.get("sunset").toString();
                String moonrise=astroObject.get("moonrise").toString();
                String moonset=astroObject.get("moonset").toString();

                weather1.setForecast(true);
                weather1.setDate(date);
                weather1.setMaxTempC(maxTempC);
                weather1.setMaxTempF(maxTempF);
                weather1.setMinTempC(minTempC);
                weather1.setMinTempF(minTempF);
                weather1.setAvgTempC(avgTempC);
                weather1.setAvgTempF(avgTempF);
                weather1.setMaxWindMph(maxWindMph);
                weather1.setMaxWindKph(maxWindKph);
                weather1.setTotalPrecipMm(totalPrecipMM);
                weather1.setTotalPrecipIn(totalPrecipIn);
                weather1.setAvgVisKm(avgVisKm);
                weather1.setAvgVisMiles(avgVisMiles);
                weather1.setAvgHumidity(avgHumidity);
                weather1.setConditionText(text3);
                weather1.setIconUrl(icon3);
                weather1.setCode(code);
                weather1.setSunriseTime(sunrise);
                weather1.setSunsetTime(sunset);
                weather1.setMoonRiseTime(moonrise);
                weather1.setMoonSetTime(moonset);
                mWeatherList.add(weather1);
                break;
        }
    }

    private String selectWeatherTextFromCode(String codeNumber,String isDay)
    {
        //isDay YES=1, NO=0
        String returnCode="";
        int code=Integer.parseInt(codeNumber);
        switch(code)
        {
            case 1000:
                if (isDay=="1")
                    returnCode="Sunny";
                else
                    returnCode="Clear";
                break;
            case 1003:
                if (isDay=="1")
                    returnCode="Partly cloudy";
                else
                    returnCode="Partly cloudy";
                break;
            case 1006:
                if (isDay=="1")
                    returnCode="Cloudy";
                else
                    returnCode="Cloudy";
                break;
            case 1009:
                if (isDay=="1")
                    returnCode="Overcast";
                else
                    returnCode="Overcast";
                break;
            case 1030:
                if (isDay=="1")
                    returnCode="Mist";
                else
                    returnCode="Mist";
                break;
            case 1063:
                if (isDay=="1")
                    returnCode="Patchy rain possible";
                else
                    returnCode="Patchy rain possible";
                break;
            case 1066:
                if (isDay=="1")
                    returnCode="Patchy snow possible";
                else
                    returnCode="Patchy snow possible";
                break;
            case 1069:
                if (isDay=="1")
                    returnCode="Patchy sleet possible";
                else
                    returnCode="Patchy sleet possible";
                break;
            case 1072:
                if (isDay=="1")
                    returnCode="Patchy freezing drizzle possible";
                else
                    returnCode="Patchy freezing drizzle possible";
                break;
            case 1087:
                if (isDay=="1")
                    returnCode="Thundery outbreaks possible";
                else
                    returnCode="Thundery outbreaks possible";
                break;
            case 1114:
                if (isDay=="1")
                    returnCode="Blowing snow";
                else
                    returnCode="Blowing snow";
                break;
            case 1117:
                if (isDay=="1")
                    returnCode="Blizzard";
                else
                    returnCode="Blizzard";
                break;
            case 1135:
                if (isDay=="1")
                    returnCode="Fog";
                else
                    returnCode="Fog";
                break;
            case 1147:
                if (isDay=="1")
                    returnCode="Freezing fog";
                else
                    returnCode="Freezing fog";
                break;
            case 1150:
                if (isDay=="1")
                    returnCode="Patchy light drizzle";
                else
                    returnCode="Patchy light drizzle";
                break;
            case 1153:
                if (isDay=="1")
                    returnCode="Light drizzle";
                else
                    returnCode="Light drizzle";
                break;
            case 1168:
                if (isDay=="1")
                    returnCode="Freezing drizzle";
                else
                    returnCode="Freezing drizzle";
                break;
            case 1171:
                if (isDay=="1")
                    returnCode="Heavy freezing drizzle";
                else
                    returnCode="Heavy freezing drizzle";
                break;
            case 1180:
                if (isDay=="1")
                    returnCode="Patchy light rain";
                else
                    returnCode="Patchy light rain";
                break;
            case 1183:
                if (isDay=="1")
                    returnCode="Light rain";
                else
                    returnCode="Light rain";
                break;
            case 1186:
                if (isDay=="1")
                    returnCode="Moderate rain at times";
                else
                    returnCode="Moderate rain at times";
                break;
            case 1189:
                if (isDay=="1")
                    returnCode="Moderate rain";
                else
                    returnCode="Moderate rain";
                break;
            case 1192:
                if (isDay=="1")
                    returnCode="Heavy rain at times";
                else
                    returnCode="Heavy rain at times";
                break;
            case 1195:
                if (isDay=="1")
                    returnCode="Heavy rain";
                else
                    returnCode="Heavy rain";
                break;
            case 1198:
                if (isDay=="1")
                    returnCode="Light freezing rain";
                else
                    returnCode="Light freezing rain";
                break;
            case 1201:
                if (isDay=="1")
                    returnCode="Moderate or heavy freezing rain";
                else
                    returnCode="Moderate or heavy freezing rain";
                break;
            case 1204:
                if (isDay=="1")
                    returnCode="Light sleet";
                else
                    returnCode="Light sleet";
                break;
            case 1207:
                if (isDay=="1")
                    returnCode="Moderate or heavy sleet";
                else
                    returnCode="Moderate or heavy sleet";
                break;
            case 1210:
                if (isDay=="1")
                    returnCode="Patchy light snow";
                else
                    returnCode="Patchy light snow";
                break;
            case 1213:
                if (isDay=="1")
                    returnCode="Light snow";
                else
                    returnCode="Light snow";
                break;
            case 1216:
                if (isDay=="1")
                    returnCode="Patchy moderate snow";
                else
                    returnCode="Patchy moderate snow";
                break;
            case 1219:
                if (isDay=="1")
                    returnCode="Moderate snow";
                else
                    returnCode="Moderate snow";
                break;
            case 1222:
                returnCode="Patchy heavy snow";
                break;
            case 1225:
                returnCode="Heavy snow";
                break;
            case 1237:
                returnCode="Ice pellets";
                break;
            case 1240:
                returnCode="Light rain shower";
                break;
            case 1243:
                returnCode="Moderate or heavy rain shower";
                break;
            case 1246:
                returnCode="Torrential rain shower";
                break;
            case 1249:
                returnCode="Light sleet showers";
                break;
            case 1252:
                returnCode="Moderate or heavy sleet showers";
                break;
            case 1255:
                returnCode="Light snow showers";
                break;
            case 1258:
                returnCode="Moderate or heavy snow showers";
                break;
            case 1261:
                returnCode="Light showers of ice pellets";
                break;
            case 1264:
                returnCode="Moderate or heavy showers of ice pellets";
                break;
            case 1273:
                returnCode="Patchy light rain with thunder";
                break;
            case 1276:
                returnCode="Moderate or heavy rain with thunder";
                break;
            case 1279:
                returnCode="Patchy light snow with thunder";
                break;
            case 1282:
                returnCode="Moderate or heavy snow with thunder";
                break;
        }

        return returnCode;
    }
}
