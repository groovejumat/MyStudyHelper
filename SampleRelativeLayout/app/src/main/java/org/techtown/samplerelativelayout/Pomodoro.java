package org.techtown.samplerelativelayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Locale;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class Pomodoro extends AppCompatActivity {

    private EditText mEditTextInput;
    private TextView mTextViewCountDown;
    private TextView studytimecnt;
    private Button mButtonSet;
    private Button mButtonStartPause;
    private Button mButtonReset;

    //
    private int OnResumeCount = 0;


    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long studytime;
    private long mEndTime;
    private static final String TAG = "Pomodoro";




    //엑티비티에서 로그 찍기 필필 수수//
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart() called");
    }


    @Override
    protected void onResume() {
        super.onResume();

//        try { // 현재 작성중인 글을 불러와주는 역할
//            // 파일에서 읽은 데이터를 저장하기 위해서 만든 변수
//            StringBuffer data1 = new StringBuffer();
//            FileInputStream fis = openFileInput("myfile1.txt");//파일명
//            BufferedReader buffer = new BufferedReader
//                    (new InputStreamReader(fis));
//            String str = buffer.readLine(); // 파일에서 한줄을 읽어옴
//            while (str != null) {
//                data1.append(str + "");
//                str = buffer.readLine();
//            }
//            //editTextNew.setText(data1);
//            String tmp = data1.toString();
//            studytime = Long.parseLong(tmp);
//            System.out.println("다음 숫자를 가지고 왔습니다. " + studytime);
//            buffer.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        OnResumeCount++;
//        System.out.println(OnResumeCount);
//        System.out.println(mButtonStartPause.getText());

        //

        //생명주기로 타이머 자동 실행
        OnResumeCount++;

        if(mButtonStartPause.getText()=="Start" && OnResumeCount<3) {
            startTimer();
            Toast.makeText(Pomodoro.this, "타이머를 다시 실행합니다. 딴짓하지말고 집중하세요.", Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        OnResumeCount++;
        System.out.println(OnResumeCount);
        Log.d(TAG, "onPause() called");
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro);

        //생명주기로 타이머 자동 실행
        OnResumeCount++;

        TextView textView = (TextView)findViewById(R.id.StatusText);

        //여기서 가져온 정보를 통해서, 텍스트 표지판을 만들어 보자.



        mEditTextInput = findViewById(R.id.edit_text_input);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        mButtonSet = findViewById(R.id.button_set);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);

        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mEditTextInput.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(Pomodoro.this, "몇분 동안 하실지 정해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput == 0) {
                    Toast.makeText(Pomodoro.this, "음수는 넣으실 수 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                setTime(millisInput);
                mEditTextInput.setText("");
            }
        });

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                    TextView txt = (TextView) findViewById(R.id.StatusText);
                    txt.setText("열공 모드 끝!!");
                } else {
                    startTimer();
                    TextView txt = (TextView) findViewById(R.id.StatusText);
                    txt.setText("열공 모드 시작!!");
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        //해당 시간에 대한 정보를 보내줌!!
        findViewById(R.id.ShowMyTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Pomodoro.this, "클릭시 명시적 인텐트 동작 시작", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplication(), ShowMyTime.class);
                intent.putExtra("Key",studytime);
                startActivity(intent);
            }
        });
    }

    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
        resetTimer();
        closeKeyboard();
    }

    private void startTimer() {
        studytime=studytime;
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                studytime++;
                System.out.println(studytime);
                TextView txt = (TextView) findViewById(R.id.studytimecount);
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterface();
            }
        }.start();

        mTimerRunning = true;
        updateWatchInterface();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }

    private void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }

    private void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        mTextViewCountDown.setText(timeLeftFormatted);
        studytimecnt = findViewById(R.id.studytimecount);
        //시간 분 초 계산하기
        if (studytime/3600 > 1){
            long hour = studytime/3600;
            long min = studytime/3600;

            studytimecnt.setText(""+studytime+"테스트");
        }

        else if(studytime/60 > 0) {
            long min = studytime/60;
            long sec = studytime%60;
            studytimecnt.setText("현재까지 "+min+"분"+sec+"초 공부함.");
        }
        else {
            studytimecnt.setText("지금 까지 총 " + studytime + "초 공부 하였습니다.");
        }

        //studytimeView.setText((int) studytime);
    }

    private void updateWatchInterface() {
        if (mTimerRunning) {


            mEditTextInput.setVisibility(View.INVISIBLE);
            mButtonSet.setVisibility(View.INVISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
        } else {
            mEditTextInput.setVisibility(View.VISIBLE);
            mButtonSet.setVisibility(View.VISIBLE);
            mButtonStartPause.setText("Start");

            if (mTimeLeftInMillis < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }

            if (mTimeLeftInMillis < mStartTimeInMillis) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        String data1 = String.valueOf(studytime);
        //String data1 = studytime;

        try {
            FileOutputStream fos = openFileOutput
                    ("myfile1.txt", // 파일명 지정
                            Context.MODE_PRIVATE);// 저장모드
            PrintWriter out = new PrintWriter(fos);
            out.println(data1);
            System.out.println("저장된 숫자 값은 : " + data1);
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "onStop() called");

        //액티비티에서 세팅되어진 버튼의 상태가 "puase"일 경우 Onstop생명주기에서 타이머 작업을 중단시킴.
        if(mButtonStartPause.getText()=="Pause") {
            pauseTimer();
            Toast.makeText(Pomodoro.this, "화면을 떠나셔서 타이머가 중단되었습니다.", Toast.LENGTH_SHORT).show();
        }

        //여기다가 값을 저장하는 작업을 하는 구나 멈출 때//
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        //적용하기
        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {




        try { // 현재 작성중인 글을 불러와주는 역할
            // 파일에서 읽은 데이터를 저장하기 위해서 만든 변수
            StringBuffer data1 = new StringBuffer();
            FileInputStream fis = openFileInput("myfile1.txt");//파일명
            BufferedReader buffer = new BufferedReader
                    (new InputStreamReader(fis));
            String str = buffer.readLine(); // 파일에서 한줄을 읽어옴
            while (str != null) {
                data1.append(str + "");
                str = buffer.readLine();
            }
            //editTextNew.setText(data1);
            String tmp = data1.toString();
            studytime = Long.parseLong(tmp);
            System.out.println("다음 숫자를 가지고 왔습니다. " + studytime);
            buffer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(studytime);
        studytimecnt = findViewById(R.id.studytimecount);
        //studytime=studytime;
        if (studytime/3600 > 0){
            long hour = studytime/3600;
            long min = studytime/3600;

            studytimecnt.setText(""+studytime+"테스트");
        }

        else if(studytime/60 > 0) {
            long min = studytime/60;
            long sec = studytime%60;
            studytimecnt.setText("현재까지 "+min+"분"+sec+"초 공부함.");
        }
//        else {
//            studytimecnt.setText("지금 까지 총 " + studytime + "초 공부 하였습니다.");
//        }


        super.onStart();

        //생명주기로 타이머 자동 실행
        OnResumeCount++;

        Log.d(TAG, "onStart() called");
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateWatchInterface();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();


            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateWatchInterface();
            } else {
                startTimer();
            }
        }
    }
}
