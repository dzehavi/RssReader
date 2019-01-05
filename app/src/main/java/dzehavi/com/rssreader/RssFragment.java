package dzehavi.com.rssreader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RssFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RssFragment extends Fragment implements RssFeedFetchTask.OnRssFeedFetchListener{

    public static final int RSS_FETCH_PERIOD = 5000;
    final String BUSINESS_LINK = "http://feeds.reuters.com/reuters/businessNews";
    final String ENTERTAINMENT_LINK = "http://feeds.reuters.com/reuters/entertainment";
    final String ENVIRONMENT_LINK = "http://feeds.reuters.com/reuters/environment";

    private RecyclerView businessRecyclerView;
    private RecyclerView.Adapter businessAdapter;
    private RecyclerView.LayoutManager businessLayoutManager;
    private TableRow businessRow;
    private RecyclerView entAndEnvRecyclerView;
    private RecyclerView.Adapter entAndEnvAdapter;
    private RecyclerView.LayoutManager entAndEnvLayoutManager;
    private TableRow entAndEnvRow;

    TableLayout businessTable;
    TableLayout entEnvTable;

    // prevents displaying the same item twice
    HashSet<String> shownFeedItems = new HashSet<>();

    // triggers a refresh every 5 seconds
    private Timer timer;

    // the main activity wants to know when a feed is selected
    private OnFragmentInteractionListener onFragmentInteractionListener;

    public RssFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rssFragmentView = inflater.inflate(R.layout.fragment_rss, container, false);
        businessTable = rssFragmentView.findViewById(R.id.business_table);
        entEnvTable = rssFragmentView.findViewById(R.id.ent_env_table);

        return rssFragmentView;
    }

    int lastEntertainmentIndex = 0;
    // callback from the RSS fetch AsyncTask
    @Override
    public void onFeedFetched(RssFeedFetchResponse response) {
        switch(response.link) {
            case BUSINESS_LINK:
                for (RssFeedItem rssFeedItem: response.items) {
                    TableRow newRow = createRssFeedItemView(rssFeedItem);
                    if (newRow == null)
                        return;
                    businessTable.addView(newRow);
                }
                break;
            case ENTERTAINMENT_LINK:
                for (RssFeedItem rssFeedItem: response.items) {
                    TableRow newRow = createRssFeedItemView(rssFeedItem);
                    if (newRow == null)
                        return;
                    entEnvTable.addView(newRow);
                    // this is a bookmark for where the first environment item will be
                    lastEntertainmentIndex++;
                }
                break;
            case ENVIRONMENT_LINK:
                for (RssFeedItem rssFeedItem: response.items) {
                    TableRow newRow = createRssFeedItemView(rssFeedItem);
                    if (newRow == null)
                        return;
                    // add new environment items after the last entertainment item
                    entEnvTable.addView(newRow, lastEntertainmentIndex);
                }
                break;
        };
    }

    @Nullable
    private TableRow createRssFeedItemView(RssFeedItem rssFeedItem) {
        // avoid re-adding the same item
        if (shownFeedItems.contains(rssFeedItem.getGuid())) {
            return null;
        }
        shownFeedItems.add(rssFeedItem.getGuid());

        TableRow newRow = new TableRow(getContext());
        View rssItemView = getLayoutInflater().inflate(R.layout.item_rss_feed, null);

        final TextView rssItemTitleView = rssItemView.findViewById(R.id.rss_item_title);
        rssItemTitleView.setText(rssFeedItem.getTitle());

        TextView rssItemDescriptionView = rssItemView.findViewById(R.id.rss_item_description);
        rssItemDescriptionView.setText(rssFeedItem.getDescription());

        rssItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = ((TextView)view.findViewById(R.id.rss_item_title)).getText().toString();
                String description = ((TextView)view.findViewById(R.id.rss_item_description)).getText().toString();
                // present the feed title in the InfoFragment's empty label
                if (onFragmentInteractionListener != null) {
                    onFragmentInteractionListener.onFeedSelected(title);
                }
                // push a new activity with the description of the selected feed
                Intent intent = new Intent(getContext(), RssDescriptionActivity.class);
                intent.putExtra("description", description);
                startActivity(intent);

            }
        });

        newRow.addView(rssItemView);
        return newRow;
    }

    @Override
    public void onFeedFetchError(String error) {
        Toast.makeText(getActivity(),
                error,
                Toast.LENGTH_LONG).show();
    }

    // the main activity wants to know when a feed is selected
    public interface OnFragmentInteractionListener {
        void onFeedSelected(String selectedRssFeedTitle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        // refresh every 5 seconds
        startTimer();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentInteractionListener = null;

        // cleanup
        stopTimer();
    }

    private void startTimer() {
        if(timer != null) {
            return;
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new RssFeedFetchTask(BUSINESS_LINK, RssFragment.this) .execute();
                new RssFeedFetchTask(ENTERTAINMENT_LINK, RssFragment.this).execute();
                new RssFeedFetchTask(ENVIRONMENT_LINK, RssFragment.this) .execute();
            }
        }, 0, RSS_FETCH_PERIOD);
    }

    private void stopTimer() {
        timer.cancel();
        timer = null;
    }
}
