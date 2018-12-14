package helperclasses;

public class Place {

    private String mLatitude="";
    private String mLongitude="";
    private String mName="";
    private String mFormatted_address="";
    private String mId="";
    private String mOpenNow="";
    private String mRating="";
    private String mPlaceId="";
    private String mGlobalCode="";
    private String mPostalCode="";
    private String[] mOpeningdays=null;
    private String pricelevel="";
    private String googleSearchUrl="";
    private String ownSearchUrl="";


    public void setGoogleSearchUrl(String googleSearchUrl) {
        this.googleSearchUrl = googleSearchUrl;
    }

    public String getOwnSearchUrl() {
        return ownSearchUrl;
    }

    public void setOwnSearchUrl(String ownSearchUrl) {
        this.ownSearchUrl = ownSearchUrl;
    }

    public String getPricelevel() {
        return pricelevel;
    }

    public void setPricelevel(String pricelevel) {
        this.pricelevel = pricelevel;
    }

    public String[] getOpeningdays() {
        return mOpeningdays;
    }

    public void setOpeningdays(String[] openingdays) {
        mOpeningdays=openingdays;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    private String mPhoneNumber="";

    public String getPostalCode()
    {
        return mPostalCode;
    }
    public void setPostalCode(String postalCode)
    {
        mPostalCode=postalCode;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getFormatted_address() {
        return mFormatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        mFormatted_address = formatted_address;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getOpenNow() {
        return mOpenNow;
    }

    public void setOpenNow(String openNow) {
        mOpenNow = openNow;
    }

    public String getRating() {
        return mRating;
    }

    public void setRating(String rating) {
        mRating = rating;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(String placeId) {
        mPlaceId = placeId;
    }

    public String getGlobalCode() {
        return mGlobalCode;
    }

    public void setGlobalCode(String globalCode) {
        mGlobalCode = globalCode;
    }

    @Override
    public String toString()
    {
        String str="Name: "+mName+"\nFormatted Address: "+mFormatted_address+"\nGlobal Code: "+mGlobalCode+"\nPlace Id: "+mPlaceId+
                "\nLatitude: "+mLatitude+"\nLongitude: "+mLongitude+"\nPostal Code: "+mPostalCode+"\nOpen Now: "+mOpenNow+"\nRating: "+mRating;

        return str;

    }
}
