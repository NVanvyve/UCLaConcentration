package groupe.onze.uclaconcentration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by nicolasvanvyve in mai 2017.
 */

public class EndOfTutoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_end_tuto,container,false);

        SharedPreferences mPrefs = this.getActivity().getSharedPreferences("label",0);
         final SharedPreferences.Editor mEditor = mPrefs.edit();

        ImageView i = (ImageView) rootView.findViewById(R.id.imageButtonTuto);
        i.setImageResource(R.drawable.final_button);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.putBoolean("tuto",true).apply();
                Intent s = new Intent(getActivity(),MainActivity.class);
                startActivity(s);
            }
        });
        return rootView;
    }
}

