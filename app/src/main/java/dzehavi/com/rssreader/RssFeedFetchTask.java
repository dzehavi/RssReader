package dzehavi.com.rssreader;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

public class RssFeedFetchTask extends AsyncTask<Void, Void, Boolean> {

    interface OnRssFeedFetchListener {
        void onFeedFetched(RssFeedFetchResponse response);
        void onFeedFetchError(String error);
    }

    private final String feedLink;
    private RssFeedFetchResponse feedFetchResponse;
    private OnRssFeedFetchListener listener;

    RssFeedFetchTask(String feedLink, OnRssFeedFetchListener listener) {
        this.listener = listener;
        this.feedLink = feedLink;
    }

    @Override
    protected void onPreExecute() {
        // show refreshing view
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
//            URL url = new URL(feedLink);
//            InputStream inputStream = url.openConnection().getInputStream();
//            feedFetchResponse = RssParser.parseFeed(inputStream);
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

        // remove refreshing view

        if (success) {
            // onPostExecute runs on UI Thread
            listener.onFeedFetched(feedFetchResponse);
        } else {
            listener.onFeedFetchError("Failed to fetch feed " + feedLink);
        }
    }
}
