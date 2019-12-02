package org.techtown.samplerelativelayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CountdownTimer extends AppCompatActivity {

    TextView clockTextView ;

    //공부시간
    int time;
    int firsttime;
    int tmptime;

    //바깥에서의 시간
    private int outcount=0;

    //휴식시간
    int resttime ;
    int firstresttime;
    int tmpresttime;
    int wsay = 0;

    //세트시간
    int setcount ;
    int maincount ;
    int tmpsetcount;
    String Activitycondition;

    //프로그래스바 전용 카운트
    Integer count =1;
    //프로그래스바 생성
    private ProgressBar progressBar;
    //프로그래스 바 객체 생성
    ProgressbarTask Progress;

    //누적시킬 집중시간
    int totalstudy = 0;

    //메인 시간 쓰레드
    TimerRunnable nr = new TimerRunnable() ;

    //실행<->포기 버튼
    Button start;

    //메인화면에 텍스트 뷰로 타이머 세팅 값 표시하기
    TextView timetext;
    TextView resttimetext;
    TextView setcounttext;

    //넘버피커 지정하기
    NumberPicker picker1;
    NumberPicker picker2;
    NumberPicker picker3;

    private static Handler mHandler ;
    private static Handler restHandler ;
    private static Handler WiseSayHandler;
    private static Handler countHandler;
    private static Handler finishHandler;
    private static Handler totalHandler;


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdowntimer);

        Activitycondition="onCreate";


        //메인화면에 텍스트 뷰로 타이머 세팅 값 표시하기
        timetext = (TextView)findViewById(R.id.time);
        resttimetext = (TextView)findViewById(R.id.resttime);
        setcounttext = (TextView)findViewById(R.id.setcount);



        //생명주기 상태를 실시간으로 점검하는 쓰레드를 실행
        final Runnable Activitycheck = new ActivityCheckRunnable() ;
        final Thread AC = new Thread(Activitycheck) ;
        AC.start() ;


        //명언 쓰레드는 공부가 시작될 당시만 띄우도록 하자!
        final TextView wisesayTextView;
        wisesayTextView = findViewById(R.id.wisesaying);
        wisesayTextView.setVisibility(View.INVISIBLE);



        //<핸들러 정리하기>//
        //공부 타이머 세팅 핸들러//
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //시 분 초 로 포멧 변경 처리하기
                int min = time / 60;
                int sec = time % 60;
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", min, sec);

                clockTextView = findViewById(R.id.clock) ;
                //초시계 전용 세팅(시연용)
                //clockTextView.setText(String.valueOf(time)) ;

                //분 초 전용 세팅
                clockTextView.setText(String.valueOf(timeLeftFormatted)) ;

                clockTextView.setTextColor(Color.BLACK);
            }
        } ;

        //휴식시간 세팅 핸들러
        restHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //시 분 초 로 포멧 변경 처리하기
                int min = resttime / 60;
                int sec = resttime % 60;
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", min, sec);

                clockTextView = findViewById(R.id.clock) ;

                //초시계 전용 세팅(시연용)
                //clockTextView.setText(String.valueOf(resttime));//쉬는시간이 반영된다.

                //분 초 전용 세팅
                clockTextView.setText(String.valueOf(timeLeftFormatted)) ;

                clockTextView.setTextColor(Color.GRAY);
            }
        } ;

        //카운트 횟수 핸들러
        countHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                clockTextView = findViewById(R.id.count) ;
                clockTextView.setText(String.valueOf(maincount-setcount+1) + "세트 째 공부중입니다.") ; //지정한 세트수가 반영된다.
            }
        } ;

        //완료 텍스트 반영 핸들러
        finishHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                clockTextView = findViewById(R.id.clock) ;
                clockTextView.setText("공부 완료") ; //지정한 세트수가 반영된다.
                //완료가 되어진 시점에서 버튼 모양 변경

                wisesayTextView.setVisibility(View.INVISIBLE);//완료가 되어진 시점에서 명언 쓰레드 안보이기

                Button timerset = (Button)findViewById(R.id.btnTimeset);
                timerset.setVisibility(View.VISIBLE); //완료시에는 타이머 세팅 버튼이 나타나지도록 처리//



                start.setText("시작");
            }
        } ;

        //총 공부시간 수정 핸들러
        totalHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                clockTextView = findViewById(R.id.totalstudy) ;
                clockTextView.setText(totalstudy + " 공부 했습니다.") ; //지정한 세트수가 반영된다.
            }
        } ;


        //명언을 바꿔서 적용시켜주는 핸들러//
        WiseSayHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) { //해당 메시지를 메인쓰레드에 보내서 처리해주는 거다.

                List<String> list = new ArrayList<>();
                list.add("꾸준함은 기본이지.");
                list.add("어떻게 하면 \"잘\" 할 수 잇을까?");
                list.add("30분마다 잘 기록하고 있어?");
                list.add("멍때리고 있는거 아니지?");
                list.add("너도 니 친구들만큼 열심히 살아야지.");
                list.add("멘탈 꽉 붙들어 잡아.");

                TextView wisesayTextView;
                wisesayTextView = findViewById(R.id.wisesaying) ; // 뷰에서 작업 처리를 해당 메서드 안에서 해준다.
                if(wsay<list.size()) {
                    wisesayTextView.setText(list.get(wsay)); // 명언세팅하기
                    wsay++;
                    Log.e(this.getClass().getName(), "현재 wsay값 : " + wsay);
                }
                else{
                    wsay=0;//초기화 : 처음부터 다시 명언리스트를 하나씩 출력하여 텍스트뷰에 반영시킴.
                    wisesayTextView.setText(list.get(wsay));
                    wsay++;
                }
            }
        };

        //명언 핸들러를 호출해서 사용하기//
        final Runnable Wsaying = new WsayRunnable();
        final Thread WS = new Thread(Wsaying);
        WS.start();


        //실제 다이얼로그 세팅 하기
        Button timerset = (Button)findViewById(R.id.btnTimeset);
        timerset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(CountdownTimer.this);
                dialog.setContentView(R.layout.timersetdialog);

                //공부시간
                picker1 = (NumberPicker)dialog.findViewById(R.id.picker1);
                //휴식시간
                picker2 = (NumberPicker)dialog.findViewById(R.id.picker2);
                //총 횟수
                picker3 = (NumberPicker)dialog.findViewById(R.id.picker3);

                //공부시간 최소 최대 값 설정
                picker1.setMinValue(10);
                picker1.setMaxValue(180);
                picker1.setValue(10);
                picker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        //타임피커로 공부시간 값 세팅(임시)
                        tmptime=newVal*60;
                        time = newVal*60;
                        picker1.setValue(newVal);
                    }
                });
                picker1.setWrapSelectorWheel(false);

                //휴식시간 최소 최대 값 설정
                picker2.setMinValue(5);
                picker2.setMaxValue(15);
                picker2.setValue(5);
                picker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        //타임피커로 휴식시간 값 세팅(임시)
                        tmpresttime=newVal*60;
                        resttime = newVal*60;
                        picker2.setValue(newVal);
                    }
                });
                picker2.setWrapSelectorWheel(false);

                //세트수 최소 최대 설정
                picker3.setMinValue(1);
                picker3.setMaxValue(10);
                picker3.setValue(1);
                picker3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        //타임피커로 세트 값 세팅(임시)
                        tmpsetcount=newVal;
                        setcount=newVal;
                        picker3.setValue(newVal);
                        maincount=setcount;
                    }
                });
                picker3.setWrapSelectorWheel(false);


                //세팅완료 버튼
                Button Setfinish = (Button) dialog.findViewById(R.id.setfinish);
                Setfinish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //메인화면에 텍스트 뷰로 타이머 세팅 값 표시하기
                        timetext = (TextView)findViewById(R.id.time);
                        resttimetext = (TextView)findViewById(R.id.resttime);
                        setcounttext = (TextView)findViewById(R.id.setcount);

                        //해당 값을 세팅하기
                        timetext.setText("세팅된 공부시간 " + picker1.getValue() + "분");
                        resttimetext.setText("세팅된 휴식시간 "+picker2.getValue() + "분");
                        setcounttext.setText("세팅된 횟수 "+picker3.getValue()+"번");

                        //실제 변수에 넘버피커 값을 세팅하기
                        time=picker1.getValue()*60;
                        tmptime=picker1.getValue()*60;

                        resttime=picker2.getValue()*60;
                        tmpresttime=picker2.getValue()*60;


                        //시연용 (초단위)
                        time=picker1.getValue();
                        tmptime=picker1.getValue();

                        resttime=picker2.getValue();
                        tmpresttime=picker2.getValue();


                        setcount=picker3.getValue();
                        tmpsetcount=picker3.getValue();

                        maincount=setcount;

                        dialog.dismiss();
                    }
                });


                dialog.show();

            }
        });



        //세팅 -> 실행 수행
        start = (Button)findViewById(R.id.btnStart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(start.getText().equals("시작")) {

                    wisesayTextView.setVisibility(View.VISIBLE);

                    Button timerset = (Button)findViewById(R.id.btnTimeset);
                    timerset.setVisibility(View.INVISIBLE); //실행시에는 타이머 세팅 버튼이 사라지도록 처리//

                    //지정한 공부시간, 그리고 지정한 휴식시간 값을 세팅해줌
                    nr=new TimerRunnable() ;
                    firsttime = time;
                    firstresttime = resttime;

                    //프로그래스바 테스트//
                    progressBar = (ProgressBar) findViewById(R.id.progressBar); // 사용할 프로그레스바를 지정
                    progressBar.setMax(time); // 최대치 값은 100 (내부에서는 time 값에 맞춰서 변경이 필요하다)
                    progressBar.setProgress(0);
                    Progress=new ProgressbarTask();



                    Thread count = new Thread(nr);
                    count.start();
                    start.setText("포기하기");
                }
                //쓰레드 종료 처리 및 초기화 기능 처리
                else{
                    wisesayTextView.setVisibility(View.INVISIBLE);

                    Button timerset = (Button)findViewById(R.id.btnTimeset);
                    timerset.setVisibility(View.VISIBLE); //실행시에는 타이머 세팅 버튼이 사라지도록 처리//

                    nr.mFinished=true; //쓰레드 죽이기//
                    nr.mPaused=true; //쓰레드 멈춤상태로 지정//

                    Progress.onProgressUpdate(0);//프로그래스 초기화 시키기//
                    clockTextView = findViewById(R.id.clock) ;
                    clockTextView.setText("00:00") ; //지정한 세트수가 반영된다.
                    time=tmptime;
                    resttime=tmpresttime;
                    setcount=tmpsetcount;

                    start.setText("시작");
                }
            }
        });
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////
    class TimerRunnable implements Runnable {
        private Object mPauseLock;
        private boolean mPaused;
        private boolean mFinished;

        public TimerRunnable() {
            mPauseLock = new Object();
            mPaused = false;
            mFinished = false;
        }

        @Override
        public void run() {
            while (!mFinished) {
                if (time > 0) {
                    Log.e(this.getClass().getName(), "[time 값] : " + String.valueOf(time));
                    countHandler.sendEmptyMessage(0);
                    mHandler.sendEmptyMessage(0); // time값을 텍스트에 반영
                    totalstudy++;
                    totalHandler.sendEmptyMessage(0);
                    //프로그래스바에 time정보를 반영한다.
                    Progress.onProgressUpdate(firsttime-time+1);

                    time--;
                } else if (resttime > 0) {//해당부분에서 쓰레드의 시간이 바뀜으로써 "다음단계가" 진행 되여야 한다.
                    Log.e(this.getClass().getName(), "[resttime 값] : " + resttime + "[firsrestttime 값] : " + firstresttime);
                    if (resttime == firstresttime) { //현재시각의 값을 초기시간의 값과 비교하기.
                        dialoghandler.sendEmptyMessage(0); // 쉬는 시각에서 다이얼로그를 띄워준다.
                    }
                    restHandler.sendEmptyMessage(0); // restime값을 텍스트에 반영
                    resttime--;
                } else {
                    mHandler.sendEmptyMessage(0); // time값을 텍스트에 반영
                    //restHandler.sendEmptyMessage(0); // restime값을 텍스트에 반영

                    setcount--;
                    if (setcount == 0) {
                        finishHandler.sendEmptyMessage(0); // time값을 텍스트에 반영

                        //기존에 세팅되어져 있던 시간과 세트 값들을 유지 함
                        time=tmptime;
                        resttime=tmpresttime;
                        setcount=tmpsetcount;
                        break;
                    }
                    //한 사이클이 끝나면, 값을 세팅해주고, 초기화 시킵니다.
                    time = firsttime;
                    resttime = firstresttime;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ///////쓰레드를 제어하는 구문////////
                synchronized (mPauseLock) {
                    while (mPaused) {
                        try {
                            mPauseLock.wait(1000);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        }
        /**
         * Call this on pause.
         */
        public void onPause() {
            synchronized (mPauseLock) {
                mPaused = true;
            }
        }

        /**
         * Call this on resume.
         */
        public void onResume() {
            synchronized (mPauseLock) {
                mPaused = false;
                mPauseLock.notifyAll();
            }
        }
    }




    class WsayRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
//                if(time==0 && resttime==0){
//                    Log.e(this.getClass().getName(), "Wsay 쓰레드가 완료 되었습니다.");
//                    break;
//                }

                if(time>-2) {
                    //mHandler.sendEmptyMessage(0);
                    //Log.e(this.getClass().getName(), "time 값 : " + String.valueOf(time));
                    WiseSayHandler.sendEmptyMessage(0);
                    //time--;
                }
                else{
                    //Log.e(this.getClass().getName(), "Wsay 쓰레드가 완료 되었습니다.");
                    break;
                }

                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace() ;
                }
            }
        }
    }

    //안드로이드 체크 쓰레드// 해당쓰레드를 통해서 실행 재실행 처리를 먼저 해보자.
    class ActivityCheckRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                if(Activitycondition.equals("onCreate")){
                    //Log.e(this.getClass().getName(), "현재 Oncreate 상태입니다.");
                }

                else if(Activitycondition.equals("onPause")) {
                    //Log.e(this.getClass().getName(), "현재 Onpause 상태입니다.");
                    outcount++;
                    if(outcount>10 && nr.mPaused==false){
                        //바깥 상태로 10초 경과가 있는 경우//
                        nr.mPaused=true;
                        createNotification();
                    }
                    //내 타이머 쓰레드가 멈추지 않은 상태에서 Onstop상태로 20초가 경과 하였을 때 처리.
                    if(outcount>20 && nr.mFinished==false){
                        outcount=0;
                        //바깥 상태로 20초 경과가 있는 경우 타이머를 초기화 시킴//
                        TextView wisesayTextView = findViewById(R.id.wisesaying);
                        wisesayTextView.setVisibility(View.INVISIBLE);

                        Button timerset = (Button)findViewById(R.id.btnTimeset);
                        timerset.setVisibility(View.VISIBLE); //실행시에는 타이머 세팅 버튼이 사라지도록 처리//

                        nr.mFinished=true; //쓰레드 죽이기//
                        nr.mPaused=true; //쓰레드 멈춤상태로 지정//

                        Progress.onProgressUpdate(0);//프로그래스 초기화 시키기//
                        clockTextView = findViewById(R.id.clock) ;
                        clockTextView.setText("00:00") ; //지정한 세트수가 반영된다.
                        time=tmptime;
                        resttime=tmpresttime;
                        setcount=tmpsetcount;

                        //start.setText("시작");
                        createNotification2();
                    }
                }

                else if(Activitycondition.equals("onStart")) {
                    //Log.e(this.getClass().getName(), "현재 Onstart 상태입니다.");
                }

                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace() ;
                }
            }
        }
    }



    class ProgressbarTask extends AsyncTask<Integer, Integer, String> {

        //백그라운드에서 실행되어지는 부분
        @Override
        protected String doInBackground(Integer... params) {
            Log.e(this.getClass().getName(), String.valueOf(params[0]));
            for (; count <= params[0]; count++) {
                Log.e(this.getClass().getName(), "params[0]:"+String.valueOf(params[0]));
                Log.e(this.getClass().getName(), "count:"+String.valueOf(count));
                try {
                    publishProgress(count);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Task Completed.";
        }
        @Override
        //해당작업이 완료 되었을 때
        protected void onPostExecute(String result) {
            //progressBar.setVisibility(View.GONE);
//            txt.setText(result);
//            btn.setText("Restart");
        }
        @Override
        // Task가 실행전에
        protected void onPreExecute() {
//            txt.setText("Task Starting...");
        }
        @Override
        // Task가 업데이트 중일때.
        protected void onProgressUpdate(Integer... values) {
//            txt.setText("Running..."+ values[0]);
            progressBar.setProgress(values[0]);
            //Log.e(this.getClass().getName(), String.valueOf(values[0]));
        }
    }



    @SuppressLint("HandlerLeak")
    public Handler dialoghandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==0){
                final EditText et = new EditText(getApplicationContext());
                new AlertDialog.Builder(CountdownTimer.this).setMessage(maincount-setcount+1+"세트 : 어떤 공부를 하셨나요?").setView(et)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        }
    };


    ////////생명주기 처리해서 문자열 담기////////
    protected void onPause(){
        super.onPause();
        //nr.onPause();

        Activitycondition="onPause";
    }

    protected void onStart(){
        super.onStart();
        nr.onResume();
        outcount = 0;

        Activitycondition="onStart";
    }

    protected void onStop(){
        super.onStop();
        //nr.onPause();
        //Activitycondition="onStop";
    }

    ///notification 구현해 보기//
    private void createNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("딴짓 하지마세요");
        builder.setContentText("10초 내로 화면으로 돌아가지 않으면 타이머가 초기화 되버립니다.");

        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(1, builder.build());
    }

    ///notification 구현해 보기//
    private void createNotification2() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("타이머가 초기화 되었습니다. 집중하셔야죠!");

        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(1, builder.build());
    }

    private void removeNotification() {


        // Notification 제거
        NotificationManagerCompat.from(this).cancel(1);
    }
}

