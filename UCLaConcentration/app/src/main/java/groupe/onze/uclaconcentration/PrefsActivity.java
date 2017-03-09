package groupe.onze.uclaconcentration;

import java.util.Arrays;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class PrefsActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private TextView message;
    private CallbackManager callbackManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        final SharedPreferences mPrefs = getSharedPreferences("label", 0);
        final EditText ed_codes = (EditText) findViewById(R.id.ed_codes);

        ed_codes.setText(mPrefs.getString("codes_cours", null));

        Button save = (Button) findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor mEditor = mPrefs.edit();
                mEditor.putString("codes_cours",ed_codes.getText().toString()).commit();
                Intent s = new Intent(PrefsActivity.this, MainActivity.class);
                startActivity(s);
            }
        });


        loginButton = (LoginButton) findViewById(R.id.login_button);
        message = (TextView) findViewById(R.id.facebook_message);

        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        callbackManager = CallbackManager.Factory.create();


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {
                Log.v("LoginActivity", "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.v("LoginActivity", exception.getCause().toString());
            }
        });
    }


}