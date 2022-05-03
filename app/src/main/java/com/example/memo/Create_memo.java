package com.example.memo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memo.Room.AppDatabase;
import com.example.memo.Room.User;
import com.example.memo.databinding.ActivityCreateMemoBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Create_memo extends AppCompatActivity implements View.OnClickListener {
    String memoTitle, content;
    ActivityCreateMemoBinding binding;
    List<User> users;
    int memoId = 0;
    int backHistory = 0;
    boolean check = true;
    EditText contentView, titleView;
    String root;
    //    SharedPreferences pref;
    AppDatabase db;
    Intent getIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateMemoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setOnClickListener();

        titleView = (EditText) findViewById(R.id.title);
        contentView = (EditText) findViewById(R.id.content);

        /*ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        if (check) {//이미 만들어진 메모 클릭 했을때
            User user = getIntent().getParcelableExtra("data"); //클릭한 user data 받아오기
            fileModify(user);

            memoId = user.getId();
            memoTitle = user.getMemoTitle();
            content = user.getContent();

            contentView.setText(content);
            titleView.setText(memoTitle);

        } else {//새로만들기 클릭 했을때
            memoId = getIntent.getExtras().getInt("memoId");
            root = getIntent.getExtras().getString("folderTitle");//폴더이름 가져오기
            /*Button success = (Button) findViewById(R.id.succes);
            success.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    success();
                }
            });*/
        }
    }

    private void init() {
        db = AppDatabase.getInstance(this);
        users = db.userDao().getAll();

        getIntent = getIntent();
        backHistory = getIntent.getExtras().getInt("backHistory", 0);
        check = getIntent.getExtras().getBoolean("check");//true = 이미만들어진 메모 불러오기 flase = 메모 새로만들기

        User user = users.get(memoId);
        binding.createTime.setText(user.createTime);
        binding.editTime.setText(user.editTime);
    }

    @Override
    public void onBackPressed() {
        success();
        super.onBackPressed();
    }

    public void success() {
        if (check) {
            User user = getIntent().getParcelableExtra("data");
            fileModify(user);
        } else {
            makeNewFile();
        }
        if (root.equals("")) { // 모든파일로
            Intent intent = new Intent(getApplicationContext(), AllFile.class);
            startActivity(intent);
        } else if (backHistory == 0) { // 메모의 폴더로
            Intent intent = new Intent(getApplicationContext(), NewFolder.class);
            intent.putExtra("folderTitle", root);
            startActivity(intent);
            finish();
        } else if (backHistory == 1) { // 휴지통으로
            Intent intent = new Intent(getApplicationContext(), Trash.class);
            intent.putExtra("folderTitle", root);
            startActivity(intent);
        }
    }

    public void makeNewFile() {
        memoTitle = titleView.getText().toString();
        if (memoTitle.equals("")) {//  메모 제목 안적으면 제목없음으로 저장
            memoTitle = "제목없음";
        }
        content = contentView.getText().toString();

        String createTime = getTime();
        User user = new User(memoId, null, 0, memoTitle, content, root, 0, 0, 0, createTime, createTime);
        db.userDao().insertAll(user);
    }

    public String getTime() {
        long nowTime = System.currentTimeMillis();
        Date date = new Date(nowTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm");
        return dateFormat.format(date);
    }

    public void fileModify(User user) {
        memoTitle = titleView.getText().toString();
        if (memoTitle.equals("")) {//  메모 제목 안적으면 제목없음으로 저장
            memoTitle = "제목없음";
        }
        content = contentView.getText().toString();
        db.userDao().updateMemo(memoTitle, content, user.getId(), user.getRoot());
        root = user.getRoot();
    }

    private void setOnClickListener() {
        binding.succes.setOnClickListener(this);
        binding.btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int getId = view.getId();

        if (binding.succes.getId() == getId) {
            success();
        } else if (binding.btnBack.getId() == getId) {
            onBackPressed();
        }
    }
}
