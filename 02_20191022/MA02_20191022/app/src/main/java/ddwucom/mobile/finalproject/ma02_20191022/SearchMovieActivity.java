package ddwucom.mobile.finalproject.ma02_20191022;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchMovieActivity extends AppCompatActivity {

    public static final String TAG = "SearchMovieActivity";

    EditText etSearch;
    ListView listView;
    String apiAddress;

    String query;

    ArrayList<MovieDTO> movieList;
    MyMovieAdapter adapter;
    NaverMovieXMLParser parser;
    NaverNetworkManager networkManager;
    ImageFileManager imageFileManager;

    MovieDBManager movieDBManager;

    int idx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        etSearch = findViewById(R.id.etSearch);
        listView = findViewById(R.id.listViewS);

        movieList = new ArrayList<>();
        adapter = new MyMovieAdapter(this, R.layout.listview_movie, movieList);
        listView.setAdapter(adapter);

        parser = new NaverMovieXMLParser();

        apiAddress = getResources().getString(R.string.apiUrl);

        networkManager = new NaverNetworkManager(this);
        networkManager.setClientId(getResources().getString(R.string.clientId));
        networkManager.setClientSecret(getResources().getString(R.string.clientSecret));

        imageFileManager = new ImageFileManager(this);

        movieDBManager = new MovieDBManager(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                MovieDTO movie = adapter.getItem(position);
                Intent intent = new Intent(SearchMovieActivity.this, DetailSearchMovie.class);
                intent.putExtra("clickMovie", movie);
                Log.d(TAG, "filename: " +movie.getImgFileName());
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 임시 파일 삭제
        imageFileManager.clearTemporaryFiles();
    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                query = etSearch.getText().toString();

                try {
                    new NetworkAsyncTask().execute(apiAddress + URLEncoder.encode(query, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    class NetworkAsyncTask extends AsyncTask<String, Integer, String> {

        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(SearchMovieActivity.this, "Wait", "Searching...");
        }

        @Override
        protected String doInBackground(String... strings) {
            String address = strings[0];
            String result = null;

            result = networkManager.downloadContents(address);
            if(result == null) return "error";
            Log.d(TAG, "result: " +result);

            movieList = parser.parse(result);
            Log.d(TAG, "movieList: " +movieList);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            adapter.setList(movieList);    // Adapter 에 결과 List(parsing 결과) 를 설정 후 notify
            progressDlg.dismiss();
        }
    }

}