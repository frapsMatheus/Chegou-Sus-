package cadesus.co.cadesus.MeusPostos;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import cadesus.co.cadesus.AdicionarRemedios.AdicionarRemediosFragment;
import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/14/16.
 */
public class MeusPostosActivity
        extends AppCompatActivity
{

    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        Button logoutButton = (Button) findViewById(R.id.toolbar_logout);
        logoutButton.setVisibility(View.GONE);
        addFragment(R.id.template_container,new MeusPostosFragment()
                ,MeusPostosFragment.FRAGMENT_TAG);
        mToolbar = (Toolbar)findViewById(R.id.tool_bar);
        mToolbar.setTitle("Selecionar postos");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    protected void addFragment(@IdRes int containerViewId,
                               @NonNull Fragment fragment,
                               @NonNull String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .disallowAddToBackStack()
                .commit();
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