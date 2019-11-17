package org.techtown.samplerelativelayout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowMyTime extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showmytime);

        //메뉴로 이동하는 인텐트//
        Button button = (Button)findViewById(R.id.gotomenu); /*페이지 전환버튼*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);//액티비티 띄우기
            }
        });

        TextView textview = (TextView) findViewById(R.id.showmytime);

        Intent intent = getIntent();

        long studytime = intent.getExtras().getLong("Key");

        long hour, min, sec;

        sec = studytime;

        if (studytime/3600>0) {
            min=sec/60;
            hour=min/60;
            sec=studytime % 60;

            textview.setText("전달받은 데이터 : " + studytime);
        }

        else if (studytime/60>0) {
            min=sec/60;
            hour=min/60;
            sec=studytime % 60;

            textview.setText("총 " + min + "분 "  + sec + "초 공부했습니다." );
        }

        else  {
            min=sec/60;
            hour=min/60;
            sec=studytime % 60;

            textview.setText("전달받은 데이터 : " + studytime);
        }

    }

}
