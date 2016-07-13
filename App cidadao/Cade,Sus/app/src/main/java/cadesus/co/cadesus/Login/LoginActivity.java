package cadesus.co.cadesus.Login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import cadesus.co.cadesus.DB.DBLogin;
import cadesus.co.cadesus.DB.DBUser;
import cadesus.co.cadesus.DB.Entidades.User;
import cadesus.co.cadesus.R;

/**
 * Created by Fraps on 10/07/2016.
 */
public class LoginActivity extends Activity implements LoginCallback {

    int CASA_PICKER_REQUEST = 100;

    Button mLoginButton;
    Button mCreateButton;
    Button mForgotPassword;

    EditText mEmail;
    EditText mPassword;

    ProgressDialog mProgressDialog;
    AlertDialog.Builder mErrorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (EditText)findViewById(R.id.login_email);
        mPassword = (EditText)findViewById(R.id.login_password);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Carregando");

        createErrorDialog();
        setupCreate();
        setupForgotPassword();
        setupLogin();
    }

    private void createErrorDialog()
    {
        mErrorDialog = new AlertDialog.Builder(this);
        mErrorDialog.setCancelable(true);
    }

    private void setupCreate()
    {
        mCreateButton = (Button)findViewById(R.id.login_create_account_button);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!contemErros()) {
                    String email = mEmail.getText().toString();
                    String senha = mPassword.getText().toString();
                    showProgressDialog();
                    DBLogin.shared().setupCallback(LoginActivity.this);
                    DBLogin.shared().createUser(email,senha);
                }
            }
        });
    }

    private void setupLogin()
    {
        mLoginButton = (Button)findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!contemErros()) {
                    String email = mEmail.getText().toString();
                    String senha = mPassword.getText().toString();
                    showProgressDialog();
                    DBLogin.shared().setupCallback(LoginActivity.this);
                    DBLogin.shared().loginUser(email,senha);
                }
            }
        });
    }

    private void setupForgotPassword()
    {
        mForgotPassword = (Button)findViewById(R.id.login_forgot_password);
        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString();
                if (email.length() == 0) {
                    mEmail.setError("Email inválido.");
                    return;
                }
                Toast toast = Toast.makeText(LoginActivity.this,
                        "Um email foi enviado para você criar uma nova senha.", Toast.LENGTH_LONG);
                DBLogin.shared().recoverPassword(email);
                toast.show();
            }
        });
    }

    private boolean contemErros()
    {
        int contaErros = 0;
        String email = mEmail.getText().toString();
        if (email.length() == 0) {
            mEmail.setError("Email inválido.");
            contaErros++;
        }
        String senha = mPassword.getText().toString();
        if (senha.length() < 6) {
            mPassword.setError("Senha muito pequena.");
            contaErros++;
        }
        if (contaErros>0) {
            return true;
        } else {
            return false;
        }
    }

    private void showProgressDialog()
    {
        mProgressDialog.show();
    }

    private void hideProgressDialog()
    {
        mProgressDialog.hide();
    }

    @Override
    public void userLoggedIn() {
        hideProgressDialog();
        finish();
    }

    @Override
    public void userCreatedAccount()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione sua casa.");
        builder.setMessage("Sua residência será utilizada para facilitar o recebimento de " +
                "notificações de postos de saúde perto de você.");
        builder.setPositiveButton("Selecionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PlacePicker.IntentBuilder pickerBuilder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(pickerBuilder.build(LoginActivity.this),
                            CASA_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Não selecionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    @Override
    public void errorHappened(String errorMessage)
    {
        hideProgressDialog();
        mErrorDialog.setMessage(errorMessage);
        AlertDialog alert = mErrorDialog.create();
        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CASA_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                User user = new User();
                user.setLocation(place.getLatLng().latitude, place.getLatLng().longitude);
                DBUser.shared().saveUser(DBLogin.shared().getUserID(),user);
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }
}
