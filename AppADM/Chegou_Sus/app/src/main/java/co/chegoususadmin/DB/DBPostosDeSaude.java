package co.chegoususadmin.DB;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.chegoususadmin.DB.Entidades.PostoDeSaude;

/**
 * Created by fraps on 7/13/16.
 */
public class DBPostosDeSaude
{

    private static DBPostosDeSaude mDBPostosDeSaude = null;
    private final FirebaseDatabase mDB;
    private final DatabaseReference mRef;

    public LatLng lastKnowLocation = null;

    DBPostosDeSaude()
    {
        mDB = FirebaseDatabase.getInstance();
        mRef = mDB.getReference("postos_saude");
    }

    public void saveUser()
    {
        mRef.child(DBLogin.shared().getUserID()).setValue(PostoDeSaude.shared());
    }

    public static DBPostosDeSaude shared()
    {
        if (mDBPostosDeSaude == null) {
            mDBPostosDeSaude = new DBPostosDeSaude();
        }
        return mDBPostosDeSaude;
    }

}
