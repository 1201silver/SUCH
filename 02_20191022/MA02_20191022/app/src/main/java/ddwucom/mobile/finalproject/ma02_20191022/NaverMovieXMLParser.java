package ddwucom.mobile.finalproject.ma02_20191022;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class NaverMovieXMLParser {

    public static final String TAGS = "SearchMovieActivity";

    public enum TagType {NONE, TITLE, IMAGE, SUBTITLE, PUBDATE, DIRECTOR, ACTOR, RATING}

    final static String TAG_ITEM = "item";
    final static String TAG_TITLE = "title";
    final static String TAG_IMG = "image";
    final static String TAG_SUBTITLE = "subtitle";
    final static String TAG_PUBDATE = "pubDate";
    final static String TAG_DIRECTOR = "director";
    final static String TAG_ACTOR = "actor";
    final static String TAG_RATING = "userRating";

    public NaverMovieXMLParser() {
    }

    public ArrayList<MovieDTO> parse(String xml) {

        ArrayList<MovieDTO> resultList = new ArrayList();
        MovieDTO dto = null;

        TagType tagType = TagType.NONE;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals(TAG_ITEM)){
                            dto = new MovieDTO();
                        }
                        else if (parser.getName().equals(TAG_TITLE)) {
                            if (dto != null) tagType = TagType.TITLE;
                        }
                        else if (parser.getName().equals(TAG_IMG)) {
                            if (dto != null) tagType = TagType.IMAGE;
                        }
                        else if (parser.getName().equals(TAG_SUBTITLE)) {
                            if (dto != null) tagType = TagType.SUBTITLE;
                        }
                        else if (parser.getName().equals(TAG_PUBDATE)) {
                            if (dto != null) tagType = TagType.PUBDATE;
                        }
                        else if (parser.getName().equals(TAG_DIRECTOR)) {
                            if (dto != null) tagType = TagType.DIRECTOR;
                        }
                        else if (parser.getName().equals(TAG_ACTOR)) {
                            if (dto != null) tagType = TagType.ACTOR;
                        }
                        else if (parser.getName().equals(TAG_RATING)) {
                            if (dto != null) tagType = TagType.RATING;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(TAG_ITEM)) {
                            resultList.add(dto);
                            dto = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch (tagType) {
                            case TITLE:
                                dto.setTitle(parser.getText());
                                break;
                            case IMAGE:
                                dto.setImgLink(parser.getText());
                                break;
                            case SUBTITLE:
                                dto.setSubtitle(parser.getText());
                                break;
                            case PUBDATE:
                                dto.setPubDate(parser.getText());
                                break;
                            case DIRECTOR:
                                dto.setDirector(parser.getText());
                                break;
                            case ACTOR:
                                dto.setActor(parser.getText());
                                break;
                            case RATING:
                                float rt = (float) Float.parseFloat(parser.getText());
                                String format = String.format("%.2f", rt);
                                dto.setRating(rt);
                                break;
                        }
                        tagType = TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
//            Log.d(TAGS, "error in parser " +e);
            e.printStackTrace();
        }
//        Log.d(TAGS, "in parser resutList: " +resultList);
        return resultList;
    }
}
