package dzehavi.com.rssreader;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

/**
 * An AsyncTask that fetches an RSS feed on a background threa, 
 * and keeps the UI interactive during the fetch.
 */
public class RssFeedFetchTask extends AsyncTask<Void, Void, Boolean> {

	// notifies the listener
    interface OnRssFeedFetchListener {
        void onFeedFetched(RssFeedFetchResponse response);
        void onFeedFetchError(String error);
    }

	// the link of the RSS feed to fetch
    private final String feedLink;
	// the returned response`
    private RssFeedFetchResponse feedFetchResponse;
	// listener to fetch end
    private OnRssFeedFetchListener listener;

    RssFeedFetchTask(String feedLink, OnRssFeedFetchListener listener) {
        this.listener = listener;
        this.feedLink = feedLink;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
			// do the actual fetching in a background thread
            RssReader rssReader = new RssReader(feedLink);
            List<RssFeedItem> items = rssReader.getItems();
            feedFetchResponse = new RssFeedFetchResponse("title", feedLink, "decription", items);
            return true;
        } catch (IOException e) {
            return false;
        } catch (XmlPullParserException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            listener.onFeedFetched(feedFetchResponse);
        } else {
            listener.onFeedFetchError("Failed to fetch feed " + feedLink);
        }
    }
}
