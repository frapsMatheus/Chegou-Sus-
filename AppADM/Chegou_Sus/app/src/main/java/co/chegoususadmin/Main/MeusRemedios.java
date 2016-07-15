package co.chegoususadmin.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import co.chegoususadmin.AdicionarRemedios.AdicionarRemediosActivity;
import co.chegoususadmin.BuildConfig;
import co.chegoususadmin.DB.DBLogin;
import co.chegoususadmin.DB.DBMain;
import co.chegoususadmin.DB.DBObserver;
import co.chegoususadmin.DB.Entidades.PostoDeSaude;
import co.chegoususadmin.R;

/**
 * Created by fraps on 7/11/16.
 */
public class MeusRemedios extends Fragment implements DBObserver {

    public static final String FRAGMENT_TAG =
            BuildConfig.APPLICATION_ID + ".FragmentTag";

    int PLACE_PICKER_REQUEST = 1;

    RecyclerView mRecyclerView;
    MeusRemediosAdapter mAdapter;
    private FloatingActionButton mActionButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview,container,false);
        mActionButton = (FloatingActionButton)view.findViewById(R.id.fab);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AdicionarRemediosActivity.class);
                startActivity(intent);
            }
        });
        mActionButton.setVisibility(View.GONE);

        if (DBLogin.shared().isLoggedUser()) {
            DBMain.shared().getPosto();
            DBMain.shared().subscribeToObserver(this);
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_recycler);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layout);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter = new MeusRemediosAdapter(DBMain.shared().
                getRemediosForPosto(),
                PostoDeSaude.shared().remedios,getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DBMain.shared().removeObserver(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void dataRemedioUpdated() {
        if (PostoDeSaude.shared().uid != null) {
            mActionButton.setVisibility(View.VISIBLE);
            mAdapter = new MeusRemediosAdapter(DBMain.shared().getRemediosForPosto(),
                    PostoDeSaude.shared().remedios,getActivity());
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void userUpdated() {

    }

    @Override
    public void postosUpdated() {


        DBMain.shared().getRemedios();
    }
}
