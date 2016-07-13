package cadesus.co.cadesus.Main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import java.util.ArrayList;

/**
 * Created by fraps on 7/11/16.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {

    public final ArrayList<Fragment> mFragments = new ArrayList<>();

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position){
            case 1:
                mFragments.add(1,new MeusRemedios());
                return mFragments.get(1);
            default:
                mFragments.add(new MapsFragment());
                return mFragments.get(0);
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
                return "Meus remédios";
            default:
                return "Mapa";
        }
    }
}
