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

    // Constructor
    public ImageAdapter(Context c) {
        mContext = c;
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
            if (position <= 3)
                size = sizeBig;
            else if (position <= 5 && position != 4)
                size = sizeAverage;
            else
                size = sizeSmall;
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
    public Integer[] mThumbIds = {
            R.drawable.loupe, R.drawable.profile,
            R.drawable.friends, R.drawable.message,
            R.drawable.meet, R.drawable.settings,
            R.drawable.about, R.drawable.logout
    };
}

