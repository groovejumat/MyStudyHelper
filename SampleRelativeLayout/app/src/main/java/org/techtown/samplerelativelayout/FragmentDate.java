package org.techtown.samplerelativelayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FragmentDate extends Fragment {
    TextView Datecheck;
    String Date;

    private ArrayList<Todo> mArrayList = new ArrayList<>();
    private TodoAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        return inflater.inflate(R.layout.recyclerfragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        Date = bundle.getString("dateinfo");
        Log.e("태그","프레그먼트에서 가져온 키값(날짜) : " +  Date );

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("calendardata",MODE_PRIVATE);
        String data_json = sharedPreferences.getString(Date, "");
        Log.e("태그","현재 셰어드 프리퍼런스에서 가지고 온 정보 확인 : " +  data_json);
        Type type = new TypeToken<ArrayList<Todo>>() {}.getType();
        Gson gson = new Gson();
        if(data_json.length()>0) {
            mArrayList = gson.fromJson(data_json, type);
        }
        else{
            Log.e("태그","현재 가져온 키값에 대한 정보가 없습니다." +  Date );
        }


        RecyclerView mRecyclerView = (RecyclerView) getView().findViewById(R.id.recyclerview_main_list);
        mAdapter = new TodoAdapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mAdapter.notifyDataSetChanged();

        Datecheck = (TextView)getView().findViewById(R.id.fragmentDate);
        Datecheck.setText(Date);

        //엑티비티로 전달 후에 값저장하기 메모내용을 추가해주는 뷰이벤트 처리
        final EditText todoText = (EditText)getView().findViewById(R.id.addText);
        Button buttonInsertA = (Button) getView().findViewById(R.id.addtodo);
        buttonInsertA.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final String todotext = todoText.getText().toString();
                Log.e("태그", "추가된 텍스트의 내용" + todotext);
                if(todotext.length()>0) {
                    Todo item = new Todo(todotext, false);
                    mArrayList.add(item);
                    mAdapter.notifyDataSetChanged();
                    //mArrayList.clear();
                }
                else{
                    Toast.makeText(getContext(), "할일을 한글자 이상 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    @Override
    public void onPause() {
        Log.d(this.getClass().getSimpleName(), "onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        //해당 부분에 대해서 각 어레이 리스트 들의 저장 처리를 하자//
        // Activity가 종료되기 전에 저장한다.
        Log.e("태그","프레그먼트에서 가져온 날짜 : " +  Date + "저장할 데이터의 갯수 : " + mArrayList.size());
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("calendardata",MODE_PRIVATE);

        //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String data_json = gson.toJson(mArrayList);  // 사용자가 입력한 저장할 데이터
        editor.putString(Date,data_json);
        Log.e("태그","저장되어진 키 값 : " +  Date + "저장된 내용 : " + data_json );

        editor.commit();

        Log.d(this.getClass().getSimpleName(), "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(this.getClass().getSimpleName(), "onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(this.getClass().getSimpleName(), "onDestroy()");
        super.onDestroy();
    }

}
