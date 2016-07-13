package cadesus.co.cadesus;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by fraps on 7/13/16.
 */
public class DB {

    private static DB mDB = null;

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

    private DB()
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
                    }
                } else {
                    mUser = null;
                }
                // ...
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    public static DB shared()
    {
        if (mDB == null) {
            mDB = new DB();
        }
        return mDB;
    }

    public void loginUser(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email,password).addOnFailureListener(mOnFailure);
    }

    public void createUser(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email,password).addOnFailureListener(mOnFailure);
    }

    public void recoverPassword(String email)
    {
        mAuth.sendPasswordResetEmail(email);
    }

    public void logOutUser()
    {
        mAuth.signOut();
        mUser = null;
    }

}
