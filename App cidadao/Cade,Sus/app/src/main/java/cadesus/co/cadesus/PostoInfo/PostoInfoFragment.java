package cadesus.co.cadesus.PostoInfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cadesus.co.cadesus.BuildConfig;
import cadesus.co.cadesus.DB.DBMain;
import cadesus.co.cadesus.DB.DBObserver;
import cadesus.co.cadesus.DB.Entidades.PostoDeSaude;
import cadesus.co.cadesus.DB.Entidades.User;
import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/14/16.
 */
public class PostoInfoFragment extends Fragment implements DBObserver {

    public static final String FRAGMENT_TAG =
            BuildConfig.APPLICATION_ID + ".FragmentTag";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mRecyclerLayout;
    private PostoDeSaude mPosto;
    private PostoInfoAdapter mAdapter;

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
        updateView();
        return v;
    }

    @Override
    public void dataUpdated() {
        updateView();
    }

    public void setPosto(String postoID)
    {
        mPosto = DBMain.shared().mPostosDeSaude.get(postoID);
    }

    public void updateView()
    {
        mAdapter = new PostoInfoAdapter(getContext(),mPosto,
                mPosto.remedios,DBMain.shared().getRemediosForPosto(mPosto.uid));
        mRecyclerView.setAdapter(mAdapter);
    }
}
