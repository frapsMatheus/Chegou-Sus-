package cadesus.co.cadesus.Main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import cadesus.co.cadesus.DB.DBLogin;
import cadesus.co.cadesus.DB.DBMain;
import cadesus.co.cadesus.DB.DBObserver;
import cadesus.co.cadesus.DB.Entidades.PostoDeSaude;
import cadesus.co.cadesus.Login.LoginActivity;
import cadesus.co.cadesus.PostoInfo.PostoInfoActivity;
import cadesus.co.cadesus.R;

public class MapsFragment extends Fragment implements OnMapReadyCallback, DBObserver {

    private GoogleMap gMap;
    private MapView mapView;

    private Button mLoginButton;
    private Map<Marker, String> mMarkers = new HashMap<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MapsInitializer.initialize(getActivity());
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
        initializeMap();
        DBMain.shared().subscribeToObserver(this);
        DBMain.shared().getPostos();
    }

    private void initializeMap() {
        if (gMap == null) {
            mapView = (MapView) getActivity().findViewById(R.id.map);
            mapView.getMapAsync(this);
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DBMain.shared().removeObserver(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);
        mLoginButton = (Button) view.findViewById(R.id.map_login_button);
        mapView = (MapView) view.findViewById(R.id.map);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setupLogin();
    }

    public void setupLogin()
    {
        if (mLoginButton != null) {
            if (DBLogin.shared().isLoggedUser()) {
                mLoginButton.setVisibility(View.GONE);
            } else {
                mLoginButton.setVisibility(View.VISIBLE);
                mLoginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        initializeMap();
        setupLogin();
        addPins();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(-15.7941,
                        -47.8825));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(9);

        gMap.moveCamera(center);
        gMap.animateCamera(zoom);

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        gMap.setMyLocationEnabled(true);
        gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String postoId = mMarkers.get(marker);
                Intent intent = new Intent(getActivity(), PostoInfoActivity.class);
                intent.putExtra("postoID",postoId);
                startActivity(intent);
            }
        });
    }

    public void addPins()
    {
        if (gMap != null) {
            gMap.clear();
            mMarkers.clear();
            for (PostoDeSaude posto : DBMain.shared().mPostosDeSaude.values()) {
                Marker marker = gMap.addMarker(new MarkerOptions()
                        .position(new LatLng(posto.location.get(0), posto.location.get(1)))
                        .title(posto.nome).snippet(posto.endereco).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                mMarkers.put(marker,posto.uid);
            }
        }
    }

    @Override
    public void dataRemedioUpdated() {

    }

    @Override
    public void userUpdated() {

    }

    @Override
    public void postosUpdated() {
        addPins();
    }
}
