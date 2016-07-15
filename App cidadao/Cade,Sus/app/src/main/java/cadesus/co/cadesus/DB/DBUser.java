package cadesus.co.cadesus.DB;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cadesus.co.cadesus.DB.Entidades.User;

/**
 * Created by fraps on 7/13/16.
 */
public class DBUser
{

    private static DBUser mDBUser = null;
    private final FirebaseDatabase mDB;
    private final DatabaseReference mRef;

    public LatLng lastKnowLocation = null;

    DBUser()
    {
        mDB = FirebaseDatabase.getInstance();
        mRef = mDB.getReference("usuarios");
    }

    public void saveUser()
    {
        if (!DBLogin.shared().getUserID().equals("")) {
            mRef.child(DBLogin.shared().getUserID()).setValue(User.shared());
        }
    }

    public static DBUser shared()
    {
        if (mDBUser == null) {
            mDBUser = new DBUser();
        }
        return mDBUser;
    }

}
