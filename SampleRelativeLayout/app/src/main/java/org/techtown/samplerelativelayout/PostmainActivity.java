package org.techtown.samplerelativelayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PostmainActivity extends AppCompatActivity {

    //어댑터 종류 설정하기//
    private ArrayList<Movie> items = new ArrayList<>();
    private ArrayList<Movie> saveditems = new ArrayList<>();
    private ArrayList<Movie> getitems = new ArrayList<>();
    private MovieAdapter adapter = new MovieAdapter(items);

    boolean Onsearch = false;

    //온스탑에서 생명주기를 통해 셰어드 프리퍼런스로 저장작업을 처리합니다.
    @Override
    protected void onStop() {
        super.onStop();

        // Activity가 종료되기 전에 저장한다.
        SharedPreferences sharedPreferences = getSharedPreferences("clipboard",MODE_PRIVATE);

        //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String data_json = gson.toJson(items);  // 사용자가 입력한 저장할 데이터
        editor.putString("saved_data",data_json); // key, value를 이용하여 저장하는 형태
        //다양한 형태의 변수값을 저장할 수 있다.
        Log.e("LOG", data_json);

        Type type = new TypeToken<ArrayList<Movie>>() {}.getType();

        if(data_json.length()>10) {
            saveditems = gson.fromJson(data_json, type);
        }
        Log.e("LOG", "해당 데이터의 사이즈는 : " + String.valueOf(saveditems.size()));

        //최종 커밋
        editor.commit();
    }




    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
//            items.remove(position);
//            adapter.notifyItemRemoved(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(PostmainActivity.this);

            builder.setTitle("클립 보드를 삭제하시겠습니까?");


            builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyDataSetChanged();
                }
            });

            builder.setNegativeButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    items.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_actions, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        //searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Onsearch = true;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Onsearch = true;
                Log.e("LOG", newText);
                Log.e("LOG", "검색 중인 글자 확인하기.");
                adapter.getFilter().filter(newText);
                Log.e("LOG", "검색기능이 실행되어졌음.");
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Onsearch = false;
                Log.e("LOG", "클로즈 버튼을 눌렸음.");
                return false;
            }
        });
        return true;
    }

    //액션바 기능 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        //or switch문을 이용하면 될듯 하다.
        if (id == android.R.id.home) {
            Toast.makeText(this, "홈아이콘 클릭", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_add) {
            //////액션바에서 내용 추가하기 기능 생성//////
            Toast.makeText(this, "추가버튼을 클릭", Toast.LENGTH_SHORT).show();
            //커스텀 다이얼로그 생성 후 띄우기//
            final Dialog dialog2 = new Dialog(PostmainActivity.this);
            dialog2.setContentView(R.layout.postdialog);
            //final EditText text1 = (EditText) dialog2.findViewById(R.id.image_url);
            final EditText text2 = (EditText) dialog2.findViewById(R.id.post_name);
            final EditText text3 = (EditText) dialog2.findViewById(R.id.url_link);

            Button button1 = (Button) dialog2.findViewById(R.id.cancel);
            button1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {

                    dialog2.dismiss();   //다이얼로그를 닫는 메소드입니다.
                }
            });

            Button button2 = (Button) dialog2.findViewById(R.id.ok);
            button2.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //String image_url = text1.getText().toString();
                    String post_name = text2.getText().toString();
                    String url_link = text3.getText().toString();
                    Movie m = new Movie("의미없음","의미없음",post_name,url_link);
                    items.add(m);
                    //참조용 데이터 리스트도 변경됌.
                    adapter.SearchDataReSet(items);
                    adapter.notifyDataSetChanged();
                    dialog2.dismiss();    //등록 작업을 진행 해줍니다.
                }
            });

            dialog2.show();
            return true;
        }
        if (id == R.id.action_grid) {
            Toast.makeText(this, "그리드버튼을 클릭", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, PostmainActivity.class);
            intent.putExtra("data", this.items);
            Log.e("LOG", "보내는 아이템의 갯수를 확인합니다." + String.valueOf(this.items.size()));
            startActivity(intent);
            finish();
            return true;
        }

        if (id == R.id.action_horizontal) {
            Toast.makeText(this, "평행모드 버튼을 클릭", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, PostmainActivity.class);
            intent.putExtra("number",2);
            intent.putExtra("data", this.items);
            Log.e("LOG", "보내는 아이템의 갯수를 확인합니다." + String.valueOf(this.items.size()));
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.action_scriptonly) {
            Toast.makeText(this, "세로 모드 버튼을 클릭", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, PostmainActivity.class);
            intent.putExtra("number",1);
            intent.putExtra("data", this.items);
            Log.e("LOG", "보내는 아이템의 갯수를 확인합니다." + String.valueOf(this.items.size()));
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postmain);

        //////////////////셰어드 프리퍼런스의 처리///////////////////
        //저장된 값을 불러오기 위해 같은 네임파일을 찾음.
        SharedPreferences sf = getSharedPreferences("clipboard",MODE_PRIVATE);
        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        String data_json = sf.getString("saved_data","");
        Log.e("태그", "저장되어진 json 데이터 값 : " + data_json);
        Type type = new TypeToken<ArrayList<Movie>>() {}.getType();
        Gson gson = new Gson();
        saveditems=gson.fromJson(data_json,type);
        items=saveditems;
        Log.e("태그", "저장되어진 json 의 갯수 : " + saveditems.size());

        //fillbasicitems();
        //(데이터를 뷰모드 변경시에 다시 세팅하기 위해서 만든 기능)
        Intent intent = getIntent();
        int viewmode = intent.getIntExtra("number",0);
        if(intent.getSerializableExtra("data")!=null) {
            System.out.println(viewmode);
            getitems = (ArrayList<Movie>) intent.getSerializableExtra("data");
            Log.e("LOG", "받는 아이템의 갯수를 확인합니다." + String.valueOf(this.getitems.size()));
            this.items=getitems;
        }
        Log.e("LOG", "어댑터에 세팅되어진 아이템의 갯수를 확인합니다." );
        adapter.setItems(items);
        Log.e("LOG", "어댑터에 세팅되어진 아이템의 갯수를 확인합니다." + items.size());
        adapter = new MovieAdapter(items);
        //recycleView 초기화
        RecyclerView recyclerView = findViewById(R.id.recycler_view);







        if(viewmode == 3){
            LinearLayoutManager linearLayoutManager
                    = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
        }


        if(viewmode == 2){
            LinearLayoutManager linearLayoutManager
                    = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
        }

        else if (viewmode == 1){
            LinearLayoutManager horizonalLayoutManager
                    = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(horizonalLayoutManager);
        }

        else {
            StaggeredGridLayoutManager mStgaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(mStgaggeredGridLayoutManager);
        }

        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void fillbasicitems(){

        Movie movie1 = new Movie("https://wooooooak.github.io/public/img/android/recycler1.png",
                "action", "aRecyclerView", "https://wooooooak.github.io/android/2019/03/28/recycler_view/");
        Log.e("LOG", "어댑터에 세팅되어진 아이템의 갯수를 확인합니다." + movie1.getUrl());

        Movie movie2 = new Movie("https://kairo96.gitbooks.io/android/content/pic2/2-4-1-1.jpg",
                "action", "bLifeCycle", "https://recipes4dev.tistory.com/90");

        Movie movie3 = new Movie("https://t1.daumcdn.net/cfile/tistory/99BADD375C88BD1A24",
                "action", "cGridView", "https://blog.naver.com/PostView.nhn?blogId=rkswlrbduf&logNo=221200565254&redirect=Dlog&widgetTypeCall=true&directAccess=false");

        Movie movie4 = new Movie("https://kairo96.gitbooks.io/android/content/pic2/2-4-1-1.jpg",
                "action", "dPostname", "https://recipes4dev.tistory.com/90");

        Movie movie5 = new Movie("https://funshop.akamaized.net/abroad/028/14237/Android%2010_0.jpg",
                "action", "ePostname", "https://recipes4dev.tistory.com/90");

        Movie movie6 = new Movie("",
                "action", "fPostname", "https://recipes4dev.tistory.com/90");

        Movie movie7 = new Movie("",
                "action", "gPostname", "https://recipes4dev.tistory.com/90");

        items.add(movie1);
        items.add(movie2);
        items.add(movie3);
        items.add(movie4);
        items.add(movie5);
        items.add(movie6);
        items.add(movie7);
    }

}
