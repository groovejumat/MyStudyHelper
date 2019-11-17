package org.techtown.samplerelativelayout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.Serializable;

public class Soundselect extends AppCompatActivity {


    // MediaPlayer 객체생성
    MediaPlayer mediaPlayer;
    // 시작버튼
    Button startButton;
    //종료버튼
    Button stopButton;

    private static final String TAG = "Soundselect";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundselect);


        //첫번째 음악과 버튼 처리
        startButton = findViewById(R.id.forrestbt);
        stopButton = findViewById(R.id.forrestbtoff);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // MediaPlayer 객체 할당
                //현재 음악파일 객체를 담는 변수가 null이 아니라면, 해당음악을 stop 시키고 다시 실행한다.
                if(mediaPlayer!=null) {
                    mediaPlayer.stop();
                    mediaPlayer = MediaPlayer.create(Soundselect.this, R.raw.asgard);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }
                else {
                    mediaPlayer = MediaPlayer.create(Soundselect.this, R.raw.asgard);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 정지버튼
                mediaPlayer.stop();
                // 초기화
                mediaPlayer.reset();
            }
        });
        //첫번째 음악 이벤트 처리 끝

        //두번째 음악과 버튼 처리
        startButton = findViewById(R.id.coffeeshopbt);
        stopButton = findViewById(R.id.coffeeshopbtoff);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(getApplicationContext(), MusicService.class));
                String musicname="coffeeshop";
                Toast.makeText(Soundselect.this, "클릭시 명시적 인텐트 동작 시작", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplication(), MusicService.class);
                intent.putExtra("Key",musicname);
                //startActivity(intent);
                startService(intent);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 정지버튼
                stopService(new Intent(getApplicationContext(), MusicService.class));
            }
        });
        //두번째 음악 이벤트 처리 끝

        //세번째 음악을 쓰레드 서비스로 처리한다면.........
        startButton = findViewById(R.id.rainbt);
        stopButton = findViewById(R.id.rainbtoff);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(getApplicationContext(), MusicService.class));
                String musicname="rain";
                Toast.makeText(Soundselect.this, "클릭시 명시적 인텐트 동작 시작", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplication(), MusicService.class);
                intent.putExtra("Key",musicname);
                //startActivity(intent);
                startService(intent);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(getApplicationContext(), MusicService.class));
            }
        });
        //세번째 음악 이벤트 처리 끝


        //네번째 음악을 쓰레드 서비스로 처리한다면.........
        startButton = findViewById(R.id.windbt);
        stopButton = findViewById(R.id.windbtoff);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(getApplicationContext(), MusicService.class));
                String musicname="wind";
                Toast.makeText(Soundselect.this, "클릭시 명시적 인텐트 동작 시작", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplication(), MusicService.class);
                intent.putExtra("Key",musicname);
                //startActivity(intent);
                startService(intent);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(getApplicationContext(), MusicService.class));
            }
        });
        //네번째 음악 이벤트 처리 끝


        //다섯번째 음악을 쓰레드 서비스로 처리한다면.........
        startButton = findViewById(R.id.bonfire);
        stopButton = findViewById(R.id.bonefireoff);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(getApplicationContext(), MusicService.class));
                String musicname="fire";
                Toast.makeText(Soundselect.this, "클릭시 명시적 인텐트 동작 시작", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplication(), MusicService.class);
                intent.putExtra("Key",musicname);
                //startActivity(intent);
                startService(intent);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(getApplicationContext(), MusicService.class));
            }
        });
        //다섯번째 음악 이벤트 처리 끝

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putSerializable("mediaplyer", (Serializable) mediaPlayer);
    }
}
