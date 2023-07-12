package ddwucom.mobile.finalproject.ma02_20191022;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyPageActivity extends AppCompatActivity {

    public static final String TAG = "MyPageActivity";

    EditText etSearchM;
    ListView listViewM;

    ArrayList<MovieDTO> movieList;
    MyPageAdapter adapter;

    MovieDBManager movieDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        listViewM = findViewById(R.id.listViewM);

        etSearchM = findViewById(R.id.etSearchM);

        movieDBManager = new MovieDBManager(this);
        movieList = new ArrayList();

        Log.d(TAG, "in MyPageActivity");
        adapter = new MyPageAdapter(this, R.layout.listview_movie, movieList);
        Log.d(TAG, "set adapter");
        listViewM.setAdapter(adapter);


        listViewM.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MyPageActivity.this, DetailScrapMovie.class);
                intent.putExtra("scrapMovie", movieList.get(position));
                startActivity(intent);
            }
        });

        listViewM.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                final int now = position;

                AlertDialog.Builder builder = new AlertDialog.Builder(MyPageActivity.this);

                builder.setTitle("< " +movieList.get(now).getTitle()+ " > 을(를) 삭제하시겠습니까?")
                        .setMessage("삭제한 영화는 복구할 수 없습니다.")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean result = movieDBManager.removeScrapMovie(movieList.get(now).get_id());

                                if(result) {
                                    Toast.makeText(MyPageActivity.this, "삭제하였습니다", Toast.LENGTH_LONG).show();

                                    movieList.clear();
                                    movieList.addAll(movieDBManager.getAllMovie());
                                    adapter.notifyDataSetChanged();
                                }
                                else {
                                    Toast.makeText(MyPageActivity.this, "삭제 실패하였습니다", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();
                return true;
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();

        movieList.clear();
        movieList.addAll(movieDBManager.getAllMovie());
        adapter.notifyDataSetChanged();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearchM:
                String title = etSearchM.getText().toString();

                movieList.clear();
                movieList.addAll(movieDBManager.searchScrapMovie(title));

                if(movieDBManager.searchScrapMovie(title).size() == 0) {
                    Toast.makeText(this, "검색 결과가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();

                break;
        }
    }
}