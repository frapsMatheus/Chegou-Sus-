package co.chegoususadmin.Login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import co.chegoususadmin.DB.DBLogin;
import co.chegoususadmin.Main.MainActivity;
import co.chegoususadmin.R;

/**
 * Created by Fraps on 10/07/2016.
 */
public class LoginActivity extends Activity implements LoginCallback {

    Button mLoginButton;
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
        setupForgotPassword();
        setupLogin();
    }

    private void createErrorDialog()
    {
        mErrorDialog = new AlertDialog.Builder(this);
        mErrorDialog.setCancelable(true);
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void errorHappened(String errorMessage)
    {
        hideProgressDialog();
        mErrorDialog.setMessage(errorMessage);
        AlertDialog alert = mErrorDialog.create();
        alert.show();
    }
}
