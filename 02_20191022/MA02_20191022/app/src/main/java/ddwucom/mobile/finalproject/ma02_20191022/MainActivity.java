package ddwucom.mobile.finalproject.ma02_20191022;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearchMovie:
                intent = new Intent(this, SearchMovieActivity.class);
                startActivity(intent);
                break;
            case R.id.btnFindTheater:
                intent = new Intent(this, FindTheaterActivity.class);
                startActivity(intent);
                break;
            case R.id.btnMyPage:
                intent = new Intent(this, MyPageActivity.class);
                startActivity(intent);
                break;
        }

    }

}