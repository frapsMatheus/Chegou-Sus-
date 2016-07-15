package cadesus.co.cadesus.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import cadesus.co.cadesus.DB.DBLogin;
import cadesus.co.cadesus.DB.DBUser;
import cadesus.co.cadesus.DB.Entidades.User;
import cadesus.co.cadesus.MeusPostos.MeusPostosActivity;
import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/13/16.
 */
public class SettingsFragment extends PreferenceFragment {

    int CASA_PICKER_REQUEST = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);

        Preference minhaCasa = findPreference("pref_key_casa");
        minhaCasa.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()), CASA_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        Preference meusPostos = findPreference("pref_key_meus_postos");
        meusPostos.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent myIntent = new Intent(getActivity(), MeusPostosActivity.class);
                startActivity(myIntent);
                return true;
            }
        });

        Preference logout = findPreference("pref_key_logout");
        logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DBLogin.shared().logOutUser();
                getActivity().finish();
                return true;
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CASA_PICKER_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(),data);
                User user = User.shared();
                user.setLocation(place.getLatLng().latitude, place.getLatLng().longitude);
                DBUser.shared().saveUser();
                Toast.makeText(getActivity(), "Localização redefinida.", Toast.LENGTH_LONG).show();
            }
        }
    }
}

