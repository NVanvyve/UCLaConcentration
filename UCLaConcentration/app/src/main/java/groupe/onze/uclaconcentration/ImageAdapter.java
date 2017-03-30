package groupe.onze.uclaconcentration;

/**
 * Created by jaigret on 28/03/17.
 */

import android.content.Context;
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
    private Integer[] mThumbIds;
    private boolean onPlay = false;

    // Constructor
    public ImageAdapter(Context c) {
        mContext = c;
        mThumbIds = mThumbIds1;
    }


    public ImageAdapter(Context c, boolean onPlay) {
        mContext = c;
        this.onPlay = onPlay;
        if (onPlay)
            mThumbIds = mThumbIds2;
        else
            mThumbIds = mThumbIds1;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            int size = 1;
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

    public void switchImages()
    {
        if (onPlay)
            mThumbIds = mThumbIds2;
        else
            mThumbIds = mThumbIds1;
        onPlay = !onPlay;
    }

    // Keep all Images in array
    private Integer[] mThumbIds1 = {
            R.drawable.play, R.drawable.pause,
            R.drawable.ade, R.drawable.calendar,
            R.drawable.caddie, R.drawable.sport,
            R.drawable.facebook
    };


    private Integer[] mThumbIds2 = {
            R.drawable.stop, R.drawable.pause,
            R.drawable.ade, R.drawable.calendar,
            R.drawable.caddie, R.drawable.sport,
            R.drawable.facebook
    };

}

