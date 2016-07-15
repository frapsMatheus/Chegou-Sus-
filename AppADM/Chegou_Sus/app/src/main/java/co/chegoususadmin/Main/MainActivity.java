package co.chegoususadmin.Main;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import co.chegoususadmin.AdicionarRemedios.AdicionarRemediosFragment;
import co.chegoususadmin.DB.DBLogin;
import co.chegoususadmin.Login.LoginActivity;
import co.chegoususadmin.R;

public class MainActivity extends AppCompatActivity {

    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        Button logoutButton = (Button) findViewById(R.id.toolbar_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBLogin.shared().logOutUser();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        addFragment(R.id.template_container,new MeusRemedios()
                ,MeusRemedios.FRAGMENT_TAG);
        mToolbar = (Toolbar)findViewById(R.id.tool_bar);
        mToolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!DBLogin.shared().isLoggedUser()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
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
