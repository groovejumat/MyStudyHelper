package org.techtown.samplerelativelayout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddmemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmemo);

        final EditText editTextTitle = (EditText)findViewById(R.id.memo_title);
        //final EditText editTextEnglish = (EditText)findViewById(R.id.memo_content);
        final EditText editTextContext = (EditText)findViewById(R.id.memo_context);

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

//                //잠시 얼림//
//                String title = editTextTitle.getText().toString();
//                System.out.println(title);
//
//                String context = editTextContext.getText().toString();
//                System.out.println(context);
//
//                Intent intent = new Intent(getApplication(), MainActivity.class);
//
//                Toast.makeText(getApplicationContext(), "메모가 저장 되었습니다.", Toast.LENGTH_LONG).show();
//
//                final Dictionary putobject= new Dictionary(title,"의미","없음");
//                putobject.setContext(context);
//
//                intent.putExtra("itemdata", putobject);
//
//                startActivity(intent);

                String title = editTextTitle.getText().toString();
                System.out.println(title);

                String context = editTextContext.getText().toString();
                System.out.println(context);

                Intent intent = new Intent(getApplication(), MainActivity.class);

                Toast.makeText(getApplicationContext(), "메모가 저장 되었습니다.", Toast.LENGTH_LONG).show();

                final Dictionary putobject= new Dictionary(title,"의미","없음");
                putobject.setContext(context);

                intent.putExtra("itemdata", putobject);

                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

//                Intent intent = new Intent(getApplication(), memoActivity.class);
//        Dictionary dict = mArrayList.get(position);
//        Toast.makeText(getApplicationContext(), dict.getId() + ' ' + dict.getEnglish() + ' ' + dict.getKorean(), Toast.LENGTH_LONG).show();
//        intent.putExtra("itemdata", dict);
//        startActivity(intent);
    }
}

