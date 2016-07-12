package cadesus.co.cadesus.Main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import java.util.ArrayList;

import cadesus.co.cadesus.MapsFragment;
import cadesus.co.cadesus.MeusRemedios;

/**
 * Created by fraps on 7/11/16.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private final ArrayList<String> mFragmentTitleList = new ArrayList<>();

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position){
            case 1:
                return new MeusRemedios();
            default:
                return new MapsFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 1:
                return "Meus remeédios";
            default:
                return "Mapa";
        }
    }
}
