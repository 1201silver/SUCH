package ddwucom.mobile.finalproject.ma02_20191022;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class DetailSearchMovie extends AppCompatActivity {

    final static String TAG = "DetailSearchMovie";

    MovieDTO clickMovie;

    ImageView ivPoster;
    TextView tvTitle;
    TextView tvSubTitle;
    TextView tvPubDate;
    TextView tvDirector;
    TextView tvActor;
    RatingBar ratingD;

    NaverNetworkManager networkManager;
    ImageFileManager imageFileManager;

    MovieDBManager movieDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deatil_search_movie);

        clickMovie = (MovieDTO) getIntent().getSerializableExtra("clickMovie");
        Log.d(TAG, "click movie: " + clickMovie);

        imageFileManager = new ImageFileManager(this);
        networkManager = new NaverNetworkManager(this);

        movieDBManager = new MovieDBManager(this);

        ivPoster = findViewById(R.id.ivPosterD);
        tvTitle = findViewById(R.id.tvTitleD);
        tvSubTitle = findViewById(R.id.tvSubTitleD);
        tvPubDate = findViewById(R.id.tvPubDateD);
        tvDirector = findViewById(R.id.tvDirectorD);
        tvActor = findViewById(R.id.tvActorD);
        ratingD = findViewById(R.id.ratingD);

        if (clickMovie.getImgLink() == null) {
            ivPoster.setImageResource(R.mipmap.ic_launcher);
        }
        else {
            Bitmap savedBitmap = imageFileManager.getBitmapFromTemporary(clickMovie.getImgLink());

            if(savedBitmap != null) {
                ivPoster.setImageBitmap(savedBitmap);
            }
            else {
                ivPoster.setImageResource(R.mipmap.ic_launcher_movie);
                new GetImageAsyncTask().execute(clickMovie.getImgLink());
            }
        }

        tvTitle.setText(clickMovie.getTitle());
        tvSubTitle.setText("( " +clickMovie.getSubtitle()+ " )");
        tvPubDate.setText(clickMovie.getPubDate());
        tvDirector.setText(clickMovie.getDirector());
        tvActor.setText(clickMovie.getActor());
        ratingD.setRating(clickMovie.getRating());
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScrap:
                String fileName = imageFileManager.moveFileToExt(clickMovie.getImgLink());
                clickMovie.setImgFileName(fileName);
                clickMovie.setUserRating(0);
                clickMovie.setMemo("");

                Log.d(TAG, "click Scrap: " +clickMovie);
                boolean result = movieDBManager.addScrapMovie(clickMovie);
                if(result) {
                    Toast.makeText(this, "스크랩하였습니다", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this, "스크랩에 실패하였습니다.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnCancelD:
                finish();
                break;
        }
    }

    class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        String imageAddress;

        @Override
        protected Bitmap doInBackground(String... params) {
            imageAddress = params[0];
            Bitmap result = null;
            result = networkManager.downloadImage(imageAddress);

            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (bitmap != null) {
                ivPoster.setImageBitmap(bitmap);
                imageFileManager.saveBitmapToTemporary(bitmap, imageAddress);
            }
        }
    }
}