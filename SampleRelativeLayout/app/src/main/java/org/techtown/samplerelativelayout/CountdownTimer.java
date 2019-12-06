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
import android.media.AudioManager;
import android.media.SoundPool;
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
import android.widget.Toast;

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

    //명언쓰레드
    Thread WS;

    //해당 명언 시작 값 및 배열 설정
    Integer Wsay =1;
    List<String> wsaylist = new ArrayList<>();
    //WsayTask Wsaying= new WsayTask();//.execute(wsaylist.size());
    WsayRunnable Wsaying = new WsayRunnable();

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

    //내 쓰레드 타이머가 실행시 휴식상태여부를 판별하기 위한 쓰레드
    boolean Onrest = false;

    private static Handler mHandler ;
    private static Handler restHandler ;
    private static Handler WiseSayHandler;
    private static Handler countHandler;
    private static Handler finishHandler;
    private static Handler totalHandler;
    private static Handler countrestHandler;


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdowntimer);

        Activitycondition="onCreate";
        outcount = 0;

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(0); //프로그래스의 값을 초기화 시킴

        //메인화면에 텍스트 뷰로 타이머 세팅 값 표시하기
        timetext = (TextView)findViewById(R.id.time);
        resttimetext = (TextView)findViewById(R.id.resttime);
        setcounttext = (TextView)findViewById(R.id.setcount);

        //중단 <-> 재실행 버튼지정(해당 버튼은 타이머가 진행시에만 생성.)//
        Button PauseResume = (Button) findViewById(R.id.PauseResume);
        PauseResume.setVisibility(View.INVISIBLE);

        //응원 글에 넣을 배열 요소 추가.
        wsaylist.add("성공을 확신하는 것은 \n 성공의 첫걸음이다.");
        wsaylist.add("10분 뒤와 10년 후를 동시에 생각하라.");
        wsaylist.add("고뇌에 지는 것은 수치가 아니다.\n쾌락에 지는 것이야 말로 수치다.");
        wsaylist.add("늦게 시작하는 것을 두려워 말고,\n 하다 중단하는 것을 두려워 하라.");
        wsaylist.add("많이 보고 많이 겪고 많이 공부하는 것은\n 배움의 세 기둥이다.");
        wsaylist.add("공부벌레들에게 잘 해 주십시오.\n 나중에 그 사람 밑에서 일하게 될 수도 있습니다.");
        //Wsaying.execute(wsaylist.size());

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
                clockTextView.setText(String.valueOf(maincount-setcount+1) + "세트 공부") ; //지정한 세트수가 반영된다.
            }
        } ;

        //카운트 횟수 핸들러
        countrestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                clockTextView = findViewById(R.id.count) ;
                clockTextView.setText(String.valueOf(maincount-setcount+1) + "세트 휴식") ; //지정한 세트수가 반영된다.
            }
        } ;

        //완료 텍스트 반영 핸들러
        finishHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                clockTextView = findViewById(R.id.clock) ;
                clockTextView.setText("집중 완료") ; //지정한 세트수가 반영된다.
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
                list.add("성공을 확신하는 것은 \n 성공의 첫걸음이다.");
                list.add("10분 뒤와 10년 후를 동시에 생각하라.");
                list.add("고뇌에 지는 것은 수치가 아니다.\n쾌락에 지는 것이야 말로 수치다.");
                list.add("늦게 시작하는 것을 두려워 말고,\n 하다 중단하는 것을 두려워 하라.");
                list.add("많이 보고 많이 겪고 많이 공부하는 것은\n 배움의 세 기둥이다.");
                list.add("눈부신 성취는 언제나\n 특별하지 않은 준비뒤에 오게 된다.");
                list.add("배우나 생각하지 않으면 공허하고,\n 생각하나 배우지 않으면 위험하다.");
                list.add("가장 유능한 사람은\n 가장 배움에 힘쓰는 사람이다.");

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
//        final Runnable Wsaying = new WsayRunnable();
//        final Thread WS = new Thread(Wsaying);
//        WS.start();



        //실제 다이얼로그 세팅 하기
        Button timerset = (Button)findViewById(R.id.btnTimeset);
        timerset.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(CountdownTimer.this);
                dialog.setContentView(R.layout.timersetdialog);

                //공부시간
                picker1 = (NumberPicker)dialog.findViewById(R.id.picker1);
                final String[] Values = new String[36];

                //넘버피커를 5단위로 지정하기 유저가 세팅하기 편하게
                for (int i = 0; i < Values.length; i++) {
                    String number = Integer.toString(i*5+10);
                    Values[i] = number.length() < 2 ?  number : number;
                    Log.e(this.getClass().getName(), "현재 넘버피커의 값 : " + Values[i]);
                }
                Values[0]="1";



                //휴식시간
                picker2 = (NumberPicker)dialog.findViewById(R.id.picker2);
                //총 횟수
                picker3 = (NumberPicker)dialog.findViewById(R.id.picker3);

                //공부시간 최소 최대 값 설정
                picker1.setMinValue(0);
                picker1.setMaxValue(34);
                picker1.setValue(0);
                picker1.setDisplayedValues(Values);
                picker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        //타임피커로 공부시간 값 세팅(임시)
