package org.techtown.samplerelativelayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class StudyCalendarActivity extends AppCompatActivity {

    private ArrayList<Todo> mArrayList = new ArrayList<>();
    private ArrayList<Todo> savedArrayList = new ArrayList<>();
    private TodoAdapter mAdapter;
    private String beforeDate;
    private String curDate;
    private String Date;

    private FragmentManager fragmentManager;
    private FragmentDate fragmentDate;
    private FragmentTransaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studycalender);
        final Calendar test = Calendar.getInstance();
        final CalendarView calendar = (CalendarView)findViewById(R.id.calendarView);


        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        curDate = sdf.format(date.getTime());
        Log.e("태그","현재의 날짜 정보 : " +  curDate);


        //테스트로 프래그먼트 생성하기//
        fragmentManager = getSupportFragmentManager();

        fragmentDate = new FragmentDate();
        Bundle bundle = new Bundle();

        //프래그먼트에 전달할 데이터를 세팅 : 현재 날짜 정보//
        bundle.putString("dateinfo",curDate);
        fragmentDate.setArguments(bundle);

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentDate).commitAllowingStateLoss();


        //캘린더 날짜 클릭 리스너 확인.
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public
            //내가 클릭을 하여 달력의 정보를 변경시켰을때.
            void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                SharedPreferences sharedPreferences = getSharedPreferences("CalendarData",MODE_PRIVATE);
                //날짜 값을 세팅//
                month = month+1;
                Date=year+""+month+""+dayOfMonth;
                //선택전의 날짜가 현재 클릭한 날짜로 바뀌어지고.
                beforeDate=curDate;
                Log.e("태그","선택전 날짜 : " +  String.valueOf(beforeDate));
                //선택전 날짜에 대해서 어레이 리스트 처리를 하자.

                //현재날짜가
                curDate=Date;
                Log.e("태그","선택후의 날짜 : " +  String.valueOf(curDate));


                //테스트로 프래그먼트 생성하기//
                fragmentManager = getSupportFragmentManager();

                fragmentDate = new FragmentDate();
                Bundle bundle = new Bundle();

                //프래그먼트에 전달할 데이터를 세팅 : 현재 날짜 정보//
                bundle.putString("dateinfo",curDate);
                fragmentDate.setArguments(bundle);

                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frameLayout, fragmentDate).commitAllowingStateLoss();


                //기존에 있던 arraylist를 해당 날짜로 셰어드 프리퍼런스로 저장//
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                if(mArrayList!=null) {
                    String data_json = gson.toJson(mArrayList);  // 사용자가 입력한 저장할 데이터
                    editor.remove(beforeDate);
                    editor.putString(beforeDate, data_json); // key, value를 이용하여 저장하는 형태
                    Log.e("태그", "바뀌기 전의 데이터를 저장하였습니다. 저장된 키값은 : " + String.valueOf(beforeDate) + "갯수는 :" + mArrayList.size());
                    editor.commit();
                }


//                if(mArrayList!=null) {
//                    //초기화 시킴//
//                    mArrayList.clear();
//                    //mAdapter = new TodoAdapter(mArrayList);
//                    mAdapter.notifyDataSetChanged();
//                }


                //해당 날짜에 대해서 저장된 것을 가지고 오기
                //////////////////셰어드 프리퍼런스의 처리///////////////////
                //저장된 값을 불러오기 위해 같은 네임파일을 찾음.
                SharedPreferences sf = getSharedPreferences("CalendarData",MODE_PRIVATE);
                //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
                String data_json = sf.getString(curDate, "");
                Log.e("태그", "저장되어진 json 데이터 값 : " + data_json);
                Type type = new TypeToken<ArrayList<Todo>>() {}.getType();
                gson = new Gson();
//                savedArrayList=gson.fromJson(data_json,type);
//                if(mArrayList!=null) {
//                    Log.e("태그", "불러져온 해당 날짜의 저장 갯수 : " + savedArrayList.size());
//                    mArrayList=savedArrayList;
//                    mAdapter.notifyDataSetChanged();
//                    //그리고 해당 값을 반영
//                }
//                else {
//                    Log.e("태그", "현재 저장되어져 있는 값이 없습니다.");
//                }

                //엑티비티로 전달 후에 값저장하기 메모내용을 추가해주는 뷰이벤트 처리
//                final EditText todoText = (EditText)findViewById(R.id.addText);
//                Button buttonInsertA = (Button) findViewById(R.id.addtodo);
//                buttonInsertA.setOnClickListener(new View.OnClickListener(){
//                    @Override
//                    public void onClick(View view) {
//                        final String todotext = todoText.getText().toString();
//                        Log.e("태그", "추가된 텍스트의 내용" + todotext);
//                        if(todotext.length()>0) {
//                            Todo item = new Todo(todotext, false);
//                            mArrayList.add(item);
//                            mAdapter.notifyDataSetChanged();
//                            //mArrayList.clear();
//                        }
//                        else{
//                            Toast.makeText(getApplicationContext(), "할일을 한글자 이상 입력해 주세요.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
            }
        });
    }


    public void highlightCalendar(String text){
        //해당 부분에서 달력 하이라이트 처리를 한다.
    }
}
