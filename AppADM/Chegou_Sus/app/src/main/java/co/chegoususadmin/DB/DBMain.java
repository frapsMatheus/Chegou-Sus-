package co.chegoususadmin.DB;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import co.chegoususadmin.DB.Entidades.PostoDeSaude;
import co.chegoususadmin.DB.Entidades.Remedio;

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
                notifyObserversRemedios();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("","");
            }
        });
    }

    public void notifyObserversUser()
    {
        for (DBObserver observer : observers) {
            observer.userUpdated();
        }
    }

    public void notifyObserversRemedios()
    {
        for (DBObserver observer : observers) {
            observer.dataRemedioUpdated();
        }
    }

    public void notifyObserversPostos()
    {
        for (DBObserver observer : observers) {
            observer.postosUpdated();
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

    public void getPosto()
    {
        DatabaseReference dbRef = mDB.getReference("postos_saude");
        Query query = dbRef.child(DBLogin.shared().getUserID());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    PostoDeSaude posto = dataSnapshot.getValue(PostoDeSaude.class);
                    PostoDeSaude.shared().remedios = new HashMap<String, Long>(posto.remedios);
                    PostoDeSaude.shared().location = posto.location;
                    PostoDeSaude.shared().nome = posto.nome;
                    PostoDeSaude.shared().cep = posto.cep;
                    PostoDeSaude.shared().endereco = posto.endereco;
                    PostoDeSaude.shared().telefone = posto.telefone;
                    PostoDeSaude.shared().uid = dataSnapshot.getKey();
                }
                notifyObserversPostos();
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
                    notifyObserversPostos();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("","");
                }
            });
    }

    public ArrayList<Remedio> getRemediosForPosto()
    {
        ArrayList<Remedio> remedios = new ArrayList<>();
        PostoDeSaude postoDeSaude = PostoDeSaude.shared();
        for (String id : postoDeSaude.remedios.keySet()) {
            remedios.add(mRemedios.get(id));
        }
        return remedios;
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

    public LinkedHashMap<PostoDeSaude,Double> getPostosCloseToLocation(LatLng position)
    {
        return getPostosCloseToLocation(position,mPostosDeSaude);
    }

}
