package dzehavi.com.rssreader;

import java.util.List;

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
