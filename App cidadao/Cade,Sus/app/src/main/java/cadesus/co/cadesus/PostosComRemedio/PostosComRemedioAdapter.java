package cadesus.co.cadesus.PostosComRemedio;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


/**
 * Created by fraps on 7/11/16.
 */
public class PostosComRemedioAdapter extends FragmentStatePagerAdapter
{

    public PostosComRemedioAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position){
            case 0:
                return new PertoDeCasaFragment();
            case 1:
                return new PertoDeMimFragment();
            default:
                return new PreferidosFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Perto de casa";
            case 1:
                return "Perto de mim";
            default:
                return "Preferidos";
        }
    }
}
