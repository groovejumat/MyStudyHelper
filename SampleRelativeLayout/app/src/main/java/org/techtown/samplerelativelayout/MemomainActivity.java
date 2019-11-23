package org.techtown.samplerelativelayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MemomainActivity extends AppCompatActivity{

    private ArrayList<Dictionary> mArrayList = new ArrayList<>();
    private ArrayList<Dictionary> sharedmArrayList = new ArrayList<>();

    private ArrayList<DictionarySave> SavedList = new ArrayList<>();

    private MemoAdapter mAdapter;
    private int count = -1;


    //온스탑에서 생명주기를 통해 셰어드 프리퍼런스로 저장작업을 처리합니다.
    @Override
    protected void onStop() {
        super.onStop();
        if(mArrayList.size()>0) {
            SavedList=new ArrayList<>();
            for (int i = 0; i < mArrayList.size(); i++) {
                //담는 데이터의 초기화//
                mArrayList.get(i).getId();
                Log.e("태그", "현재 : " + mArrayList.get(i).getId());
                Log.e("태그", "현재 : " + mArrayList.get(i).getContext());
                Log.e("태그", "현재 : " + mArrayList.get(i).getKorean());
                Log.e("태그", "현재 : " + mArrayList.get(i).getEnglish());
                DictionarySave Saveinfo = new DictionarySave(mArrayList.get(i).getId(), mArrayList.get(i).getContext(), mArrayList.get(i).getKorean(), mArrayList.get(i).getEnglish());
                SavedList.add(Saveinfo);
                //저장되어진 저장용 어레이리스트의 데이터를 확인.
            }
            Log.e("태그", "현재 저장될 SavedList의 갯수는 : " + SavedList.size());
        }

        // Activity가 종료되기 전에 저장한다.
        SharedPreferences sharedPreferences = getSharedPreferences("sMemo",MODE_PRIVATE);

        //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String data_json = gson.toJson(SavedList);  // 사용자가 입력한 저장할 데이터
        editor.putString("saved_data",data_json); // key, value를 이용하여 저장하는 형태
        //다양한 형태의 변수값을 저장할 수 있다.
        Log.e("LOG", data_json);

        Type type = new TypeToken<ArrayList<Dictionary>>() {}.getType();

        if(data_json.length()>10) {
            SavedList = gson.fromJson(data_json, type);
        }
        Log.e("LOG", "해당 데이터의 사이즈는 : " + String.valueOf(SavedList.size()));

        //최종 커밋
        editor.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("msg","11 OnCreate!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_check);


        //저장된 값을 불러오기 위해 같은 네임파일을 찾음.
        SharedPreferences sf = getSharedPreferences("sMemo",MODE_PRIVATE);
        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        String data_json = sf.getString("saved_data","");
        Log.e("태그", "저장되어진 json 데이터 값 : " + data_json);
        Type type = new TypeToken<ArrayList<DictionarySave>>() {}.getType();
        Gson gson = new Gson();
        SavedList=gson.fromJson(data_json,type);
        Log.e("태그", "저장되어진 리스트 사이즈 값 " + SavedList.size());
        if(SavedList.size()>0){
            for(int i = 0; i<SavedList.size(); i++){
                Dictionary savedata= new Dictionary(SavedList.get(i).theme,SavedList.get(i).content,SavedList.get(i).date,SavedList.get(i).time);
                mArrayList.add(savedata);
            }
            Log.e("태그", "저장되어진 리스트 사이즈 값 " + mArrayList.size());
        }
        //mArrayList=sharedmArrayList;
        //Log.e("태그", "저장되어진 json 의 갯수 : " + sharedmArrayList.size());
        //불러온 값 덮어 씌우기
        //images=imagesshared;

        //리사이클러뷰 뷰간 간격 조정
        RecyclerDecoration spaceDecoration = new RecyclerDecoration(20);



        //생성할 릴사이클러 뷰의 아이디 값을 세팅하고, 레이아웃 매니저를 통해서 해당 리사이클러뷰를 새로 진행 방식으로 만든다.
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(spaceDecoration);

        //어레이 리스트 생성
        //mArrayList = new ArrayList<>();
        Dictionary dict = new Dictionary("시연용메모", "시연용", "시연용" );
        dict.setContext("이 메모는 시연용 입니다.");



        //내가 만든 어댑터 클래스 객체를 생성
        mAdapter = new MemoAdapter(this,mArrayList);
        //mAdapter = new CustomAdapter(this, mArrayList);

        //내가 만든 어댑터를 생성한 리사이클려뷰에 세팅
        mRecyclerView.setAdapter(mAdapter);


        //뷰나누기??
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        //추가하기에서 들여온 데이터를 추가한 인텐트를 받는다. (요청코드 수정하기)
        //intent.getExtras()!=null
        Intent intent = getIntent();
        if(intent.getSerializableExtra("itemdata")!=null) {
            System.out.println("데이터 추가 작업 진행");
            Dictionary getobject = (Dictionary) intent.getSerializableExtra("itemdata");
            System.out.println("추가된 메모장 정보 확인");
            System.out.println(getobject.getEnglish());
            System.out.println(getobject.getKorean());
            System.out.println(getobject.getContext());
            mArrayList.add(0,getobject);
            mAdapter.notifyItemInserted(0);
//            mRecyclerView.scrollToPosition(0);
        }

        //변경된 데이터가 존재하면(activity_deit_memo로 부터 전달 받음)
        Intent memo_edit_data = getIntent();
        if(intent.getSerializableExtra("chagedata")!=null) {
            System.out.println("데이터 변경 작업 진행");
            Dictionary getobject = (Dictionary)intent.getSerializableExtra("chagedata");
            int position = getIntent().getIntExtra("position",1);
            System.out.println("포지션 값을 받아옴" + position);
            mArrayList.set(position,getobject);
            mAdapter.notifyItemInserted(0);
        }



        //리사이클러 뷰에서 아이템을 터치했을 때의 동작을 처리한다.
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                //아이템을 클릭했을 때에 메모장에 대한 정보를 보내주어야 하니까, 해당 내용에 관련된 dic객체를 생성해서 보내준다.
                Intent intent2 = new Intent(getApplication(), EditmemoActivity.class);
                Dictionary dict = mArrayList.get(position);
                Toast.makeText(getApplicationContext(), dict.getId() + ' ' + dict.getEnglish() + ' ' + dict.getKorean() + position, Toast.LENGTH_LONG).show();
                System.out.println("<<메모장 정보 확인>>");
                System.out.println(dict.getId());
                System.out.println(dict.getEnglish());
                System.out.println(dict.getKorean());
                System.out.println(dict.getContext());
                String a = dict.getId();
                String b = dict.getEnglish();
                String c = dict.getContext();
                String e = dict.getKorean();
                int d = position;

                //제목
                intent2.putExtra("title", a);
//                intent2.putExtra("english", b);
//                intent2.putExtra("english2",e);
                //내용
                intent2.putExtra("context", c);
                //인덱스의 위치
                intent2.putExtra("position", d);

                //startActivity(intent2);
                startActivityForResult(intent2, 2);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        //엑티비티로 전달 후에 값저장하기 메모내용을 추가해주는 뷰이벤트 처리
        Button buttonInsertA = (Button) findViewById(R.id.button_insert_activity);
        buttonInsertA.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddmemoActivity.class); // 다음 넘어갈 클래스 지정
                startActivityForResult(intent,1); // 다음 화면으로 넘어간다
            }
        });
    }




    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MemomainActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MemomainActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //추가 작업처리
        if(requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                Log.e("LOG", "추가 결과 받기 성공");
                Dictionary result = (Dictionary)data.getSerializableExtra("itemdata");
                System.out.println("추가된 메모장 정보 확인");
                System.out.println(result.getEnglish());
                System.out.println(result.getKorean());
                System.out.println(result.getContext());
                mArrayList.add(0,result);
                mAdapter.notifyItemInserted(0);
            }
        }

        //수정작업 처리
        if(requestCode == 2){
            if (resultCode == Activity.RESULT_OK){
                Log.e("LOG", "수정작업 결과 받기 성공");
                Dictionary getobject = (Dictionary)data.getSerializableExtra("chagedata"); // Edit_memo_activity에서 보낸 정보를 받아옵니다.
                int position = data.getIntExtra("position",1); // 수정할 포지션 정보를 가지고 옵니다.
                Log.e("LOG", getobject.getId());
                Log.e("LOG",getobject.getContext());
                Log.e("LOG", String.valueOf(position)); // 포지션 값의 확인.
                mArrayList.set(position,getobject); // 해당 포지션의 객체값을 가져온 객체 정보로 덮어쓰기 작업을 합니다.
                mAdapter.notifyDataSetChanged();
            }
        }
    }

}
