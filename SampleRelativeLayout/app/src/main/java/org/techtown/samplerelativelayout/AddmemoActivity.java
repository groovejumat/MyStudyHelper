package org.techtown.samplerelativelayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AddmemoActivity extends AppCompatActivity {

//    final EditText editTextTitle = (EditText)findViewById(R.id.memo_title);
//    //final EditText editTextEnglish = (EditText)findViewById(R.id.memo_content);
//    final EditText editTextContext = (EditText)findViewById(R.id.memo_context);
    boolean OnstopSaving = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmemo);
        final EditText editTextTitle = (EditText)findViewById(R.id.memo_title);
        //final EditText editTextEnglish = (EditText)findViewById(R.id.memo_content);
        final EditText editTextContext = (EditText)findViewById(R.id.memo_context);

        /////셰어드 프리퍼런스로 저장되어진 값 가지고 오기/////
        //저장된 값을 불러오기 위해 같은 네임파일을 찾음.
        SharedPreferences sf = getSharedPreferences("SavedAddmemo",MODE_PRIVATE);
        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        final String savedtitle = sf.getString("addmemoTitle","");
        final String savedcontext = sf.getString("addmemoContext","");
        if(savedtitle.length()>1 || savedcontext.length()>1){
            Log.e("LOG", "메모 내용을 불러와주는 처리가 필요하다.");
            Log.e("LOG", "불러온 내용 확인하기");
            Log.e("LOG", "제목정보" + savedtitle);
            Log.e("LOG", "내용"+savedcontext);
            //alert dialong로 처리하기//
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("작성 중인 메모내용이 확인 되었습니다. 내용을 불러올까요?");

            builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            builder.setNegativeButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.e("LOG", "제목정보" + savedtitle);
                    Log.e("LOG", "내용"+savedcontext);
                    editTextTitle.setText(savedtitle);
                    editTextContext.setText(savedcontext);
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }





        String title = editTextTitle.getText().toString();
        System.out.println(title);
        //String english = editTextEnglish.getText().toString();
        //System.out.println(english);
        String context = editTextContext.getText().toString();
        System.out.println(context);


        //putdata get setted//




        Button buttonInsertA = (Button) findViewById(R.id.addconfirm);
        buttonInsertA.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String title = editTextTitle.getText().toString();
                System.out.println(title);

                String context = editTextContext.getText().toString();
                System.out.println(context);

                if(title.length()>0) {
                    Intent intent = new Intent(getApplication(), MainActivity.class);

                    Toast.makeText(getApplicationContext(), "메모가 저장 되었습니다.", Toast.LENGTH_LONG).show();

                    final Dictionary putobject = new Dictionary(title, "의미", "없음");
                    putobject.setContext(context);

                    intent.putExtra("itemdata", putobject);

                    setResult(Activity.RESULT_OK, intent);

                    //셰어드프리퍼런스의 임시 메모내용을 초기화
                    Log.e("LOG", "셰어드 프리퍼런스 내용을 초기화 합니다.");
                    SharedPreferences sharedPreferences = getSharedPreferences("SavedAddmemo", MODE_PRIVATE);
                    //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    // 사용자가 입력한 저장할 데이터
                    editor.putString("addmemoTitle", null);
                    editor.putString("addmemoContext", null);// key, value를 이용하여 저장하는 형태
                    //최종 커밋
                    editor.commit();

                    OnstopSaving = false;

                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "제목을 한글자 이상 해주세요.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //온스탑에서 생명주기를 통해 셰어드 프리퍼런스로 저장작업을 처리합니다.
    @Override
    protected void onStop() {
        super.onStop();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("작성 중이신 메모 내용을 임시 저장 할까요?");

        builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.setNegativeButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        //저장한 글자의 내용이 한 글자 이상이라면 셰어드 프리퍼런스로 임시저장 처리를한다.
        final EditText editTextTitle = (EditText) findViewById(R.id.memo_title);
        final EditText editTextContext = (EditText) findViewById(R.id.memo_context);
        if (OnstopSaving == true) {// 해당 메모가 저장등록 버튼을 거쳐가지 않았을 경우 저장 처리.
            if (editTextTitle.getText().toString().length() > 1 || editTextContext.getText().toString().length() > 1) {
                Log.e("LOG", "메모 내용이 임시 저장 되었습니다.");

                SharedPreferences sharedPreferences = getSharedPreferences("SavedAddmemo", MODE_PRIVATE);

                //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                // 사용자가 입력한 저장할 데이터
                editor.putString("addmemoTitle", editTextTitle.getText().toString());
                editor.putString("addmemoContext", editTextContext.getText().toString());// key, value를 이용하여 저장하는 형태

                //최종 커밋
                editor.commit();
            }
        }
    }
}

