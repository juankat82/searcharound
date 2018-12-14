package helperclasses;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.main.searcharound.R;
import com.squareup.picasso.Picasso;

    public class ForecastFragment extends Fragment {

        private static Weather sWeather;
        private ImageView mImageView;

        //forecast
        private TextView mTextViewAddressForecast;
        private TextView mDateForecast;
        private TextView mTemperatureForecast;
        private TextView mConditionTextForecast;
        private TextView mWindForecast;
        private TextView mBarometricsForecast;
        private TextView mVisiForecast;
        private TextView mLightPeriods;

        private static final String TAG="ForecastFragment";

        public static Fragment newFragment(Weather weather)
        {
            sWeather=weather;
            return new ForecastFragment();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(false);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.forecast_weather_fragment_layout, container, false);
            mImageView = view.findViewById(R.id.image_view_forecast);
            Picasso.get().load(sWeather.getIconUrl()).into(mImageView);
            mTextViewAddressForecast = view.findViewById(R.id.textView_address_forecast);
            SpannableString spannableString3 = new SpannableString(sWeather.getName());
            spannableString3.setSpan(new UnderlineSpan(), 0, spannableString3.length(), 0);
            mTextViewAddressForecast.setText(spannableString3);
            mDateForecast = view.findViewById(R.id.forecast_dates);
            mDateForecast.setText("Time: " + sWeather.getDate());
            mTemperatureForecast = view.findViewById(R.id.textView_temperature_forecast);

            if (Singleton.isDecimal()) {
                mTemperatureForecast.setText("Min Temp: " + sWeather.getMinTempC() + "C°\nMax Temp: " + sWeather.getMaxTempC() + "C°\nAvg. Temp: " + sWeather.getAvgTempC() + "C°");
            } else {
                mTemperatureForecast.setText("Min Temp: " + sWeather.getMinTempF() + "F°\nMax Temp: " + sWeather.getMaxTempF() + "F°\nAvg. Temp: " + sWeather.getAvgTempF() + "F°");
            }

            mConditionTextForecast = view.findViewById(R.id.textView_text_forecast);
            SpannableString spannableStringTextForecast = new SpannableString("Weather is: " + sWeather.getConditionText());
            spannableStringTextForecast.setSpan(new UnderlineSpan(), 0, spannableStringTextForecast.length(), 0);
            mConditionTextForecast.setText(spannableStringTextForecast);

            mWindForecast = view.findViewById(R.id.textView_wind_forecast);
            if (Singleton.isDecimal()) {
                mWindForecast.setText("Max Wind Speed: " + sWeather.getMaxWindKph() + " km/h");
            } else {
                mWindForecast.setText("Max Wind Speed: " + sWeather.getMaxWindMph() + " Miles/h");
            }

            mBarometricsForecast = view.findViewById(R.id.textView_barometrics_forecast);
            if (Singleton.isDecimal()) {
                mBarometricsForecast.setText("Total Precip: " + sWeather.getTotalPrecipMm() + " mm" + "\nHumidity: " + sWeather.getAvgHumidity() + "%\nUV: " + sWeather.getUv());
            } else {
                mBarometricsForecast.setText("Total Precip. : " + sWeather.getGetTotalPrecipIn() + " in" + "\nHumidity: " + sWeather.getAvgHumidity() + "%\nUV: " + sWeather.getUv());
            }

            mVisiForecast = view.findViewById(R.id.textView_visibility_forecast);
            if (Singleton.isDecimal()) {
                mVisiForecast.setText("Visibility: " + sWeather.getAvgVisKm() + " km");
            } else {
                mVisiForecast.setText("Visibility: " + sWeather.getAvgVisMiles() + " miles");
            }

            mLightPeriods = view.findViewById(R.id.textView_sunperiods_forecast);
            mLightPeriods.setText("Sunrise: " + sWeather.getSunriseTime() + "\nSunset: " + sWeather.getSunsetTime() + "\nMoonrise: " + sWeather.getMoonRiseTime() + "\nMoonset: " + sWeather.getMoonSetTime());

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
