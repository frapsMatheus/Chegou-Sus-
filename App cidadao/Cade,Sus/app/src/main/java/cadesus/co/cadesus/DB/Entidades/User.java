package cadesus.co.cadesus.DB.Entidades;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fraps on 7/13/16.
 */

@IgnoreExtraProperties
public class User
{

    public double latitutde;
    public double longitude;

    public Map<String,Long> remedios = new HashMap<>();
    public List<String> postos_saude = new ArrayList<>();

    private static User mUser = null;

    public static User shared()
    {
        if (mUser == null) {
            mUser = new User();
        }
        return mUser;
    }

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public void adicionarRemedio(String id, Long caixas)
    {
        remedios.put(id,caixas);
    }

    public void removerRemedio(String id)
    {
        remedios.remove(id);
    }

    public void adicionarPosto(String id)
    {
        postos_saude.add(id);
    }

    public void removerPosto(String id)
    {
        postos_saude.remove(id);
    }

    public void setLocation(double lat, double lon)
    {
        latitutde = lat;
        longitude = lon;
    }
}
