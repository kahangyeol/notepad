package com.example.memo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memo.Room.AppDatabase;
import com.example.memo.Room.User;
import com.example.memo.recycle.memo.MemoRecyclerAdapter;

import java.util.List;

public class create_memo extends AppCompatActivity {
    TextView mFolder_name;
    String memoTitle, content;
    MemoRecyclerAdapter adapter;
    List<User> users;
    int mbtn_id_t = 0;
    int count = 0;
    int btn_id = 0, mbtn_id = 0;
    int backHistory = 0;
    boolean check = true, overlap = true;
    EditText des;
    EditText contentView, titleView;
    String root;
    String value_save[] = new String[10000];
    SharedPreferences pref;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memo);
        adapter = new MemoRecyclerAdapter();
        db = AppDatabase.getInstance(this);
        users = db.userDao().getAll();

        titleView = (EditText) findViewById(R.id.title);
        contentView = (EditText) findViewById(R.id.content);

        Intent intent = getIntent();
        backHistory = intent.getExtras().getInt("backHistory", 0);
        /*if (folder_name.length() >= 5) {
            folder_name = folder_name.substring(0, 4);
            folder_name = folder_name + "...";
        }*/
        check = intent.getExtras().getBoolean("check");//true = 이미만들어진 메모 불러오기 flase = 메모 새로만들기

        ImageButton imageButton10 = (ImageButton) findViewById(R.id.imageButton10);
        imageButton10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (check) {//이미 만들어진 메모 클릭 했을때
            User user = getIntent().getParcelableExtra("data"); //클릭한 user data 받아오기
            fileModify(user);


            count = user.getId();
            memoTitle = user.getMemoTitle();
            content = user.getContent();

            contentView.setText(content);
            titleView.setText(memoTitle);

            Button succes = (Button) findViewById(R.id.succes);
            succes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    success();
                    /*if(root.equals("")){ // root ""면 모든파일로
                        Intent intent = new Intent(getApplicationContext(), allFile.class);
                        startActivity(intent);
                        finish();
                    }
                    Intent intent = new Intent(getApplicationContext(), NewFolder.class);
                    intent.putExtra("folderTitle", user.getRoot());
                    startActivity(intent);
                    finish();*/
                }
            });
        } else if (!check) {//새로만들기 클릭 했을때
            count = intent.getExtras().getInt("memoId");
            root = intent.getExtras().getString("folderTitle");//폴더이름 가져오기
            Button succes = (Button) findViewById(R.id.succes);
            succes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    success();
                    //                    makeNewFile();

                    /*Intent intent = new Intent(getApplicationContext(), NewFolder.class);
                    intent.putExtra("folderTitle", root);
                    startActivity(intent);
                    finish();*/
                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        success();
        super.onBackPressed();
    }

    public void success(){
        if (check) {
            User user = getIntent().getParcelableExtra("data");
            fileModify(user);
        } else if (!check) {
            makeNewFile();
        }
        if (root.equals("")) { // 모든파일로
            Intent intent = new Intent(getApplicationContext(), allFile.class);
            startActivity(intent);
        } else if (backHistory == 0) { // 메모의 폴더로
            Intent intent = new Intent(getApplicationContext(), NewFolder.class);
            intent.putExtra("folderTitle", root);
            startActivity(intent);
            finish();
        } else if (backHistory == 1) { // 휴지통으로
            Intent intent = new Intent(getApplicationContext(), trash.class);
            intent.putExtra("folderTitle", root);
            startActivity(intent);
        }
    }

    public void makeNewFile(){
        memoTitle = titleView.getText().toString();
        if (memoTitle.equals("")) {//  메모 제목 안적으면 제목없음으로 저장
            memoTitle = "제목없음";
        }
        content = contentView.getText().toString();
        User user = new User(count, null, 0, memoTitle, content, root, 0, 0, 0);
        db.userDao().insertAll(user);
    }

    public void fileModify(User user){
        memoTitle = titleView.getText().toString();
        if (memoTitle.equals("")) {//  메모 제목 안적으면 제목없음으로 저장
            memoTitle = "제목없음";
        }
        content = contentView.getText().toString();
        db.userDao().updateMemo(memoTitle, content, user.getId(), user.getRoot());
        root = user.getRoot();
    }
}
