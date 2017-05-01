package groupe.onze.uclaconcentration;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;


/**
 * Created by nicolasvanvyve in avr. 2017.
 */

public class SwipeTuto extends FragmentActivity {

    public static int[] image_tuto;
    ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    boolean first;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_swipe_tuto);
        MainActivity.isInBackground = false;

        first = true;

        image_tuto = new int[]{R.drawable.tuto_firstview,
                                R.drawable.tuto_menu,
                                R.drawable.tuto_etude,
                                R.drawable.tuto_pause,
                                R.drawable.tuto_settings,
                                R.drawable.tuto_sport_level,
                                R.drawable.tuto_sport,
                                R.drawable.tuto_store};

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // Nothing
        } else {
            //Select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == image_tuto.length){
                return new EndOfTutoFragment();
            } else {
                if (first){
                    first = false;
                    Toast.makeText(getApplicationContext(),getString(R.string.swipe),Toast.LENGTH_LONG).show();
                }
                ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("image_tuto",image_tuto[position]);
                fragment.setArguments(bundle);
                return fragment;
            }
        }

        @Override
        public int getCount() {
            return image_tuto.length+1;
        }
    }

    @Override
    public void onPause() {
        MainActivity.isInBackground = true;
        super.onPause();
    }

    @Override
    public void onDestroy() {
        MainActivity.isInBackground = true;
        super.onDestroy();
    }

    @Override
    public void onResume() {
        MainActivity.isInBackground = false;
        super.onResume();
    }

    @Override
    public void onStop() {
        MainActivity.isInBackground = true;
        super.onStop();
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.5f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view,float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}