//                        tmptime=newVal*60;
//                        time = newVal*60;
                        picker1.setValue(newVal);
                    }
                });
                //picker1.setWrapSelectorWheel(false);

                //휴식시간 최소 최대 값 설정
                picker2.setMinValue(1);
                picker2.setMaxValue(15);
                picker2.setValue(1);
                picker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        //타임피커로 휴식시간 값 세팅(임시)
                        tmpresttime=newVal*60;
                        resttime = newVal*60;
                        picker2.setValue(newVal);
                    }
                });
                //picker2.setWrapSelectorWheel(false);

                //세트수 최소 최대 설정
                picker3.setMinValue(1);
                picker3.setMaxValue(5);
                picker3.setValue(2);
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
                //picker3.setWrapSelectorWheel(false);


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
                        timetext.setText("세팅된 공부시간 " + Integer.parseInt(Values[picker1.getValue()]) + "분");
                        resttimetext.setText("세팅된 휴식시간 "+picker2.getValue() + "분");
                        setcounttext.setText("세팅된 횟수 "+picker3.getValue()+"번");

                        //실제 변수에 넘버피커 값을 세팅하기
                        time=Integer.parseInt(Values[picker1.getValue()]);
                        time=Integer.parseInt(Values[picker1.getValue()])*60;
                        tmptime=Integer.parseInt(Values[picker1.getValue()])*60;

//                        time=picker1.getValue()*60;
//                        tmptime=picker1.getValue()*60;

                        resttime=picker2.getValue()*60;
                        tmpresttime=picker2.getValue()*60;


                        //시연용 (초단위)
