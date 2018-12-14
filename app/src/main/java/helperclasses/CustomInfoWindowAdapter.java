package helperclasses;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.main.searcharound.R;
import java.util.List;


public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private LayoutInflater inflater;
    private List<Place> mPlaceList=Singleton.getPlaceList();
    private View v;

    public CustomInfoWindowAdapter(LayoutInflater inflater)
    {
        this.inflater=inflater;
    }

    @Override
    public View getInfoContents(final Marker m)
    {
        //loades custom layout
        List<Marker> markerList=Singleton.getMarkerList();
        v=inflater.inflate(R.layout.marker_layout,null);
        int chosenIndex=-1;
        String text="";
        String link="CLICK TO ROUTE";

        for (int i=0;i<markerList.size();i++)
        {
            if (markerList.get(i).getTag().equals(m.getTag()))
                chosenIndex=i;
        }
        Place place=mPlaceList.get(chosenIndex);
        text="Name: "+place.getName()+"\n";
        text+="Location: "+place.getLatitude()+", "+place.getLongitude()+"\n";
        text+="Address: "+place.getFormatted_address()+" "+place.getPostalCode()+"\n";

        if (!place.getPhoneNumber().equals(""))
            text+="Phone: "+place.getPhoneNumber()+"\n";

        if (!place.getOpenNow().equals(""))
            text+="Is open now: "+place.getOpenNow()+"\n";

        if (place.getOpeningdays()!=null)
        {
            text+="Opening hours: \n";
            for(String st: place.getOpeningdays())
                text+="\t"+st+"\n";
        }

        if (!place.getRating().equals(""))
             text+="Rating: "+place.getRating()+"\n";

        if (!place.getPricelevel().equals(""))
             text+="Price level: "+place.getPricelevel()+"\n";

        String id;
        if (!place.getPlaceId().equals(""))
        {
            text+="Place id: "+place.getPlaceId()+"\n";
            id=place.getPlaceId();
        }
        else
        {
            text+="Place id: "+place.getId()+"\n";
            id=place.getId();
        }

        if (!place.getOwnSearchUrl().equals(""))
             text+="Website: "+place.getOwnSearchUrl()+"\n";

        if (!place.getGlobalCode().equals(""))
            text+="Global Code: "+place.getGlobalCode()+"\n";


        final String ids=id;
        TextView titleView=(TextView)v.findViewById(R.id.marker_text_view);
        titleView.setText(text);
        TextView linkView=(TextView)v.findViewById(R.id.link_text);
        Singleton.getGoogleMap().setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                             public void onInfoWindowClick(Marker marker) {
                                                 //your code
                                             }
                                         });
        linkView.setText(link);
        return v;
    }
    @Override
    public View getInfoWindow(Marker m) {
        if (m.isInfoWindowShown())
        {
            String code = m.getSnippet();
        }
        return null;
    }
}
