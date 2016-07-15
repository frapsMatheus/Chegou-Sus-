package cadesus.co.cadesus.PostosComRemedio;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import cadesus.co.cadesus.DB.DBMain;
import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/11/16.
 */
public class PostosComRemedioActivity extends AppCompatActivity
{

    ViewPager mViewPager;
    PostosComRemedioAdapter mAdapter;
    Toolbar mToolbar;
    public String remedioID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        remedioID = getIntent().getStringExtra("remedioID");

        mToolbar = (Toolbar)findViewById(R.id.tool_bar);
        mToolbar.setTitle(DBMain.shared().mRemedios.get(remedioID).principio_ativo);
        Button logoutButton = (Button) findViewById(R.id.toolbar_logout);
        logoutButton.setVisibility(View.GONE);

        setSupportActionBar(mToolbar);

        mAdapter = new PostosComRemedioAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabBar);
        tabLayout.setupWithViewPager(mViewPager);

    }


}
