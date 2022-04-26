package com.example.memo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class memo extends AppCompatActivity {
    String folder_name, mtitle,content;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        TextView folder_name_tv,mtitle_tv, content_tv;
        Intent intent = getIntent();
        int btn_id;
        String f_title, m_title;

        btn_id = intent.getExtras().getInt("btn_id");//클릭한 버튼의 아이디를 가져옴

        pref = getSharedPreferences("memo"+btn_id,Context.MODE_PRIVATE);

        content = pref.getString("content","");

        f_title = intent.getExtras().getString("folder_name"); //폴더명
        folder_name = f_title;

        folder_name_tv = (TextView)findViewById(R.id.folder_name); //폴더명 텍스트뷰

        if (f_title.length() >= 5) {
            f_title=f_title.substring(0, 4);
            f_title = f_title + "...";
        }
        folder_name_tv.setText(f_title);

        m_title = intent.getExtras().getString("mtitle");//메모제목
        mtitle = m_title;

        mtitle_tv = (TextView)findViewById(R.id.title_memo);//메모제목 텍스트뷰뷰

        if (m_title.length() >= 5) {
            m_title=m_title.substring(0, 4);
            m_title = m_title + "...";
        }
        mtitle_tv.setText(m_title);

        content_tv = (TextView)findViewById(R.id.content);
        content_tv.setText("\n"+content);

        RelativeLayout memo_main = (RelativeLayout)findViewById(R.id.memo_main);
        memo_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),create_memo.class);
                int check = 0;

                intent.putExtra("check",check);
                intent.putExtra("content",content);
                intent.putExtra("folder_name",folder_name);
                intent.putExtra("mtitle",mtitle);
                startActivity(intent);
            }
        });
    }
}