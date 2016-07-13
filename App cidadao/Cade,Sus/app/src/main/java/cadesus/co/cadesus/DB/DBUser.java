package cadesus.co.cadesus.DB;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cadesus.co.cadesus.DB.Entidades.User;

/**
 * Created by fraps on 7/13/16.
 */
public class DBUser
{

    private static DBUser mDBUser = null;
    private final FirebaseDatabase mDB;
    private final DatabaseReference mRef;

    DBUser()
    {
        mDB = FirebaseDatabase.getInstance();
        mRef = mDB.getReference("users");
    }

    public void saveUser(String userID, User user)
    {
        mRef.child(userID).setValue(user);
    }

    public static DBUser shared()
    {
        if (mDBUser == null) {
            mDBUser = new DBUser();
        }
        return mDBUser;
    }

}
