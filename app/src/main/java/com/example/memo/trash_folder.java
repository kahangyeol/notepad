package com.example.memo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class trash_folder extends AppCompatActivity {
    Button nButton [];
    LinearLayout layout;
    int btn_id,count;
    String folder_n;
    SharedPreferences pref;
    String [] value_save = new String[100000];
    int [] nButtonId = new int[10000];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash_folder);
        TextView title_tv;
        String title;


        nButton = new Button[10000];
        layout = (LinearLayout) findViewById(R.id.memo_list);
        Intent intent = getIntent();

        btn_id = intent.getExtras().getInt("btn_id");
        pref = getSharedPreferences("folder_t"+btn_id, Context.MODE_PRIVATE);

        count = pref.getInt("count",-1);
        loadButton();
        title = intent.getExtras().getString("title");
        folder_n = title;
        title_tv = findViewById(R.id.title);
        if (title.length() >= 5) {
            title=title.substring(0, 4);
            title = title + "...";
        }
        title_tv.setText(title);
        //-----------------뒤로가기(꺽쇠괄호)클릭----------------------
        ImageButton imageButton10 = (ImageButton) findViewById(R.id.imageButton10);
        imageButton10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadButton(){
        for (int i = 0; i < count; i++) {
            value_save[i] = pref.getString("button_name" + i, "");
            nButton[i] = new Button(this);
            nButton[i].setText(value_save[i]);
            nButton[i].setId(i + 1);
            layout.addView(nButton[i], new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT
            ));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            nButtonId[i] = nButton[i].getId();
            nButton[i].setBackgroundResource(R.drawable.btn_bg);
            nButton[i].setGravity(Gravity.CENTER_VERTICAL);
            nButton[i].setTextSize(20);
            nButton[i].setPadding(120, 0, 0, 0);
            registerForContextMenu(nButton[i]);

            Drawable img = getBaseContext().getResources().getDrawable(R.drawable.notebook);
            img.setBounds(0, 0, 85, 85);
            nButton[i].setCompoundDrawables(img, null, null, null);
            nButton[i].setPadding(70, 0, 0, 0);
        }
        Click_btn();
    }
    private void Click_btn(){
        for (int j = 0; j < count; j++) {
            int b_temp = j;
            String ttt = nButton[j].getText().toString();
            nButton[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //int resID = getResources().getIdentifier("Button" + i, "id", "com.example.memo");//    int resID = getResources().getIdentifier("com.androidside:id/Button"+i,null,null);
                    Intent intent = new Intent(getApplicationContext(), trash_memo.class);
                    String title = nButton[b_temp].getText().toString();
                    intent.putExtra("count",count);
                    intent.putExtra("folder_name", folder_n);
                    intent.putExtra("mbtn_id",nButtonId[b_temp]);//클릭한 메모 아이디
                    intent.putExtra("btn_id",btn_id);//폴더 아이디
                    intent.putExtra("mtitle",value_save[b_temp]);//메모제목
                    startActivity(intent);
                    finish();
                    save();
                }
            });
        }
    }
    public void save() {
        SharedPreferences folder_name = getSharedPreferences("folder_t" + btn_id, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = folder_name.edit();

        edit.putInt("count", count);
        edit.putString("title", folder_n);
        for (int i = 0; i < count; i++) {
            edit.putString("button_name" + i, value_save[i]);
//            edit.putInt("nButtonId"+i, nButtonId[i]);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        save();
        finish();
    }
}