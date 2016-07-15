package co.chegoususadmin.DB;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import co.chegoususadmin.DB.Entidades.PostoDeSaude;
import co.chegoususadmin.Login.LoginCallback;

/**
 * Created by fraps on 7/13/16.
 */
public class DBLogin {

    private static DBLogin mDBLogin = null;

    private FirebaseAuth mAuth;
    public  FirebaseUser mUser = null;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private LoginCallback mLoginCallback;

    private OnFailureListener mOnFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            if (mLoginCallback != null) {
                mLoginCallback.errorHappened(e.getMessage());
            }
        }
    };

    public void setupCallback(LoginCallback loginCallback)
    {
        mLoginCallback = loginCallback;
    }

    public boolean isLoggedUser()
    {
        if (mUser == null) {
            return false;
        } else {
            return true;
        }
    }

    public String getUserID()
    {
        return mUser.getUid();
    }

    private DBLogin()
    {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    if (mLoginCallback != null) {
                        mLoginCallback.userLoggedIn();
                        mLoginCallback = null;
                    }
                } else {
                    mUser = null;
                }
                // ...
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    public static DBLogin shared()
    {
        if (mDBLogin == null) {
            mDBLogin = new DBLogin();
        }
        return mDBLogin;
    }

    public void loginUser(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email,password).addOnFailureListener(mOnFailure);
    }

    public void recoverPassword(String email)
    {
        mAuth.sendPasswordResetEmail(email);
    }

    public void logOutUser()
    {
        mAuth.signOut();
        mUser = null;
        PostoDeSaude.shared().clearUser();
    }

}
