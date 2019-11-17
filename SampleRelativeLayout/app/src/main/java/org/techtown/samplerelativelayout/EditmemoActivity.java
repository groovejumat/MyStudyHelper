package org.techtown.samplerelativelayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditmemoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_editmemo);
        Intent intent =getIntent();
        String title = (String)getIntent().getStringExtra("title");
        //String english = (String)getIntent().getStringExtra("english");
        //String english2 = (String)getIntent().getStringExtra("english2");
        String content = (String)getIntent().getStringExtra("context");
        final int position = getIntent().getIntExtra("position",1);
        System.out.println("선택한 위치 값 출력하기" + position);
        final Dictionary dic = new Dictionary(title,"의미","없음"); // 가져온 정보를 통해서 새로운 dic객체를 생성해 줍니다.
        dic.setContext(content);

        //객체형태로 정보를 받는다//
        //Dictionary getobject = (Dictionary) intent.getSerializableExtra("putdic");
        //System.out.println(getobject.getId());



        //현재 엑티비티에 있는 에디트 텍스트들을 연결
        final EditText editTextTitle = (EditText)findViewById(R.id.memo_title_ed);
        editTextTitle.setEnabled(false);

        final TextView editTextKorean = (TextView)findViewById(R.id.memo_time_ed);
        final TextView editTextEnglish = (TextView)findViewById(R.id.memo_date_ed);

        final EditText editTextContext = (EditText) findViewById(R.id.memo_context_ed);
        editTextContext.setEnabled(false);


        editTextTitle.setText(dic.getId()); //제목
        editTextEnglish.setText(dic.getEnglish()); //날짜 및 시간
        editTextKorean.setText(dic.getKorean()); //날짜 및 시간
        editTextContext.setText(dic.getContext()); //내용 세팅

        Button buttonInsertA = (Button) findViewById(R.id.modify_confirm);
        buttonInsertA.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String title = editTextTitle.getText().toString();
                System.out.println(title);

                String english = editTextEnglish.getText().toString();
                System.out.println(english);

                String context = editTextContext.getText().toString();
                System.out.println(context);

                Intent intent = new Intent(getApplication(), MainActivity.class);

                Toast.makeText(getApplicationContext(), "메모가 수정 되었습니다.(메모 수정 객체 및 포지션 값 전달)", Toast.LENGTH_LONG).show();

                final Dictionary putobject= new Dictionary(title,english,"test");
                putobject.setContext(context);

                intent.putExtra("chagedata", putobject);
                intent.putExtra("position",position);

                //startActivity(intent);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

        Button buttonInsertB = (Button) findViewById(R.id.edit_mode);
        buttonInsertB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                editTextTitle.setEnabled(true);
                editTextContext.setEnabled(true);
            }
        });

    }
}

