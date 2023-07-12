package ddwucom.mobile.finalproject.ma02_20191022;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DetailScrapMovie extends AppCompatActivity {

    public static final String TAG = "DetailScrapMovie";

    MovieDTO scrapMovie;

    ImageView ivPosterM;
    TextView tvtitleM;
    TextView tvPubDateM;
    TextView tvDirectorM;
    TextView tvActorM;
    RatingBar rbRatingM;

    ImageView ivPictureM;
    TextView tvMemo;
    RatingBar rbUserRatingM;

    RatingBar ratingBar;
    EditText etMemo;


    NaverNetworkManager networkManager;
    ImageFileManager imageFileManager;

    MovieDBManager movieDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_scrap_movie);

        scrapMovie = (MovieDTO) getIntent().getSerializableExtra("scrapMovie");

        imageFileManager = new ImageFileManager(this);
        networkManager = new NaverNetworkManager(this);

        movieDBManager = new MovieDBManager(this);

        ivPosterM = findViewById(R.id.ivPosterM);
        tvtitleM = findViewById(R.id.tvTitleM);
        tvPubDateM = findViewById(R.id.tvPubDateM);
        tvDirectorM = findViewById(R.id.tvDirectorM);
        tvActorM = findViewById(R.id.tvActorM);
        rbRatingM = findViewById(R.id.rbRatingM);

        ivPictureM = findViewById(R.id.ivPictureM);
        tvMemo = findViewById(R.id.tvMemo);
        rbUserRatingM = findViewById(R.id.rbUserRatingM);

        if (scrapMovie.getImgLink() == null) {
            ivPosterM.setImageResource(R.mipmap.ic_launcher);
        }
        else {
            Bitmap savedBitmap = imageFileManager.getBitmapFromTemporary(scrapMovie.getImgLink());

            if(savedBitmap != null) {
                ivPosterM.setImageBitmap(savedBitmap);
            }
            else {
                ivPosterM.setImageResource(R.mipmap.ic_launcher_movie);
                new DetailScrapMovie.GetImageAsyncTask().execute(scrapMovie.getImgLink());
            }
        }

        tvtitleM.setText(scrapMovie.getTitle());
        tvPubDateM.setText(scrapMovie.getPubDate());
        tvDirectorM.setText(scrapMovie.getDirector());
        tvActorM.setText(scrapMovie.getActor());
        rbRatingM.setRating(scrapMovie.getRating());

        tvMemo.setText(scrapMovie.getMemo());
        rbUserRatingM.setRating(scrapMovie.getUserRating());
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTwitter:
                String strLink = null;
                String msg = "영화 <" +scrapMovie.getTitle()+ "> 관람후기\n" +
                        "별점: " +scrapMovie.getUserRating()+ "\n" +
                        "후기: " +scrapMovie.getMemo();

                try {
                    strLink = String.format("http://twitter.com/intent/tweet?text=%s",
                            URLEncoder.encode(msg, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strLink)) ;
                startActivity(intent);

                break;
            case R.id.btnUpdate:
                final ConstraintLayout updateMemo = (ConstraintLayout) View.inflate(this, R.layout.update_memo_dialog_layout, null);

                ratingBar = updateMemo.findViewById(R.id.ratingBar);
                etMemo = updateMemo.findViewById(R.id.etMemo);

                etMemo.setText(scrapMovie.getMemo());
                Log.d(TAG, "in dialog: " +scrapMovie.getMemo());

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("감상평 수정")
                        .setView(updateMemo)
                        .setPositiveButton("수정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String memo = etMemo.getText().toString();
                                float starRating = ratingBar.getRating();
                                Log.d(TAG, "in dialog, after update memo: " +memo);

                                scrapMovie.setUserRating(starRating);
                                scrapMovie.setMemo(memo);

                                boolean result = movieDBManager.updateMovie(scrapMovie);
                                Log.d(TAG, "in dialog, after update: " +scrapMovie.getMemo());
                                tvMemo.setText(scrapMovie.getMemo());
                                rbUserRatingM.setRating(scrapMovie.getUserRating());

                                if (result) {
                                    Toast.makeText(DetailScrapMovie.this, "수정 성공하였습니다", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(DetailScrapMovie.this, "수정 실패하였습니다", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();

                break;
            case R.id.btnCancelM:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
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
                ivPosterM.setImageBitmap(bitmap);
                imageFileManager.saveBitmapToTemporary(bitmap, imageAddress);
            }
        }
    }
}