package cadesus.co.cadesus.Main;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import cadesus.co.cadesus.DB;
import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/11/16.
 */
public class MainActivity extends AppCompatActivity
{
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

        mToolbar = (Toolbar)findViewById(R.id.tool_bar);
        int textColor;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textColor = getResources().getColor(R.color.colorText,null);
        } else {
            textColor = getResources().getColor(R.color.colorText);
        }
        mToolbar.setTitleTextColor(textColor);
        setSupportActionBar(mToolbar);

        mAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tabBar);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabTextColors(ColorStateList.valueOf(textColor));

        mLogOut = (Button)findViewById(R.id.toolbar_logout);
        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB.shared().logOutUser();
                prepareViews();
            }
        });

        mClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
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

    private void prepareViews()
    {
        if (mAdapter.mFragments.size() > 0) {
            ((MapsFragment) mAdapter.mFragments.get(0)).setupLogin();
        }
        if (DB.shared().isLoggedUser()) {
            mTabLayout.setVisibility(View.VISIBLE);
            mLogOut.setVisibility(View.VISIBLE);
        } else {
            mTabLayout.setVisibility(View.GONE);
            mLogOut.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        mClient.disconnect();
        super.onStop();
    }
}
