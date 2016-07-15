package cadesus.co.cadesus.Settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/13/16.
 */
public class SettingsActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_template);
        Button logoutButton = (Button) findViewById(R.id.toolbar_logout);
        logoutButton.setVisibility(View.GONE);

        mToolbar = (Toolbar)findViewById(R.id.tool_bar);
        mToolbar.setTitle("Configurações");
        setSupportActionBar(mToolbar);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(R.id.template_container, new SettingsFragment())
                .commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
