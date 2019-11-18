package org.techtown.samplerelativelayout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //백색소음선택창으로 이동하는 인텐트//
        Button button = (Button)findViewById(R.id.whitenoisebt); /*페이지 전환버튼*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Soundselect.class);
                startActivity(intent);//액티비티 띄우기
            }

        });

        //타이머로 이동하는 인텐트//
        Button button2 = (Button)findViewById(R.id.concentratebt); /*페이지 전환버튼*/
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Pomodoro.class);
                startActivity(intent);//액티비티 띄우기
            }

        });

        //타이머로 이동하는 인텐트//
        Button button3 = (Button)findViewById(R.id.memo); /*페이지 전환버튼*/
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MemomainActivity.class);
                startActivity(intent);//액티비티 띄우기
            }
        });

        //포스팅으로 이동하는 인텐트//
        Button button4 = (Button)findViewById(R.id.post); /*페이지 전환버튼*/
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PostmainActivity.class);
                startActivity(intent);//액티비티 띄우기
            }
        });

        //포스팅으로 이동하는 인텐트//
        Button button5 = (Button)findViewById(R.id.signup); /*페이지 전환버튼*/
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);//액티비티 띄우기
            }
        });

//        //인텐트 데이터 받아오는 버튼//
//        Button button5 = (Button)findViewById(R.id.signup); /*페이지 전환버튼*/
//        button5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //공부한 통합 시간 알아보기//
//                TextView textview = (TextView) findViewById(R.id.studychecker);
//                Intent intent = getIntent();
//                textview.setText("전달받은 데이터 : "+intent.getExtras().getInt("Key"));
//            }

//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
