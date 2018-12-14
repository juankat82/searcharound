package helperclasses;

public class Weather {


    //Current Fields (JUST ONE WILL BE ALLOWED WHEN QUERYING FORECAST, THEN ALL THE OTHERS WIL LBE FORECAST-LIKE
    private String id="";
    private String name="";
    private String region="";
    private String country="";
    private String lat="";
    private String lon="";
    private String localtime="";
    private String lastUpdated="";
    private String tempC="";
    private String tempF="";
    private String isDay="";
    private String text="";
    private String icon="";
    private String weatherText="";
    private String windMph="";
    private String windKph="";
    private String windDegree="";
    private String windDir="";
    private String pressureMb="";
    private String pressureIn="";
    private String precipMm="";
    private String precipIn="";
    private String humidity="";
    private String cloud="";
    private String feelslikeC="";
    private String feelslikeF="";
    private String visKm="";
    private String visMiles="";

    //Forecast fields
    private boolean isForecast=false;
    private String date="";
    private String maxTempF="";
    private String maxTempC="";
    private String minTempF="";
    private String minTempC="";
    private String avgTempC="";
    private String avgTempF="";
    private String maxWindMph="";
    private String maxWindKph="";
    private String totalPrecipMm="";
    private String totalPrecipIn="";
    private String avgVisKm="";//Average visibility in kilometer
    private String avgVisMiles="";//Average visibility in miles
    private String avgHumidity="";
    private String conditionText="";
    private String iconUrl="";
    private String code="";
    private String uv="";
    private String sunriseTime="";
    private String sunsetTime="";
    private String moonRiseTime="";
    private String moonSetTime="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotalPrecipIn() {
        return totalPrecipIn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLocaltime() {
        return localtime;
    }

    public void setLocaltime(String localtime) {
        this.localtime = localtime;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getTempC() {
        return tempC;
    }

    public void setTempC(String tempC) {
        this.tempC = tempC;
    }

    public String getTempF() {
        return tempF;
    }

    public void setTempF(String tempF) {
        this.tempF = tempF;
    }

    public String getIsDay() {
        return isDay;
    }

    public void setIsDay(String isDay) {
        this.isDay = isDay;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWeatherText() {
        return weatherText;
    }

    public void setWeatherText(String weatherText) {
        this.weatherText = weatherText;
    }

    public String getWindMph() {
        return windMph;
    }

    public void setWindMph(String windMph) {
        this.windMph = windMph;
    }

    public String getWindKph() {
        return windKph;
    }

    public void setWindKph(String windKph) {
        this.windKph = windKph;
    }

    public String getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(String windWindDegree) {
        this.windDegree = windWindDegree;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public String getPressureMb() {
        return pressureMb;
    }

    public void setPressureMb(String pressureMb) {
        this.pressureMb = pressureMb;
    }

    public String getPressureIn() {
        return pressureIn;
    }

    public void setPressureIn(String pressureIn) {
        this.pressureIn = pressureIn;
    }

    public String getPrecipMm() {
        return precipMm;
    }

    public void setPrecipMm(String precipMm) {
        this.precipMm = precipMm;
    }

    public String getPrecipIn() {
        return precipIn;
    }

    public void setPrecipIn(String precipIn) {
        this.precipIn = precipIn;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getCloud() {
        return cloud;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

    public String getFeelslikeC() {
        return feelslikeC;
    }

    public void setFeelslikeC(String feelslikeC) {
        this.feelslikeC = feelslikeC;
    }

    public String getFeelslikeF() {
        return feelslikeF;
    }

    public void setFeelslikeF(String feelslikeF) {
        this.feelslikeF = feelslikeF;
    }

    public String getVisKm() {
        return visKm;
    }

    public void setVisKm(String visKm) {
        this.visKm = visKm;
    }

    public String getVisMiles() {
        return visMiles;
    }

    public void setVisMiles(String visMiles) {
        this.visMiles = visMiles;
    }

    public boolean isForecast() {
        return isForecast;
    }

    public void setForecast(boolean forecast) {
        isForecast = forecast;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMaxTempF() {
        return maxTempF;
    }

    public void setMaxTempF(String maxTempF) {
        this.maxTempF = maxTempF;
    }

    public String getMaxTempC() {
        return maxTempC;
    }

    public void setMaxTempC(String maxTempC) {
        this.maxTempC = maxTempC;
    }

    public String getMinTempF() {
        return minTempF;
    }

    public void setMinTempF(String minTempF) {
        this.minTempF = minTempF;
    }

    public String getMinTempC() {
        return minTempC;
    }

    public void setMinTempC(String minTempC) {
        this.minTempC = minTempC;
    }

    public String getAvgTempC() {
        return avgTempC;
    }

    public void setAvgTempC(String avgTempC) {
        this.avgTempC = avgTempC;
    }

    public String getAvgTempF() {
        return avgTempF;
    }

    public void setAvgTempF(String avgTempF) {
        this.avgTempF = avgTempF;
    }

    public String getMaxWindMph() {
        return maxWindMph;
    }

    public void setMaxWindMph(String maxWindMph) {
        this.maxWindMph = maxWindMph;
    }

    public String getMaxWindKph() {
        return maxWindKph;
    }

    public void setMaxWindKph(String maxWindKph) {
        this.maxWindKph = maxWindKph;
    }

    public String getTotalPrecipMm() {
        return totalPrecipMm;
    }

    public void setTotalPrecipMm(String totalPrecipMm) {
        this.totalPrecipMm = totalPrecipMm;
    }

    public String getGetTotalPrecipIn() {
        return totalPrecipIn;
    }

    public void setTotalPrecipIn(String totalPrecipIn) {
        this.totalPrecipIn = totalPrecipIn;
    }

    public String getAvgVisKm() {
        return avgVisKm;
    }

    public void setAvgVisKm(String avgVisKm) {
        this.avgVisKm = avgVisKm;
    }

    public String getAvgVisMiles() {
        return avgVisMiles;
    }

    public void setAvgVisMiles(String avgVisMiles) {
        this.avgVisMiles = avgVisMiles;
    }

    public String getAvgHumidity() {
        return avgHumidity;
    }

    public void setAvgHumidity(String avgHumidity) {
        this.avgHumidity = avgHumidity;
    }

    public String getConditionText() {
        return conditionText;
    }

    public void setConditionText(String conditionText) {
        this.conditionText = conditionText;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = "https:"+iconUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUv() {
        return uv;
    }

    public void setUv(String uv) {
        this.uv = uv;
    }

    public String getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(String sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public String getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(String sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    public String getMoonRiseTime() {
        return moonRiseTime;
    }

    public void setMoonRiseTime(String moonRiseTime) {
        this.moonRiseTime = moonRiseTime;
    }

    public String getMoonSetTime() {
        return moonSetTime;
    }

    public void setMoonSetTime(String moonSetTime) {
        this.moonSetTime = moonSetTime;
    }


    public String toString()
    {
        String str=null;

        if (isForecast())
        {
            str="\nIsForecast: "+isForecast+"\ndate: "+date+"\nminTempC: "+minTempC+"\nuv: "+uv+"\n--------------------------";
        }
        else
        {
            str="\nIsForecast: "+isForecast+"\nName: "+name+"\nregion: "+region+"\nCountry: "+country+"\nfeelsLikeC: "+feelslikeC+"\nisDay: "+isDay+"\n--------------------------";
        }
        return str;
    }



}
