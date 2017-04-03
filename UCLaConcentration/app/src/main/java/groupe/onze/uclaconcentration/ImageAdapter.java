package groupe.onze.uclaconcentration;

/**
 * Created by jaigret on 28/03/17.
 */

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private int sizeBig = 700;
    private int sizeSmall = 200;
    private int sizeAverage = 400;
    private int play;
    private int pause;

    // Constructor
    public ImageAdapter(Context c) {
        mContext = c;
        play = R.drawable.play;
        pause = R.drawable.pause_1;

        mThumbIds[0] = play;
        mThumbIds[1] = pause;
    }


    public ImageAdapter(Context c, boolean onPlay, boolean onPause) {

        mContext = c;

        if (onPause)
            pause = R.drawable.pause_2;
        else
            pause = R.drawable.pause_1;

        if (onPlay)
            play = R.drawable.stop;
        else
            play = R.drawable.play;

        mThumbIds[0] = play;
        mThumbIds[1] = pause;

        Log.i("TEST", "IMAGE ADAPTER : " + getCount());
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return mThumbIds[position];
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("VIEW", "POSITION : +++" + position);
        ImageView imageView;
        int size;

        if (convertView == null) {
            if (position <= 1)
                size = sizeSmall;
            else if (position == mThumbIds.length-1)
                size = sizeAverage;
            else
                size = sizeBig;
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(size, size));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }
        else
        {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // Keep all Images in array
    private Integer[] mThumbIds = {
            play, pause,
            R.drawable.ade, R.drawable.calendar,
            R.drawable.caddie, R.drawable.sport,
            R.drawable.facebook
    };

}

