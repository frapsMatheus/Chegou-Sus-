package cadesus.co.cadesus.DB.Entidades;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

/**
 * Created by fraps on 7/13/16.
 */
public class User
{

    public double latitutde;
    public double longitude;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public void setLocation(double lat, double lon)
    {
        latitutde = lat;
        longitude = lon;
    }
}
