package cadesus.co.cadesus.Login;

/**
 * Created by fraps on 7/13/16.
 */
public interface LoginCallback
{

    void userLoggedIn();

    void userCreatedAccount();

    void errorHappened(String errorMessage);

}