//                        time=(picker1.getValue());
//                        time=Integer.parseInt(Values[picker1.getValue()]);
//                        tmptime=Integer.parseInt(Values[picker1.getValue()]);
//
//                        resttime=picker2.getValue();
//                        tmpresttime=picker2.getValue();

                        setcount=picker3.getValue();
                        tmpsetcount=picker3.getValue();

                        maincount=setcount;

                        //time값을 텍스트에 반영하기//
                        mHandler.sendEmptyMessage(0); // time값을 텍스트에 반영

                        progressBar = (ProgressBar) findViewById(R.id.progressBar);
                        progressBar.setProgress(0); //프로그래스의 값을 초기화 시킴

                        Toast.makeText(getApplicationContext(), "타이머 세팅이 완료되어 졌습니다.", Toast.LENGTH_LONG).show();

                        dialog.dismiss();
                    }
                });


                dialog.show();

            }
        });


        //해당 기본 값이 true 상태로 세팅
        nr.mFinished=true;

        //명언 쓰레드 생성//
        //Wsaying = new WsayRunnable();
        WS = new Thread(Wsaying);

        //세팅 -> 실행 수행
        start = (Button)findViewById(R.id.btnStart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //명언 핸들러를 호출해서 사용하기//

                if(WS.isAlive()==false){
                    WS.start();
                }

                if(tmptime==0){
                    Toast.makeText(getApplicationContext(), "현재 타이머가 세팅되어져 있지 않습니다.", Toast.LENGTH_LONG).show();
                }
                else if(start.getText().equals("시작")) {

                    //중단 <-> 재실행 버튼지정(해당 버튼은 타이머가 진행시에만 생성.)//
                    final Button PauseResume = (Button) findViewById(R.id.PauseResume);

                    //카운트 상태 나타내는 텍스트 표시
                    clockTextView = findViewById(R.id.count) ;
                    clockTextView.setVisibility(View.VISIBLE);

                    PauseResume.setVisibility(View.VISIBLE);
                    PauseResume.setText("중지");
                    PauseResume.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(PauseResume.getText().equals("중지")) {
                                Toast.makeText(getApplicationContext(), "타이머가 중지 되었습니다.", Toast.LENGTH_LONG).show();
                                PauseResume.setText("재시작");
                                nr.onPause();
                            }
                            else{
                                PauseResume.setText("중지");
                                Toast.makeText(getApplicationContext(), "타이머가 재시작 되었습니다.", Toast.LENGTH_LONG).show();
                                nr.onResume();
                            }
                        }
                    });
                    //중단 <-> 재실행 부분 끝//
                    wisesayTextView.setVisibility(View.VISIBLE);

                    //세트 정보 보이기//
                    clockTextView.setVisibility(View.VISIBLE);

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
                    nr.onPause();

                    //포기하는 것을 재확인 하는 다이얼로그를 띄운다.
                    AlertDialog.Builder builder = new AlertDialog.Builder(CountdownTimer.this);

                    builder.setTitle("집중을 포기하실껀가요?");


                    builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            nr.onResume();

                        }
                    });

                    builder.setNegativeButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //카운트 상태 나타내는 텍스트 표시
                            clockTextView = findViewById(R.id.count) ;
                            clockTextView.setVisibility(View.INVISIBLE);

                            wisesayTextView.setVisibility(View.INVISIBLE);

                            Button timerset = (Button)findViewById(R.id.btnTimeset);
                            timerset.setVisibility(View.VISIBLE); //실행시에는 타이머 세팅 버튼이 사라지도록 처리//

                            //중단 <-> 재실행 버튼지정(해당 버튼은 타이머가 진행시에만 생성.)//
                            Button PauseResume = (Button) findViewById(R.id.PauseResume);
                            PauseResume.setVisibility(View.INVISIBLE);


                            nr.mFinished=true; //쓰레드 죽이기//
                            nr.mPaused=true; //쓰레드 멈춤상태로 지정//

                            Progress.onProgressUpdate(0);//프로그래스 초기화 시키기//
                            clockTextView = findViewById(R.id.clock) ;

                            time=tmptime;//time값을 텍스트에 반영하기//
                            mHandler.sendEmptyMessage(0); // 초기의 time값을 텍스트에 반영

                            resttime=tmpresttime;
                            setcount=tmpsetcount;

                            start.setText("시작");


                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

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
                    Onrest=false; // 공부시간 시작시에는 휴식 상태가 false로 바뀐다.
                    Log.e(this.getClass().getName(), "[time 값] : " + String.valueOf(time));
                    countHandler.sendEmptyMessage(0);
                    mHandler.sendEmptyMessage(0); // time값을 텍스트에 반영
                    totalstudy++;
                    totalHandler.sendEmptyMessage(0);
                    //프로그래스바에 time정보를 반영한다.
                    Progress.onProgressUpdate(firsttime-time+1);

                    time--;
                } else if (resttime > 0) {//해당부분에서 쓰레드의 시간이 바뀜으로써 "다음단계가" 진행 되여야 한다.
                    countrestHandler.sendEmptyMessage(0);
                    Onrest = true; // 휴식 상태가 true로 바뀐다.
                    outcount=0;

                    //휴식시간이 30초 가 남았을때의 처리하기(생명주기 상태에 따라서 다르다!!)
                    if(resttime==30 && Activitycondition.equals("onStart")){

                        //RunOnUiThread 를 활용하여, UI부분 수정을 활용해 보기.
                        runOnUiThread(new Runnable() { public void run() {
                            Toast.makeText(getApplicationContext(), "휴식시간이 얼마 남지 않았습니다. 준비해주세요.", Toast.LENGTH_LONG).show(); //내부에 있는 경우에는 토스트로 메시지를 알린다.
                        } });

                    }
                    if(resttime==30 && Activitycondition.equals("onPause")){
                        createNotification3(); //바깥에 있는 경우에는 노티피케이션으로 메시지를 알린다.
                    }

                    Log.e(this.getClass().getName(), "[resttime 값] : " + resttime + "[firsrestttime 값] : " + firstresttime);
                    if (resttime == firstresttime) { //현재시각의 값을 초기시간의 값과 비교하기.
                        dialoghandler.sendEmptyMessage(0); // 쉬는 시각에서 다이얼로그를 띄워준다.
                    }
                    restHandler.sendEmptyMessage(0); // restime값을 텍스트에

                    resttime--;
                } else {
                    Onrest = true;
                    //프로그래스바에 time정보를 반영한다.(time==0 일 때의 처리)
                    Progress.onProgressUpdate(firsttime-time+1);

                    mHandler.sendEmptyMessage(0); // time값을 텍스트에 반영
                    //restHandler.sendEmptyMessage(0); // restime값을 텍스트에 반영

                    setcount--;
                    if (setcount == 0) {
                        clockTextView = findViewById(R.id.count) ;
                        clockTextView.setVisibility(View.INVISIBLE);

                        finishHandler.sendEmptyMessage(0); // time값을 텍스트에 반영

                        //기존에 세팅되어져 있던 시간과 세트 값들을 유지 함
                        time=tmptime;
                        resttime=tmpresttime;
                        setcount=tmpsetcount;
                        nr.mFinished=true;
                        runOnUiThread(new Runnable() { public void run() {
                            Button PauseResume = (Button) findViewById(R.id.PauseResume);
                            PauseResume.setVisibility(View.INVISIBLE);
                        } });


                        break;
                    }
                    //한 사이클이 끝나면, 값을 세팅해주고, 초기화 시킵니다.
                    time = firsttime;
                    resttime = firstresttime;
                    Progress.onProgressUpdate(0);
                    countHandler.sendEmptyMessage(0);
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
        private Object mPauseLock;
        private boolean mPaused;
        private boolean mFinished;

        public WsayRunnable() {
            mPauseLock = new Object();
            mPaused = false;
            mFinished = false;
        }
        @Override
        public void run() {
            while (!mFinished) {
//                if(time==0 && resttime==0){
//                    Log.e(this.getClass().getName(), "Wsay 쓰레드가 완료 되었습니다.");
//                    break;
//                }

                    if (time > -2) {
                        //mHandler.sendEmptyMessage(0);
                        //Log.e(this.getClass().getName(), "time 값 : " + String.valueOf(time));
                        WiseSayHandler.sendEmptyMessage(0);
                        //time--;
                    } else {
                        //Log.e(this.getClass().getName(), "Wsay 쓰레드가 완료 되었습니다.");
                        break;
                    }

                    try {
                        Thread.sleep(5000);
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
                    if(outcount==10 && nr.mPaused==false && Onrest==false){
                        //바깥 상태로 10초 경과가 있는 경우//
                        // 일단 타이머는 정지된다
                        //RunOnUiThread 를 활용하여, UI부분 수정을 활용해 보기.
                        runOnUiThread(new Runnable() { public void run() {
                            //중지가 된 상태에서 되돌아 왔을 때에는 재시작 상태.
//                            Button PauseResume = (Button) findViewById(R.id.PauseResume);
//                            PauseResume.setText("재시작");
                        } });

                        createNotification();
                    }
                    //내 타이머 쓰레드가 멈추지 않은 상태에서 Onstop상태로 20초가 경과 하였을 때 처리.
                    if(outcount>20 && nr.mFinished==false && Onrest==false && nr.mPaused==false ){  //바깥에서의 시간이 20초가 경과했고, 초시계쓰레드는 돌아가고 있으며, 휴식상태가 아니고, 시계를 일시 중지한 상태가 아닐 경우에 처리//
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

                        //RunOnUiThread 를 활용하여, UI부분 수정을 활용해 보기.
                        runOnUiThread(new Runnable() { public void run() {
                            clockTextView = findViewById(R.id.count) ;
                            clockTextView.setVisibility(View.INVISIBLE);

                            //타이머가 초기화가 되어져 버렸을 경우에는 초기화를 시켜버린다//
                            Button PauseResume = (Button) findViewById(R.id.PauseResume);
                            PauseResume.setVisibility(View.INVISIBLE);
                            //타이머 초기화//

                            start.setText("시작");
                            progressBar = (ProgressBar) findViewById(R.id.progressBar);
                            progressBar.setProgress(0); //프로그래스의 값을 초기화 시킴
                            time=tmptime;//time값을 텍스트에 반영하기//
                            mHandler.sendEmptyMessage(0); // 초기의 time값을 텍스트에 반영
                        } });


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



    //명언을 주기적으로 바꿔주기 위한 AsyncTask의 처리//
    class WsayTask extends AsyncTask<Integer, Integer, String> {

        //백그라운드에서 실행되어지는 부분
        @Override
        protected String doInBackground(Integer... params) {
            Log.e(this.getClass().getName(), String.valueOf(params[0]));
            for (; count <= params[0]; count++) {
                Log.e(this.getClass().getName(), "params[0]:"+String.valueOf(params[0]));
                Log.e(this.getClass().getName(), "count:"+String.valueOf(count));
                try {
                    publishProgress(count);
                    if(params[0]==count){
                        count=0;
                    }
                    Thread.sleep(6000);
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
            //txt.setText("Task Starting...");
        }
        @Override
        // Task가 업데이트 중일때.
        protected void onProgressUpdate(Integer... values) {
            TextView wisesayTextView = findViewById(R.id.wisesaying);
            wisesayTextView.setText(""+wsaylist.get(values[0]-1));
            //progressBar.setProgress(values[0]);
            Log.e(this.getClass().getName(), String.valueOf(values[0]));
            Log.e(this.getClass().getName(), wsaylist.get(values[0]-1));
        }
    }



    //프로그래스바를 처리해주기 위한 AsyncTask의 처리//
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
//                final EditText et = new EditText(getApplicationContext());
//                AlertDialog dialog = new AlertDialog.Builder(CountdownTimer.this).setMessage(maincount-setcount+1+"세트 : 어떤 공부를 하셨나요?").setView(et)
//                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .show();
//                dialog.show();
                //다이얼로그는 30초가 지나면 종료된다.
//                try {
//                    Thread.sleep(30000);
//                    dialog.dismiss();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
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
        Button PauseResume = (Button) findViewById(R.id.PauseResume);
        //화면에서 잠시 나갔다가 돌아왔을 때에, 타이머 쓰레드가 중지 상태라면 재실행.
        if(nr.mPaused=true&&PauseResume.getText().equals("중지")) {
                nr.onResume();
        }
        outcount = 0;

        Activitycondition="onStart";
    }

    protected void onStop(){
        super.onStop();
        //nr.onPause();
        //Activitycondition="onStop";
    }

    protected void onDestroy(){
        super.onDestroy();
        Wsaying.mFinished=true;
        WS.interrupt();

    }

    ///////노티피케이션으로 안내 하기///////
    ///notification 구현해 보기//
    private void createNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("혹시 딴 짓 하고 계신가요?");
        builder.setContentText("10초 내로 화면으로 돌아가지 않으면 \n 타이머가 초기화 되버립니다.");

        //builder.setColor(Color.RED);
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
        builder.setContentTitle("타이머가 초기화 되어버렸습니다. 집중하셔야죠!");

        //builder.setColor(Color.RED);
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
    private void createNotification3() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("휴식시간이 얼마남지 않았습니다. 다음 공부를 준비해주세요.");

        //builder.setColor(Color.RED);
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

    @Override
    public void onBackPressed() {

        Log.e(this.getClass().getName(), "현재 타임 쓰레드의 상태 : " +  nr.mFinished);
        //타이머가 돌고 있을 때에는 백버튼이 불가능하다. 하지만 타이머가 안돌고 있을 때에는 백버튼 사용이 가능하다.
        if(nr.mFinished==true) {
            super.onBackPressed();
        }
        else{
            Toast.makeText(getApplicationContext(), "타이머 진행중인 경우 뒤로가기를 하실 수 없습니다.", Toast.LENGTH_LONG).show();
        }
    }
}

