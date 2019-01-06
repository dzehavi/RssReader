package dzehavi.com.rssreader;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
  * SAX tag handler. The Class contains a list of RssFeedItems which is being filled while the parser is working
  */
public class RssParseHandler extends DefaultHandler {
    // List of items parsed
    private List<RssFeedItem> rssItems;
    // a local reference to an RssFeedItem which is constructed while parser is working on an item tag
    private RssFeedItem currentItem;
    // Parsing title indicator
    private boolean parsingTitle;
    // Parsing link indicator
    private boolean parsingLink;
    // Parsing decription indicator
    private boolean parsingDescription;
    // Parsing guid indicator
    private boolean parsingGuid;

    public RssParseHandler() {
        rssItems = new ArrayList();
    }
	
    /** 
	 * This method will be called when parsing is done.
	 * @return the lst of parsed RssFeedItems
	 */
    public List<RssFeedItem> getItems() {
        return rssItems;
    }
    /**
	 * The StartElement method creates an empty RssFeedItem object when an item start tag is being processed. 
	 * When a title, link, description or guid tag are being processed appropriate indicators are set to true.
	 */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("item".equals(qName)) {
            currentItem = new RssFeedItem();
        } else if ("title".equals(qName)) {
            parsingTitle = true;
        } else if ("link".equals(qName)) {
            parsingLink = true;
        } else if ("description".equals(qName)) {
            parsingDescription = true;
        } else if ("guid".equals(qName)) {
            parsingGuid = true;
        }
    }
    /** 
	 * The EndElement method adds the  current RssFeedItem to the list when a closing item tag is processed. 
	 * It sets appropriate indicators to false -  when title, link, description and guid closing tags are processed
	 */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("item".equals(qName)) {
            rssItems.add(currentItem);
            currentItem = null;
        } else if ("title".equals(qName)) {
            parsingTitle = false;
        } else if ("link".equals(qName)) {
            parsingLink = false;
        } else if ("description".equals(qName)) {
            parsingDescription = false;
        } else if ("guid".equals(qName)) {
            parsingGuid = false;
        }
    }
	
    /** 
	 * fills current RssFeedItem object with data when title, link, description and guid tag content is being processed
	 */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (parsingTitle) {
            if (currentItem != null) {
                currentItem.setTitle(new String(ch, start, length));
            }
        } else if (parsingLink) {
            if (currentItem != null) {
                currentItem.setLink(new String(ch, start, length));
                parsingLink = false;
            }
        } else if (parsingDescription) {
            if (currentItem != null) {
                currentItem.setDescription(new String(ch, start, length));
                parsingDescription = false;
            }
        } else if (parsingGuid) {
            if (currentItem != null) {
                currentItem.setGuid(new String(ch, start, length));
                parsingGuid = false;
            }
        }
    }
}