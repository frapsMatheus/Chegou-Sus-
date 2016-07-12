package cadesus.co.cadesus.Main;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/11/16.
 */
public class MainActivity extends AppCompatActivity
{
    ViewPager mViewPager;
    MainPagerAdapter mAdapter;
    Toolbar mToolbar;


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

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabBar);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabTextColors(ColorStateList.valueOf(textColor));

    }
}
