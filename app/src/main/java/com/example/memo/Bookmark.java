package com.example.memo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Bookmark extends AppCompatActivity {
    SharedPreferences pref;
    int count = 0, mbtn_id, count_f = 0, btn_id_f;
    int[] nButtonId = new int[10000];
    int[] nButtonId_f = new int[10000];
    String[] value_save = new String[10000];
    String[] value_save_f = new String[10000];
    Button[] nButton = new Button[10000];
    Button[] nButton_f = new Button[10000];
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        pref = getSharedPreferences("star", Context.MODE_PRIVATE);
        count_f = pref.getInt("bm_count_f", 0);
        count = pref.getInt("bm_count", 0);


//-----------------뒤로가기(꺽쇠괄호)클릭----------------------
        ImageButton imageButton10 = (ImageButton) findViewById(R.id.imageButton10);
        imageButton10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loadButton();
        count();
    }

    private void loadButton() {
        layout = (LinearLayout) findViewById(R.id.linearLayout4);
        for (int i = 0; i < count; i++) {
            mbtn_id = pref.getInt("mbtn_id" + i, -1);
            value_save[i] = pref.getString("btn_name" + i, "");
            nButton[i] = new Button(this);
            nButton[i].setText(value_save[i]);
            nButton[i].setId(mbtn_id);
            layout.addView(nButton[i], new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT
            ));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            nButtonId[i] = nButton[i].getId();
            nButton[i].setBackgroundResource(R.drawable.btn_bg);
            nButton[i].setGravity(Gravity.CENTER_VERTICAL);
            nButton[i].setTextSize(20);
            nButton[i].setPadding(120, 0, 0, 0);
            registerForContextMenu(nButton[i]);

            Drawable img = getBaseContext().getResources().getDrawable(R.drawable.star);
            img.setBounds(0, 0, 85, 85);
            nButton[i].setCompoundDrawables(img, null, null, null);
            nButton[i].setPadding(70, 0, 0, 0);
        }
        Click_btn();
        for (int i = 0; i < count_f; i++) {
            layout = (LinearLayout) findViewById(R.id.linearLayout3);
            btn_id_f = pref.getInt("btn_id_f" + i, -1);
            value_save_f[i] = pref.getString("btn_name_f" + i, "");
            nButton_f[i] = new Button(this);
            nButton_f[i].setText(value_save_f[i]);
            nButton_f[i].setId(btn_id_f);
            layout.addView(nButton_f[i], new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT
            ));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            nButtonId_f[i] = nButton_f[i].getId();
            nButton_f[i].setBackgroundResource(R.drawable.btn_bg);
            nButton_f[i].setGravity(Gravity.CENTER_VERTICAL);
            nButton_f[i].setTextSize(20);
            nButton_f[i].setPadding(120, 0, 0, 0);
            registerForContextMenu(nButton_f[i]);

            Drawable img = getBaseContext().getResources().getDrawable(R.drawable.star);
            img.setBounds(0, 0, 85, 85);
            nButton_f[i].setCompoundDrawables(img, null, null, null);
            nButton_f[i].setPadding(70, 0, 0, 0);
        }
        Click_btn_f();
    }

    private void Click_btn() {
        for (int j = 0; j < count; j++) {
            int b_temp = j;
            String ttt = nButton[j].getText().toString();
            nButton[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //int resID = getResources().getIdentifier("Button" + i, "id", "com.example.memo");//    int resID = getResources().getIdentifier("com.androidside:id/Button"+i,null,null);
                    Intent intent = new Intent(getApplicationContext(), create_memo.class);
                    int btn_id = nButtonId[b_temp] / 100;
                    SharedPreferences pr = getSharedPreferences("folder" + btn_id, Context.MODE_PRIVATE);
                    String folder_n = pr.getString("title", "");

                    String title = nButton[b_temp].getText().toString();
                    // intent.putExtra("count",count);
                    intent.putExtra("folder_name", folder_n);
                    intent.putExtra("mbtn_id", nButtonId[b_temp]);//클릭한 메모 아이디
                    intent.putExtra("btn_id", btn_id);//폴더 아이디
                    intent.putExtra("mtitle", value_save[b_temp]);//메모제목
                    int check = 0;
                    intent.putExtra("check", check);
                    intent.putExtra("bm_check",1);
                    startActivity(intent);
                    finish();
                    save();
                }
            });
        }
    }

    private void Click_btn_f() {
        for(int i = 0; i < count_f; i++) {
            int temp = i;
            nButton_f[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), NewFolder.class);
                    String title = nButton_f[temp].getText().toString();
                    intent.putExtra("title", title);
                    intent.putExtra("btn_id", nButtonId_f[temp]);
                    intent.putExtra("bm_check",1);
                    startActivity(intent);
                    save();
                }
            });
        }
    }
    private void count() {
        TextView folder_count = (TextView) findViewById(R.id.folder);
        TextView memo_count = ( TextView) findViewById(R.id.memo);

        String memo_s = "메모: " + count + "개";
        String folder_s = "폴더: " + count_f + "개";

        folder_count.setText(folder_s);
        memo_count.setText(memo_s);
    }



    String temp;
    String temp_f;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        for (int i = 0; i < count; i++) {
            if (v == nButton[i]) {//---------------------------메모--------------------------------
                temp = nButton[i].getText().toString();
                menu.add(0, 1, 0, "즐겨찾기 해제");
                menu.add(0, 2, 0, "휴지통");
                menu.add(0, 3, 0, "상단에 고정");
                menu.add(0, 4, 0, "이름변경");
            }
        }
        for (int i = 0; i < count_f; i++) {//---------------------------폴더--------------------------------
            if (v == nButton_f[i]) {
                temp_f = nButton_f[i].getText().toString();
                menu.add(0, 5, 0, "즐겨찾기 해제");
                menu.add(0, 6, 0, "휴지통");
                menu.add(0, 7, 0, "상단에 고정");
                menu.add(0, 8, 0, "이름변경");
            }
        }
    }

    String name_temp;

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        for (int i = 0; i < count; i++) {
            if (temp == nButton[i].getText().toString()) {
                switch (item.getItemId()) {
                    case 1:
                        count--;
                        int c = count + i;
                        nButton[i].setVisibility(View.GONE);
                        while (i < count) {
                            int temp = i + 1;
                            nButton_f[i] = nButton_f[temp];
//                            nButton[i].setId(temp);
                            value_save[i] = value_save[temp];
                            nButtonId[i] = nButton[i].getId();

                            i++;
                        }
                        count();
                        save();
                        return true;
                    case 2:
                        return true;
                    case 3:
                        return true;
                    case 4:
                        return true;
                }

            }

        }
        for (int i = 0; i < count_f; i++) {
            if (temp_f == nButton_f[i].getText()) {
                switch (item.getItemId()) {
                    case 5:
                        SharedPreferences.Editor edit = pref.edit();
                        int d = 0;
                        for (int j = 0; j < count; j++) {
                            int btn_id = nButtonId[j] / 100;
                            if (btn_id == nButtonId[i]) {
                                nButton[j].setVisibility(View.GONE);
                                edit.putInt("mbtn_id"+j,0);
                                edit.putString("btn_name"+j,"");
                                edit.putInt("check"+j,0);
                                d++;
                            }
                        }
                        count = count-d;
                        delete(i);



                        edit.putInt("btn_id_f" + i, pref.getInt("btn_id_f" + (i + 1), 0));
                        edit.putString("btn_name_f" + i, pref.getString("btn_name_f" + (i + 1), ""));
                        edit.putInt("check_f" + i, 0);
                        count_f--;
                        edit.putInt("bm_count_f", count_f);
                        SharedPreferences main = getSharedPreferences("Save", Context.MODE_PRIVATE);
                        SharedPreferences.Editor e = main.edit();
                        for (int j = 0; j < main.getInt("count", -1); j++) {
//                            int a = main.getInt("button_id" + j, -1);
                            if (nButtonId_f[i] == main.getInt("button_id" + j, -1)) {
                                edit.putInt("check_f" + i, 0);
                                save();
                                count();
                                continue;
                            }
                        }
                        edit.commit();
                        nButton_f[i].setVisibility(View.GONE);
                        return true;
                    case 6:
                        count_f--;
                        int c = count_f + i;
                        nButton_f[i].setVisibility(View.GONE);
                        while (i < count_f) {
                            int temp = i + 1;
                            nButton_f[i] = nButton_f[temp];
//                            nButton[i].setId(temp);
                            value_save_f[i] = value_save_f[temp];
                            nButtonId_f[i] = nButton_f[i].getId();
                            /*if(count+i == c) {
                                delete(i);
                            }*/
                            i++;
                        }
                        count();
                        save();
                        return true;
                    case 7:
                        return true;
                    case 8:
                        AlertDialog.Builder ad = new AlertDialog.Builder(Bookmark.this);
                        ad.setTitle("폴더 이름 변경");
                        ad.setMessage("변경할 폴더 이름을 입력하세요");
                        int temp = i;
                        final EditText et = new EditText(Bookmark.this);
                        ad.setView(et);
                        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                value_save_f[temp] = et.getText().toString();
                                if (!(value_save_f[temp] == ""))
                                    nButton_f[temp].setText(value_save_f[temp]);
                                else {
                                    AlertDialog.Builder noname = new AlertDialog.Builder(Bookmark.this);
                                    noname.setTitle("폴더 이름을 입력하세요");
                                    //noname.setMessage("제목없는 폴더는 만들수 없습니다");
                                    noname.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    noname.show();
                                }
                                SharedPreferences main = getSharedPreferences("Save", Context.MODE_PRIVATE);
                                SharedPreferences.Editor e = main.edit();
                                for (int j = 0; j < main.getInt("count", -1); j++) {
                                    int a = main.getInt("button_id" + j, -1);
                                    if (nButtonId_f[temp] == a) {
                                        e.putString("button_name" + j, value_save_f[temp]);
                                        e.commit();
                                        save();
                                        continue;
                                    }
                                }
                            }

                        });
                        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        ad.show();


                        return true;

                }

            }

        }

        return true;

        //return super.onContextItemSelected(item);
    }

    private void delete(int i) {

    }

    private void save() {
        {
            Intent intent = getIntent();
            SharedPreferences star = getSharedPreferences("star", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = star.edit();

            edit.putInt("bm_count_f", count_f);
            edit.putInt("bm_count", count);
            for (int i = 0; i < count; i++) {
                edit.putString("btn_name" + i, value_save[i]);
                edit.putInt("btn_id" + i, nButtonId[i]);
                // edit.putInt("mbtn_id"+i,mbtn_id);//메모 아이디

//            edit.putInt("nButtonId"+i, nButtonId[i]);
            }
            for (int i = 0; i < count_f; i++) {
                edit.putInt("btn_id_f" + i, nButtonId_f[i]);
                edit.putString("btn_name_f" + i, value_save_f[i]);
            }
            edit.commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        save();
    }
}