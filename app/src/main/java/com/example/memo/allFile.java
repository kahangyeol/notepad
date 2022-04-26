package com.example.memo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memo.Room.AppDatabase;
import com.example.memo.Room.User;
import com.example.memo.recycle.RecyclerDecoration;
import com.example.memo.recycle.memo.MemoRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class allFile extends AppCompatActivity {
    public static Context mContext;
    List<User> users;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    MemoRecyclerAdapter adapter;
    //리사이클러뷰
    ImageButton newMemo;// ID = newMemo
    LinearLayout layout;// ID = memo_list
    //View
//    int nButtonId[] = new int[100000];
    List<Integer> nButtonId = new ArrayList<>();
    int memoId = 0;
    int[] star = new int[10000];
    int count = 0;
    String value, folderTitle;
    String[] value_save = new String[100000];
    SharedPreferences pref;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_folder);
        mContext = this;

        initialized();
        count();


        /*for (int i = 0; i < count; i++) {
            SharedPreferences s = getSharedPreferences("star", Context.MODE_PRIVATE);
            star[i] = s.getInt("check" + i, 0);
            if (star[i] == 1) {
                Drawable img = getBaseContext().getResources().getDrawable(R.drawable.star);
                img.setBounds(0, 0, 85, 85);
                nButton[i].setCompoundDrawables(img, null, null, null);
                nButton[i].setPadding(70, 0, 0, 0);
            }
        }*/

        /*TextView folderTitleTextView = findViewById(R.id.title); //폴더 이름 텍스트뷰에 입력
        folderTitleTextView.setText(folderTitle);*/
        //-----------------뒤로가기(꺽쇠괄호)클릭----------------------
        ImageButton imageButton10 = (ImageButton) findViewById(R.id.imageButton10);
        imageButton10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // ---------------------새로만들기 클릭--------------------------
        newMemo = findViewById(R.id.newMemo);
        newMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count();
                Intent intent = new Intent(getApplicationContext(), create_memo.class);
                intent.putExtra("folderTitle", "");//폴더이름 전송
                intent.putExtra("memoId", count);
                intent.putExtra("check",false);
//                save();
                startActivity(intent);
                finish();
            }
        });

        ImageButton trash;
        trash = (ImageButton) findViewById(R.id.trash);
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), trash.class);
                intent.putExtra("check",1);
                intent.putExtra("folderTitle", folderTitle);
                startActivity(intent);
                finish();
            }
        });
    }

    private void count() {
        TextView folderCount = (TextView) findViewById(R.id.memo_count);
        folderCount.setText("메모: " + (adapter.getItemCount()));
        count = adapter.getItemCount();
    }

    public void initialized() {
        recyclerView = (RecyclerView) ((Activity)mContext).findViewById(R.id.memoRecyclerView);
        adapter = new MemoRecyclerAdapter();
        linearLayoutManager = new LinearLayoutManager(this);
        users = AppDatabase.getInstance(this).userDao().getAllMemo();

        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).trash == 0) {
                adapter.addItem(users.get(i));
                nButtonId.add(i);
            }
        }
        count = adapter.getItemCount();
        RecyclerDecoration spaceDecoration = new RecyclerDecoration(-30);
        //리싸이클러뷰 간격조절
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(this).getOrientation());

        //리싸이클러뷰 구분선 생성
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addItemDecoration(spaceDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

}