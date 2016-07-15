package cadesus.co.cadesus.MeusPostos;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import cadesus.co.cadesus.BuildConfig;
import cadesus.co.cadesus.DB.DBMain;
import cadesus.co.cadesus.DB.DBObserver;
import cadesus.co.cadesus.DB.DBUser;
import cadesus.co.cadesus.DB.Entidades.PostoDeSaude;
import cadesus.co.cadesus.DB.Entidades.User;
import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/14/16.
 */
public class MeusPostosFragment extends Fragment implements DBObserver {

    public static final String FRAGMENT_TAG =
            BuildConfig.APPLICATION_ID + ".FragmentTag";

    RecyclerView mRecyclerView;
    LinkedHashMap<String, PostoDeSaude> mPostosInitialList = new LinkedHashMap<>();
    LinkedHashMap<String, Boolean> mPostoSelecionado = new LinkedHashMap<>();

    private RecyclerView.LayoutManager mRecyclerLayout;
    private ArrayList<PostoDeSaude> mPostoSearch;

    private MeusPostosAdapter mAdapter;
    private FloatingActionButton mDone;
    boolean mFirstTime = true;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        setHasOptionsMenu(true);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.fragment_recycler);
        mPostosInitialList = DBMain.shared().mPostosDeSaude;
        mRecyclerLayout = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mRecyclerLayout);
        generateChecked();
        searchList("");
        DBMain.shared().subscribeToObserver(this);
        mDone = (FloatingActionButton)v.findViewById(R.id.fab);
        mDone.setImageResource(R.drawable.check_mark);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.shared().postos_saude.clear();
                for (Map.Entry<String,Boolean> pair : mPostoSelecionado.entrySet())
                {
                    if (pair.getValue()) {
                        User.shared().adicionarPosto(pair.getKey());
                    }
                }
                DBUser.shared().saveUser();
                getActivity().finish();
            }
        });
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager manager =
                (SearchManager) getActivity().getApplication().getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = (MenuItem) menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(
                manager.getSearchableInfo(getActivity().getComponentName()));
        if (searchView != null) {
            searchView.setVisibility(View.VISIBLE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchList(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchList(newText);
                    return false;
                }
            });
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    searchList("");
                    return false;
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MeusPostosActivity)getActivity()).getSupportActionBar().show();
    }

    private void searchList(String text)
    {
//        TODO: Add section
        ArrayList<PostoDeSaude> postosAtuais =
                new ArrayList<>(DBMain.shared().getAllPostosWithLocation(
                        DBUser.shared().lastKnowLocation).keySet());
        mPostoSearch = new ArrayList<PostoDeSaude>();
        for (PostoDeSaude posto : postosAtuais) {
            if (posto.nome.toLowerCase().contains(text.toLowerCase()) ||
                    posto.endereco.toLowerCase().contains(text.toLowerCase())) {
                mPostoSearch.add(posto);
            }
        }
        mAdapter = new MeusPostosAdapter(mPostoSearch,mPostoSelecionado);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DBMain.shared().removeObserver(this);
    }

    public void generateChecked()
    {
        if (mPostoSelecionado.isEmpty()) {

            for (PostoDeSaude posto : mPostosInitialList.values()) {
                mPostoSelecionado.put(posto.uid,false);
            }
            if (User.shared().postos_saude.isEmpty()) {
                LinkedHashMap<PostoDeSaude,Double> postoDeSaudePertoDeMim = DBMain.shared()
                        .getPostosCloseToLocation(new LatLng(User.shared().latitude, User.shared().longitude));
                for (PostoDeSaude posto : postoDeSaudePertoDeMim.keySet()) {
                    mPostoSelecionado.put(posto.uid,true);
                }
            } else {
                for (String postoUID : User.shared().postos_saude) {
                    mPostoSelecionado.put(postoUID,true);
                }
            }
        }
    }


    @Override
    public void dataRemedioUpdated() {

    }

    @Override
    public void userUpdated() {
        if (mFirstTime) {
            mPostosInitialList = DBMain.shared().mPostosDeSaude;
            generateChecked();
            searchList("");
            mFirstTime = false;
        }
    }

    @Override
    public void postosUpdated() {
        mPostosInitialList = DBMain.shared().mPostosDeSaude;
        generateChecked();
        searchList("");
    }
}
