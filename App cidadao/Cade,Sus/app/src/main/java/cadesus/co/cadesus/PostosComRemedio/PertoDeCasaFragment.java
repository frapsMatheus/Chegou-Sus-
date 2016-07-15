package cadesus.co.cadesus.PostosComRemedio;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import cadesus.co.cadesus.DB.DBMain;
import cadesus.co.cadesus.DB.DBObserver;
import cadesus.co.cadesus.DB.DBUser;
import cadesus.co.cadesus.DB.Entidades.PostoDeSaude;
import cadesus.co.cadesus.DB.Entidades.Remedio;
import cadesus.co.cadesus.DB.Entidades.User;
import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/11/16.
 */
public class PertoDeCasaFragment extends Fragment implements DBObserver
{

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mRecyclerLayout;
    private PostosComRemedioRecyclerAdapter mAdapter;
    private Remedio mRemedio;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_just_recyclerview, container, false);
        setHasOptionsMenu(true);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.layout_recyclerView);
        mRecyclerLayout = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mRecyclerLayout);
        DBMain.shared().subscribeToObserver(this);
        mRemedio = DBMain.shared().mRemedios.get(((PostosComRemedioActivity)getActivity()).remedioID);
        updateView();
        return v;
    }

    private void updateView() {
        LatLng casa = new LatLng(User.shared().latitude,User.shared().longitude);
        LinkedHashMap<String, PostoDeSaude> postos = DBMain.shared().getPostosComRemedio(mRemedio.uid);
        LinkedHashMap<PostoDeSaude,Double> postosComDistancia =
                DBMain.shared().getPostosCloseToLocation(casa,postos);
        LinkedHashMap<String,Double> distancias = new LinkedHashMap<>();
        for (LinkedHashMap.Entry<PostoDeSaude,Double> pair : postosComDistancia.entrySet()) {
            distancias.put(pair.getKey().uid,pair.getValue());
        }
        mAdapter = new PostosComRemedioRecyclerAdapter(new ArrayList<>(postosComDistancia.keySet()),
                distancias,getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void dataUpdated() {
        updateView();
    }
}
