package helperclasses;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.main.searcharound.R;
import com.squareup.picasso.Picasso;

public class CurrentFragment extends Fragment {

    private static Weather sWeather;
    private TextView textViewAddress;
    private TextView textViewLatlon;
    private TextView textViewDates;
    private TextView textViewTemperature;
    private TextView textViewText;
    private TextView textViewWind_data;
    private TextView textViewBarometrics;
    private TextView textViewVisibility;
    private ImageView mImageView;//shared


    private static final String TAG="CurrentFragment";

    public static Fragment newFragment(Weather weather)
    {
        sWeather=weather;
        return new CurrentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.current_weather_fragment_layout,container,false);
        Log.i(TAG,"isDecimal: "+Singleton.isDecimal());
        mImageView=view.findViewById(R.id.weather_imageView);
        Picasso.get().load(sWeather.getIcon()).into(mImageView);
        textViewAddress=view.findViewById(R.id.textView_address);
        SpannableString spannableString=new SpannableString(sWeather.getName());
        spannableString.setSpan(new UnderlineSpan(),0,spannableString.length(),0);
        textViewAddress.setText(spannableString);
        textViewLatlon=view.findViewById(R.id.textView_latlon);
        textViewLatlon.setText("Lat: "+sWeather.getLat()+"\nLon: "+sWeather.getLon());
        textViewDates=view.findViewById(R.id.textView_dates);
        textViewDates.setText("Time: "+sWeather.getLocaltime()+"\nUpdated: "+sWeather.getLastUpdated());
        textViewTemperature=view.findViewById(R.id.textView_temperature);
        if (Singleton.isDecimal())
            textViewTemperature.setText("Temp: "+sWeather.getTempC()+"C°\nFeels like: "+sWeather.getFeelslikeC()+"C°");
        else
            textViewTemperature.setText("Temp: "+sWeather.getTempF()+"F°\nFeels like: "+sWeather.getFeelslikeF()+"F°");

        textViewText=view.findViewById(R.id.textView_text);
        SpannableString spannableStringText=new SpannableString("Weather is: "+sWeather.getText());
        spannableStringText.setSpan(new UnderlineSpan(),0,spannableStringText.length(),0);
        textViewText.setText(spannableStringText);
        textViewWind_data=view.findViewById(R.id.textView_wind_data);
        if (Singleton.isDecimal())
        {
            textViewWind_data.setText("Wind speed: "+sWeather.getWindKph()+" kmh\nWind degree: "+sWeather.getWindDegree()+"°\nWind Direction: "+sWeather.getWindDir());
        }
        else
        {
            textViewWind_data.setText("Wind speed: "+sWeather.getWindMph()+" mph\nWind degree: "+sWeather.getWindDegree()+"\nDirection: "+sWeather.getWindDir());
        }

        textViewBarometrics=view.findViewById(R.id.textView_barometrics);

        if (Singleton.isDecimal())
        {
            textViewBarometrics.setText("Pressure: "+sWeather.getPressureMb()+" mB\nPrecip: "+sWeather.getPrecipMm()+" mm\nHumidity: "+sWeather.getHumidity()+"%");
        }
        else
        {
            textViewBarometrics.setText("Pressure: "+sWeather.getPressureIn()+" in\nPrecip: "+sWeather.getPrecipIn()+" in\nHumidity: "+sWeather.getHumidity()+"%");
        }

        textViewVisibility=view.findViewById(R.id.textView_visibility);
        if (Singleton.isDecimal())
        {
            textViewVisibility.setText("Visibility: "+sWeather.getVisKm()+" Km");
        }
        else
        {
            textViewVisibility.setText("Visibility: "+sWeather.getVisMiles()+" Miles");
        }
        return view;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        try {
            finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
