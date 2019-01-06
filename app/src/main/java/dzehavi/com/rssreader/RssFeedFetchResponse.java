package dzehavi.com.rssreader;

import java.util.List;

/**
 * encapsulates the data that is received from fetching an RSS feed.
 */
class RssFeedFetchResponse {

    final String title;
    final String link;
    final String description;
    final List<RssFeedItem> items;

    RssFeedFetchResponse(String title, String link, String description, List<RssFeedItem> items) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.items = items;
    }
}
