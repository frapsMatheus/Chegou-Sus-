package cadesus.co.cadesus.Main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.GameEntity;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import cadesus.co.cadesus.DB.DBLogin;
import cadesus.co.cadesus.DB.DBMain;
import cadesus.co.cadesus.DB.DBUser;
import cadesus.co.cadesus.MainApplication;
import cadesus.co.cadesus.R;
import cadesus.co.cadesus.Settings.SettingsActivity;

/**
 * Created by fraps on 7/11/16.
 */
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {
    ViewPager mViewPager;
    MainPagerAdapter mAdapter;
    Toolbar mToolbar;
    TabLayout mTabLayout;
    Button mLogOut;
    private GoogleApiClient mClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        mAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tabBar);
        mTabLayout.setupWithViewPager(mViewPager);

        mLogOut = (Button) findViewById(R.id.toolbar_logout);
        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(myIntent);
            }
        });

        mClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API).addConnectionCallbacks(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mClient.connect();
        prepareViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareViews();
    }

    private void prepareViews() {
        if (mAdapter.mFragments.size() > 0) {
            ((MapsFragment) mAdapter.mFragments.get(0)).setupLogin();
        }
        if (DBLogin.shared().isLoggedUser()) {
            mTabLayout.setVisibility(View.VISIBLE);
            mLogOut.setVisibility(View.VISIBLE);
            DBMain.shared().getUser();
        } else {
            mTabLayout.setVisibility(View.GONE);
            mLogOut.setVisibility(View.GONE);
            mViewPager.setCurrentItem(0);
        }
    }

    @Override
    protected void onStop() {
        mClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mClient);
        if (lastLocation != null) {
            DBUser.shared().lastKnowLocation = new LatLng(lastLocation.getLatitude()
                    ,lastLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
