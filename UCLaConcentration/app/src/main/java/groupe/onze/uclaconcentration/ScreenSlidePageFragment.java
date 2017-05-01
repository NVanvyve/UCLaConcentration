package groupe.onze.uclaconcentration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by nicolasvanvyve in mai 2017.
 */

public class ScreenSlidePageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_tuto, container, false);

        int image = R.drawable.tuto_firstview;

        Bundle bundle = getArguments();
        if (bundle != null) {
            image = bundle.getInt("image_tuto",0);
        }
        else {
            Log.i("TutoFrag","bundle is null");
        }
        ImageView screen = (ImageView) rootView.findViewById(R.id.screenshot);
        screen.setImageResource(image);

        return rootView;
    }
}

