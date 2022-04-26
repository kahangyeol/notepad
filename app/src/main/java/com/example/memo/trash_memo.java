package com.example.memo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class trash_memo extends AppCompatActivity {
    TextView mFolder_name;
    String mtitle, content;
    int mbtn_id_t=0;
    int count = 0;
    int btn_id = 0, check = 1, mbtn_id = 0;
    String folder_nt;
    String value_save[] = new String[10000];
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash_memo);


//-----------------뒤로가기(꺽쇠괄호)클릭----------------------
        ImageButton imageButton10 = (ImageButton) findViewById(R.id.imageButton10);
        imageButton10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        check = intent.getExtras().getInt("check");
        SharedPreferences folder_name = getSharedPreferences("folder_t"+btn_id, Context.MODE_PRIVATE);
        mbtn_id = intent.getExtras().getInt("mbtn_id");
        btn_id = mbtn_id/100;

        //String folder_name = intent.getExtras().getString("folder_name");//폴더이름 가져오기

        folder_nt = intent.getExtras().getString("folder_name");
        if (folder_nt.length() >= 5) {
            folder_nt = folder_nt.substring(0, 4);
            folder_nt = folder_nt + "...";
        }

        mFolder_name = (TextView) findViewById(R.id.folder_name);
        mFolder_name.setText(folder_nt);
//            mbtn_id = intent.getExtras().getInt("mbtn_id");//클릭한 메모의 아이디
            mbtn_id_t = mbtn_id;//메모의 아이디를 받아옴
            count = intent.getExtras().getInt("count");
            btn_id = intent.getExtras().getInt("btn_id");//폴더의 아이디
            mbtn_id = intent.getExtras().getInt("mbtn_id");
            if(mbtn_id < 99){
                mbtn_id = btn_id*100+mbtn_id;
            }//아이디가 1인 폴더의 아이디가 1인 메모의 아이디 101
            mtitle = intent.getExtras().getString("mtitle");//메모제목 가져오기
            pref = getSharedPreferences("memo"+mbtn_id, Context.MODE_PRIVATE);
            content = pref.getString("content","");//메모내용 불러오기



            TextView content_et = (TextView) findViewById(R.id.content);
            TextView title_et = (TextView) findViewById(R.id.title);

            content_et.setText(content);
            title_et.setText(mtitle);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),trash.class);
        startActivity(intent);

    }
}