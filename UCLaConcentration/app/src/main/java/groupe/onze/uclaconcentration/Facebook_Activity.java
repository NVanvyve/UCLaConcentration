package groupe.onze.uclaconcentration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookActivity;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

/**
 * Created by nicolasvanvyve on 12/03/17.
 */

public class Facebook_Activity extends AppCompatActivity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebook_layout);

        Button back = (Button) findViewById(R.id.facebook_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(Facebook_Activity.this, MainActivity.class);
                startActivity(s);
            }
        });


        Bundle inBundle = getIntent().getExtras();
        String name = inBundle.get("name").toString();
        String surname = inBundle.get("surname").toString();
        String imageUrl = inBundle.get("imageUrl").toString();


        TextView nameView = (TextView) findViewById(R.id.nameAndSurname);
        nameView.setText("" + name + " " + surname);

        new DownloadImage((ImageView) findViewById(R.id.profileImage)).execute(imageUrl);

        FacebookSdk.sdkInitialize(getApplicationContext());

        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                Intent login = new Intent(Facebook_Activity.this, Connexion.class);
                startActivity(login);
                finish();
            }
        });


    }


}
