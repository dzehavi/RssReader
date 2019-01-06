package dzehavi.com.rssreader;

import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
  * Fecthes feed content from a given feed URL, then parses and generates a list of RssFeedItems. 
  */
public class RssReader {
    // RSS Feed URL
    private String rssUrl;
    public RssReader(String rssUrl) {
        this.rssUrl = rssUrl;
    }
    /**
     * Get RSS items. This method will be called to get the parsing process result.
     * @return list of fetched feed items
     */
    public List<RssFeedItem> getItems() throws Exception {
        // using SaxParser as the method of parsing the feed. 
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        // SAX parser handler object which handles feed elements
        RssParseHandler handler = new RssParseHandler();
        saxParser.parse(rssUrl, handler);
        // The result of the parsing process is being stored in the handler object
        return handler.getItems();
    }
}