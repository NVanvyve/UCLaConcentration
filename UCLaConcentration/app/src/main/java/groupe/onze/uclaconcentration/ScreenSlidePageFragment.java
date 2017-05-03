package groupe.onze.uclaconcentration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by nicolasvanvyve in mai 2017.
 */

public class ScreenSlidePageFragment extends Fragment {

    private static final int MEGABYTE = (1024*1024);

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences mPrefs = this.getActivity().getSharedPreferences("label",0);
        final SharedPreferences.Editor mEditor = mPrefs.edit();

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_tuto,container,false);
        try {
            int image = R.drawable.tuto_firstview;

            Bundle bundle = getArguments();
            if (bundle != null) {
                image = bundle.getInt("image_tuto",0);
            } else {
                Log.i("TutoFrag","bundle is null");
            }
            ImageView screen = (ImageView) rootView.findViewById(R.id.screenshot);
            screen.setImageResource(image);

            return rootView;
        }
        catch (OutOfMemoryError e){
            Log.e("TUTO",e.getMessage());
            Toast.makeText(getActivity().getApplicationContext(),"Out Of Memory Error - Skip Tutorial",Toast.LENGTH_SHORT).show();
            mEditor.putBoolean("tuto",true).apply();
            startActivity(new Intent(getActivity(),MainActivity.class));
            return rootView;
        }
    }
}

