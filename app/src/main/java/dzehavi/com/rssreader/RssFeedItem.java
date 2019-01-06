package dzehavi.com.rssreader;

/**
 * represent a single RSS feed item.
 */
public class RssFeedItem {
    private String title;
    private String link;
    private String description;
    private String guid;

    public RssFeedItem() {

    }

    public RssFeedItem(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
