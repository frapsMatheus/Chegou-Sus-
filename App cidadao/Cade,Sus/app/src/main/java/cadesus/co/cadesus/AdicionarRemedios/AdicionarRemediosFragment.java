package cadesus.co.cadesus.AdicionarRemedios;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import cadesus.co.cadesus.BuildConfig;
import cadesus.co.cadesus.DB.DBMain;
import cadesus.co.cadesus.DB.DBObserver;
import cadesus.co.cadesus.DB.DBUser;
import cadesus.co.cadesus.DB.Entidades.Remedio;
import cadesus.co.cadesus.DB.Entidades.User;
import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/13/16.
 */
public class AdicionarRemediosFragment extends Fragment implements AdicionarRemedioCallback,
        DBObserver {

    public static final String FRAGMENT_TAG =
            BuildConfig.APPLICATION_ID + ".FragmentTag";

    LinkedHashMap<String,Remedio> mRemediosInitialList = new LinkedHashMap<>();
    ArrayList<Remedio> mRemedioSearch = new ArrayList<>();
    AdicionarRemediosAdapter mAdapter;
    RecyclerView mRecyclerView;

    private RecyclerView.LayoutManager mRecyclerLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_new_medicine, container, false);
        setHasOptionsMenu(true);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.medicine_recyclerView);
        mRemediosInitialList = DBMain.shared().mRemedios;
        mRecyclerLayout = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mRecyclerLayout);
        searchList("");
        DBMain.shared().subscribeToObserver(this);
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
        ((AdicionarRemediosActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DBMain.shared().removeObserver(this);
    }

    private void searchList(String text)
    {
//        TODO: Add section
        ArrayList<Remedio> remediosAtuais = new ArrayList<Remedio>(mRemediosInitialList.values());
        mRemedioSearch = new ArrayList<Remedio>();
        for (Remedio remedio : remediosAtuais) {
            if (remedio.patologia.toLowerCase().contains(text.toLowerCase()) ||
                    remedio.principio_ativo.toLowerCase().contains(text.toLowerCase())) {
                mRemedioSearch.add(remedio);
            }
        }
        mAdapter = new AdicionarRemediosAdapter(mRemedioSearch,getActivity(),this);
        mRecyclerView.setAdapter(mAdapter);
//        mLevelsMap = new LinkedHashMap<Integer,Integer>();
//        mPositionsLevelsMap = new LinkedHashMap<Integer,Integer>();
//        mMapsDictionary = new LinkedHashMap<Integer, Map>();
//        int currentLevel = -1;
//        int currentPosition = 0;
//        for (Map map : mMapsArray) {
//            if(isValidMap(map,text)) {
//                if (currentLevel != map.mLevel) {
//                    mLevelsMap.put(currentPosition, map.mLevel);
//                    mPositionsLevelsMap.put(map.mLevel, currentPosition);
//                    currentPosition++;
//                    currentLevel = map.mLevel;
//                }
//                mMapsDictionary.put(currentPosition, map);
//                currentPosition++;
//            }
//        }
//        mMapsAdapter = new MapsRecyclerAdapter(mImageDialog,mMapsDictionary,mLevelsMap,
//                mPositionsLevelsMap,getActivity());
//        mMapsRecycler.setAdapter(mMapsAdapter);
    }

    @Override
    public void remedioAdicionado(Remedio remedio, int quantidade) {
        User.shared().adicionarRemedio(remedio.uid,Long.valueOf(quantidade));
        DBUser.shared().saveUser();
        getActivity().finish();
    }

    @Override
    public void dataUpdated() {
        searchList("");
    }
}
