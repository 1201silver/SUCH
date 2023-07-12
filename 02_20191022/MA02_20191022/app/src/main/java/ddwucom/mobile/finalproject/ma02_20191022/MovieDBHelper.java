package ddwucom.mobile.finalproject.ma02_20191022;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDBHelper extends SQLiteOpenHelper {

    final static String DB_NAME = "movie.db";

    public final static String TABLE_NAME = "scrapMovie_table";

    public final static String COL_ID = "_id";
    public final static String COL_TITLE = "title";
    public final static String COL_IMGLINK = "imgLink";
    public final static String COL_IMGFILE = "imgFileName";
    public final static String COL_SUBTITLE = "subTitle";
    public final static String COL_PUBDATE = "pubDate";
    public final static String COL_DIRECTOR = "director";
    public final static String COL_ACTOR = "actor";
    public final static String COL_RATING = "rating";

    public final static String USER_COL_RATING = "userRating";
    public final static String USER_COL_MEMO = "memo";

    public MovieDBHelper(Context context) {
        super(context, DB_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " +TABLE_NAME+ " (" +COL_ID+ " integer primary key autoincrement, "
                +COL_TITLE+ " TEXT," +COL_IMGLINK+ " TEXT," +COL_IMGFILE+ " TEXT," +COL_SUBTITLE+
                " TEXT," +COL_PUBDATE+ " TEXT," +COL_DIRECTOR+ " TEXT," +COL_ACTOR+ " TEXT,"
                +COL_RATING+ " REAL," +USER_COL_RATING+ " REAL," +USER_COL_MEMO+ " TEXT)";

        db.execSQL(sql);

//        db.execSQL("insert into " + TABLE_NAME + " values (null, '어바웃 타임'" +
//                ", 'https://ssl.pstatic.net/imgmovie/mdi/mit110/0920/92075_P31_154949.jpg', '92075_P31_154949.jpg',  'About Time'" +
//                ", '2013', '리차드 커티스|', '도널 글리슨|레이첼 맥아담스|', '4.64', '0', '아직 안봤다')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);

    }
}
