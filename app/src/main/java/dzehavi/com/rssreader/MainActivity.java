package dzehavi.com.rssreader;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements RssFragment.OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the tabs. 
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the tab contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the two tabs
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the tabs.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // factory
            switch(position) {
                case 0:
                    return new InfoFragment();
                case 1:
                    return new RssFragment();
                default:
                    throw new IllegalArgumentException("Position: " + position);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

	/**
	 * update InfoFragment with the title of the selected feed in RssFragment
	 * inter-fragment communication is done through the parent activity
	 * @param selectedFeedTitle the title of the feed that was elected in RssFragment
	 */
    @Override
    public void onFeedSelected(String selectedFeedTitle) {
        mListener.onFeedTitle(selectedFeedTitle);
    }

    interface OnFeedTitleListener {
        void onFeedTitle(String feedTitle);
    }
    private OnFeedTitleListener mListener;

    void setFeedTitleListener(OnFeedTitleListener listener) {
        mListener = listener;
    }

}
