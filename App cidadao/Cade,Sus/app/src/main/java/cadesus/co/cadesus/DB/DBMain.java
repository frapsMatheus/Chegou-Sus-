package cadesus.co.cadesus.DB;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Observer;
import java.util.TreeMap;

import cadesus.co.cadesus.DB.Entidades.PostoDeSaude;
import cadesus.co.cadesus.DB.Entidades.Remedio;
import cadesus.co.cadesus.DB.Entidades.User;
import cadesus.co.cadesus.MeusPostos.PostosPertoUpdate;

import android.location.Location;
import android.util.Log;
/**
 * Created by fraps on 7/13/16.
 */
public class DBMain {

    private static DBMain mDBMain;
    private final FirebaseDatabase mDB;

    public LinkedHashMap<String, Remedio>      mRemedios = new LinkedHashMap();
    public LinkedHashMap<String, PostoDeSaude> mPostosDeSaude = new LinkedHashMap();

    public ArrayList<DBObserver> observers = new ArrayList<>();

    private double mLocationRadius = 100000;

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
                    User.shared().latitude = user.latitude;
                    User.shared().longitude = user.longitude;
                    User.shared().remedios = new LinkedHashMap<String, Long>(user.remedios);
                    User.shared().push_token = FirebaseInstanceId.getInstance().getToken();
                    DBUser.shared().saveUser();
                }
                notifyObservers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getPostos() {
        DatabaseReference dbRef = mDB.getReference("postos_saude");
        Query queryPostos = dbRef.orderByKey();
        queryPostos.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot childSnap : dataSnapshot.getChildren()) {
                        PostoDeSaude postoDeSaude = childSnap.getValue(PostoDeSaude.class);
                        postoDeSaude.uid = childSnap.getKey();
                        mPostosDeSaude.put(postoDeSaude.uid, postoDeSaude);
                    }
                    notifyObservers();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("","");
                }
            });
    }

    public ArrayList<Remedio> getRemediosForUser()
    {
        ArrayList<Remedio> remedios = new ArrayList<>();

        for (String id : User.shared().remedios.keySet()) {
            remedios.add(mRemedios.get(id));
        }
        return remedios;
    }

    public ArrayList<Remedio> getRemediosForPosto(String postoId)
    {
        ArrayList<Remedio> remedios = new ArrayList<>();
        PostoDeSaude postoDeSaude = mPostosDeSaude.get(postoId);
        for (String id : postoDeSaude.remedios.keySet()) {
            remedios.add(mRemedios.get(id));
        }
        return remedios;
    }

    public ArrayList<PostoDeSaude> getPostosForUser() {
        ArrayList<PostoDeSaude> postosDeSaude = new ArrayList<>();

        for (String id : User.shared().postos_saude) {
            postosDeSaude.add(mPostosDeSaude.get(id));
        }
        return postosDeSaude;
    }


    public LinkedHashMap<String, PostoDeSaude> getPostosComRemedio(String remedioId)
    {
        final LinkedHashMap<String, PostoDeSaude> result = new LinkedHashMap<>();
        for (PostoDeSaude posto : mPostosDeSaude.values()) {
            if (posto.remedios.get(remedioId) != null) {
                result.put(posto.uid,posto);
            }
        }
        return result;
    }


    public LinkedHashMap<PostoDeSaude,Double> getPostosCloseToLocation(LatLng position, LinkedHashMap<String, PostoDeSaude> postos)
    {
        final LinkedHashMap<PostoDeSaude,Double> postosNaLocalizacao = new LinkedHashMap<>();
        Location myLocation = new Location("");
        myLocation.setLatitude(position.latitude);
        myLocation.setLongitude(position.longitude);
        for (PostoDeSaude posto : postos.values()) {
            Location currentLocation = new Location("");
            currentLocation.setLatitude(posto.location.get(0));
            currentLocation.setLongitude(posto.location.get(1));
            double distance = myLocation.distanceTo(currentLocation);
            if (distance<mLocationRadius) {
                postosNaLocalizacao.put(posto,distance);
            }
        }
//        TODO: ORDENAR ISSO AQUI
        return postosNaLocalizacao;
    }

    public LinkedHashMap<PostoDeSaude,Double> getPostosPreferidosComDistancia(LatLng position , LinkedHashMap<String, PostoDeSaude> postos)
    {
        final LinkedHashMap<PostoDeSaude,Double> postosNaLocalizacao = new LinkedHashMap<>();
        Location myLocation = new Location("");
        myLocation.setLatitude(position.latitude);
        myLocation.setLongitude(position.longitude);
        for (String postoID : User.shared().postos_saude) {
            PostoDeSaude posto = postos.get(postoID);
            if (posto != null) {
                Location currentLocation = new Location("");
                currentLocation.setLatitude(posto.location.get(0));
                currentLocation.setLongitude(posto.location.get(1));
                double distance = myLocation.distanceTo(currentLocation);
                postosNaLocalizacao.put(posto, distance);
            }

        }
//        TODO: ORDENAR ISSO AQUI
        return postosNaLocalizacao;
    }

    public LinkedHashMap<PostoDeSaude,Double> getPostosCloseToLocation(LatLng position)
    {
        return getPostosCloseToLocation(position,mPostosDeSaude);
    }

}
