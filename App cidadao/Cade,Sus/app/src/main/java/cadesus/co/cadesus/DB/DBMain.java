package cadesus.co.cadesus.DB;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Observer;

import cadesus.co.cadesus.DB.Entidades.PostoDeSaude;
import cadesus.co.cadesus.DB.Entidades.Remedio;
import cadesus.co.cadesus.DB.Entidades.User;

import android.util.Log;
/**
 * Created by fraps on 7/13/16.
 */
public class DBMain {

    private static DBMain mDBMain;
    private final FirebaseDatabase mDB;

    public LinkedHashMap<String,Remedio> mRemedios = new LinkedHashMap();
    public ArrayList<PostoDeSaude> mPostosDeSaude = new ArrayList<>();

    public ArrayList<DBObserver> observers = new ArrayList<>();

    DBMain()
    {
        mDB = FirebaseDatabase.getInstance();
    }

    public static DBMain shared()
    {
        if (mDBMain == null) {
            mDBMain = new DBMain();
        }
        return mDBMain;
    }

    public void setPostos_de_saude(ArrayList<PostoDeSaude> postos_de_saude)
    {
        mPostosDeSaude = postos_de_saude;
    }

    public void getRemedios()
    {
        DatabaseReference dbRef = mDB.getReference("remedios");
        Query queryRemedios = dbRef.orderByKey();
        queryRemedios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnap : dataSnapshot.getChildren()) {
                   Remedio remedio = childSnap.getValue(Remedio.class);
                   remedio.uid = childSnap.getKey();
                   mRemedios.put(remedio.uid,remedio);
                }
                notifyObservers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("","");
            }
        });
    }

    public void notifyObservers()
    {
        for (DBObserver observer : observers) {
            observer.dataUpdated();
        }
    }

    public void subscribeToObserver(DBObserver observer)
    {
        observers.add(observer);
    }

    public void removeObserver(DBObserver observer)
    {
        observers.remove(observer);
    };

    public void getUser()
    {
        DatabaseReference dbRef = mDB.getReference("usuarios");
        Query query = dbRef.child(DBLogin.shared().getUserID());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    User user = dataSnapshot.getValue(User.class);
                    User.shared().postos_saude = new ArrayList<String>(user.postos_saude);
                    User.shared().latitutde = user.latitutde;
                    User.shared().longitude = user.longitude;
                    User.shared().remedios = new LinkedHashMap<String, Long>(user.remedios);
                }
                notifyObservers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public ArrayList<Remedio> getRemediosForUser()
    {
        ArrayList<Remedio> remedios = new ArrayList<>();
        LinkedHashMap<String,Long> quantidades = new LinkedHashMap<>();
        for (String id : User.shared().remedios.keySet()) {
            remedios.add(mRemedios.get(id));
        }
        return remedios;
    }

}
