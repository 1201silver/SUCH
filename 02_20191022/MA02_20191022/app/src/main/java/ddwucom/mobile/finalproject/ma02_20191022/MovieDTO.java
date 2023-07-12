package ddwucom.mobile.finalproject.ma02_20191022;

import android.text.Html;
import android.text.Spanned;

import java.io.Serializable;

public class MovieDTO implements Serializable {

    private int _id;
    private String title;
    private String hyperlink;
    private String imgLink;
    private String imgFileName;
    private String subtitle;
    private String pubDate;
    private String director;
    private String actor;
    private float rating;

    private float userRating;
    private String memo;

    public MovieDTO() {
        title = null;
        imgLink = null;
        imgFileName = null;
        subtitle = null;
        pubDate = null;
        director = null;
        actor = null;
        rating = 0;
    }


    public MovieDTO(int _id, String title, String imgLink, String imgFileName, String subtitle, String pubDate, String director, String actor, float rating,
                    float userRating, String memo) {
        this._id = _id;
        this.title = title;
        this.imgLink = imgLink;
        this.imgFileName = imgFileName;
        this.subtitle = subtitle;
        this.pubDate = pubDate;
        this.director = director;
        this.actor = actor;
        this.rating = rating;
        this.userRating = userRating;
        this.memo = memo;
    }

    public int get_id() {
        return _id;
    }
    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        Spanned spanned = Html.fromHtml(title);
        return spanned.toString();
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getHyperlink() {
        return hyperlink;
    }
    public void setHyperlink(String hyperlink) {
        this.hyperlink = hyperlink;
    }

    public String getImgLink() {
        return imgLink;
    }
    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getImgFileName() {
        return imgFileName;
    }
    public void setImgFileName(String imgFileName) {
        this.imgFileName = imgFileName;
    }

    public String getSubtitle() {
        Spanned spanned = Html.fromHtml(subtitle);
        return spanned.toString();
    }
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPubDate() {
        return pubDate;
    }
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getDirector() {
        return director;
    }
    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }
    public void setActor(String actor) {
        this.actor = actor;
    }

    public float getRating() {
        return rating;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getUserRating() {
        return userRating;
    }
    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return "MovieDTO{" +
                "_id=" + _id +
                ", title='" + title + '\'' +
                ", hyperlink='" + hyperlink + '\'' +
                ", imgLink='" + imgLink + '\'' +
                ", imgFileName='" + imgFileName + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", director='" + director + '\'' +
                ", actor='" + actor + '\'' +
                ", rating=" + rating +
                ", userRating=" + userRating +
                ", memo='" + memo + '\'' +
                '}';
    }
}
