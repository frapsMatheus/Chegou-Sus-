package cadesus.co.cadesus.AdicionarRemedios;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/13/16.
 */
public class AdicionarRemediosActivity extends AppCompatActivity
{

    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        Button logoutButton = (Button) findViewById(R.id.toolbar_logout);
        logoutButton.setVisibility(View.GONE);
        addFragment(R.id.template_container,new AdicionarRemediosFragment()
                ,AdicionarRemediosFragment.FRAGMENT_TAG);
        mToolbar = (Toolbar)findViewById(R.id.tool_bar);
        mToolbar.setTitle("Adicionar remédio");
        setSupportActionBar(mToolbar);
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
}
