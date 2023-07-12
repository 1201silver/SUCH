package ddwucom.mobile.finalproject.ma02_20191022;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class MovieDBManager {

    public static final String TAG = "MovieDBManager";

    MovieDBHelper movieDBHelper;
    Cursor cursor = null;

    public MovieDBManager(Context context) {
       movieDBHelper = new MovieDBHelper(context);
    }

    public ArrayList<MovieDTO> getAllMovie() {
        ArrayList<MovieDTO> movieList = new ArrayList();

        SQLiteDatabase db = movieDBHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + MovieDBHelper.TABLE_NAME, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(MovieDBHelper.COL_ID));
            String title = cursor.getString(cursor.getColumnIndex(MovieDBHelper.COL_TITLE));
            String imgLink = cursor.getString(cursor.getColumnIndex(MovieDBHelper.COL_IMGLINK));
            String imgFileName = cursor.getString(cursor.getColumnIndex(MovieDBHelper.COL_IMGFILE));
            String subTitle = cursor.getString(cursor.getColumnIndex(MovieDBHelper.COL_SUBTITLE));
            String pubDate = cursor.getString(cursor.getColumnIndex(MovieDBHelper.COL_PUBDATE));
            String director = cursor.getString(cursor.getColumnIndex(MovieDBHelper.COL_DIRECTOR));
            String actor = cursor.getString(cursor.getColumnIndex(MovieDBHelper.COL_ACTOR));
            float rating = cursor.getFloat(cursor.getColumnIndex(MovieDBHelper.COL_RATING));

            float userRating = cursor.getFloat(cursor.getColumnIndex(MovieDBHelper.USER_COL_RATING));
            String memo = cursor.getString(cursor.getColumnIndex(MovieDBHelper.USER_COL_MEMO));

            Log.d(TAG, "in DBManager / title: " +title+ "imglink: " +imgLink);
            movieList.add(new MovieDTO(id, title, imgLink, imgFileName, subTitle, pubDate, director, actor, rating, userRating, memo));
        }

        cursor.close();
        movieDBHelper.close();

        return movieList;
    }

    // 영화 추가
    public boolean addScrapMovie(MovieDTO newMovie) {
        SQLiteDatabase db = movieDBHelper.getWritableDatabase();

        Log.d(TAG, "in DBManager / addScrapMovie: " +newMovie);

        ContentValues value = new ContentValues();
        value.put(MovieDBHelper.COL_TITLE, newMovie.getTitle());
        value.put(MovieDBHelper.COL_IMGLINK, newMovie.getImgLink());
        value.put(MovieDBHelper.COL_IMGFILE, newMovie.getImgFileName());
        value.put(MovieDBHelper.COL_SUBTITLE, newMovie.getSubtitle());
        value.put(MovieDBHelper.COL_PUBDATE, newMovie.getPubDate());
        value.put(MovieDBHelper.COL_DIRECTOR, newMovie.getDirector());
        value.put(MovieDBHelper.COL_ACTOR, newMovie.getActor());
        value.put(MovieDBHelper.COL_RATING, newMovie.getRating());

        value.put(MovieDBHelper.USER_COL_RATING, newMovie.getUserRating());
        value.put(MovieDBHelper.USER_COL_MEMO, newMovie.getMemo());

        long count = db.insert(MovieDBHelper.TABLE_NAME, null, value);

        if(count > 0) return true;
        return false;
    }

    // 영화 삭제
    public boolean removeScrapMovie(long id) {
        SQLiteDatabase db = movieDBHelper.getWritableDatabase();

        String whereClause = MovieDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] {String.valueOf(id)};

        int result = db.delete(MovieDBHelper.TABLE_NAME, whereClause, whereArgs);

        movieDBHelper.close();

        if(result > 0) return true;
        return false;
    }

    // 관람후기, 메모 수정
    public boolean updateMovie(MovieDTO upMovie) {
        SQLiteDatabase db = movieDBHelper.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(movieDBHelper.USER_COL_RATING, upMovie.getUserRating());
        value.put(movieDBHelper.USER_COL_MEMO, upMovie.getMemo());

        String whereClause = MovieDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] {String.valueOf(upMovie.get_id())};

        int result = db.update(MovieDBHelper.TABLE_NAME, value, whereClause, whereArgs);

        movieDBHelper.close();

        if(result > 0) return true;
        return false;
    }

    // 스크랩한 영화 검색
    public ArrayList<MovieDTO> searchScrapMovie(String movieTitle) {

        ArrayList<MovieDTO> resultList = new ArrayList();

        SQLiteDatabase db = movieDBHelper.getReadableDatabase();

        String selection = MovieDBHelper.COL_TITLE + "=?";
        String[] selectArgs = new String[] {movieTitle};

        Cursor cursor = db.query(MovieDBHelper.TABLE_NAME, null, selection, selectArgs, null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(MovieDBHelper.COL_ID));
            String title = cursor.getString(cursor.getColumnIndex(MovieDBHelper.COL_TITLE));
            String imgLink = cursor.getString(cursor.getColumnIndex(MovieDBHelper.COL_IMGLINK));
            String imgFileName = cursor.getString(cursor.getColumnIndex(MovieDBHelper.COL_IMGFILE));
            String subTitle = cursor.getString(cursor.getColumnIndex(MovieDBHelper.COL_SUBTITLE));
            String pubDate = cursor.getString(cursor.getColumnIndex(MovieDBHelper.COL_PUBDATE));
            String director = cursor.getString(cursor.getColumnIndex(MovieDBHelper.COL_DIRECTOR));
            String actor = cursor.getString(cursor.getColumnIndex(MovieDBHelper.COL_ACTOR));
            float rating = cursor.getFloat(cursor.getColumnIndex(MovieDBHelper.COL_RATING));

            float userRating = cursor.getFloat(cursor.getColumnIndex(MovieDBHelper.USER_COL_RATING));
            String memo = cursor.getString(cursor.getColumnIndex(MovieDBHelper.USER_COL_MEMO));

            resultList.add(new MovieDTO(id, title, imgLink, imgFileName, subTitle, pubDate, director, actor, rating, userRating, memo));
        }

        cursor.close();
        movieDBHelper.close();

        return resultList;
    }


    public void close() {
        if (movieDBHelper != null) movieDBHelper.close();
        if (cursor != null) cursor.close();
    };

}
