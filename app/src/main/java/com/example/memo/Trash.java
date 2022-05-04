package com.example.memo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memo.Room.AppDatabase;
import com.example.memo.Room.User;
import com.example.memo.recycle.RecyclerDecoration;
import com.example.memo.recycle.trash.TrashRecyclerViewAdapter;

import java.util.List;

public class Trash extends AppCompatActivity {
    public static Context mContext;
    int btn_id_f = 0;
    Button nButton[];
    Button nButton_f[];
    TextView folder_countMemo, memo_countMemo;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<User> users;

    int check;
    String backFolderTitle;
    String [] value_save = new String[100000];
    String [] value_save_f = new String[100000];
    int countMemo,mbtn_id,countMemoFolder,count;
    TrashRecyclerViewAdapter adapter;
    int nButtonId[] = new int[100000];
    int nButtonId_f[] = new int[100000];
    LinearLayout layout;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);

        mContext = this;

        initialized();
        count();

        Intent intent = getIntent();
        check = intent.getExtras().getInt("check");//0 = main , 1 = newFolder
        backFolderTitle = intent.getExtras().getString("folderTitle");

        nButton = new Button[10000];
        nButton_f = new Button[10000];

        pref = getSharedPreferences("trash", Context.MODE_PRIVATE);


        //mbtn_id = pref.getInt("mbtn_id",-1);
        layout = (LinearLayout) findViewById(R.id.linearLayout3);

//-----------------모두 되돌리기 클릭-----------------
        ImageButton rollback = (ImageButton) findViewById(R.id.rollback);
        rollback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.getItemCount() > 0) {
                    adapter.allRollback();
                }else{
                    Toast.makeText(Trash.this, "복원할 폴더 또는 메모가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
//-----------------뒤로가기(꺽쇠괄호)클릭----------------------
        ImageButton imageButton10 = (ImageButton) findViewById(R.id.btnBack);
        imageButton10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//-------------비우기----------------------------
        ImageButton clear = (ImageButton)findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users.clear();
                adapter.allClear();


                /*AlertDialog.Builder ad = new AlertDialog.Builder(trash.this);
                ad.setTitle("모두 삭제 하시겠습니까?");
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor edit = pref.edit();
                        for(int i = 0; i <countMemo; i++)
                            nButton[i].setVisibility(View.GONE);
                        for(int i = 0; i <countMemoFolder; i++)
                            nButton_f[i].setVisibility(View.GONE);
                        countMemo = 0;
                        countMemoFolder=0;
                        edit.putInt("countMemo",countMemo);
                        edit.putInt("countMemoFolder ",countMemoFolder);
                        edit.commit();

                    }
                });
                ad.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });*/
            }
        });


    }

    private void initialized() {
        recyclerView = findViewById(R.id.trashRecyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        adapter = new TrashRecyclerViewAdapter();

        users = AppDatabase.getInstance(this).userDao().trashGetAll();

        for(int i = 0; i < users.size(); i++){
            /*User user = users.get(i);
            if(user.getId() >= 1000 && user.getRoot() != null) {
            }*/
            User user = users.get(i);
            adapter.addItem(user);
        }
        RecyclerDecoration spaceDecoration = new RecyclerDecoration(-30);
        //리싸이클러뷰 간격조절
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.
                getContext(), new LinearLayoutManager(this).getOrientation());

        //리싸이클러뷰 구분선 생성
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addItemDecoration(spaceDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    //---------휴지통 개수----------
    private void count(){
        memo_countMemo = (TextView) findViewById(R.id.memo);
        count = adapter.getItemCount();
        memo_countMemo.setText("파일: " + (count));

    }

    String temp;
    String temp_f;

    private void delete(int i){
        /*int temp = i+1;
        SharedPreferences folder_name = getSharedPreferences("folder_t"+temp, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = folder_name.edit();

        edit.putInt("countMemo",0);

        edit.commit();*/
    }
    private void Rollback(int i){



        int countMemo_m;
        String folder_n;
        int btn_id = nButtonId[i]/100;
        SharedPreferences folder_name = getSharedPreferences("folder"+btn_id, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = folder_name.edit();

        countMemo_m = folder_name.getInt("countMemo",-1);

        edit.putString("button_name"+(countMemo_m-1),value_save[i]);

        folder_n = pref.getString("title","");
        edit.putString("title",folder_n);

        countMemo_m++;
        edit.putInt("countMemo",countMemo_m);

        SharedPreferences memo = getSharedPreferences("memo"+nButtonId[i],Context.MODE_PRIVATE);
        SharedPreferences.Editor memo_edit = memo.edit();
        String content = memo.getString("content", "");
        memo_edit.putString("content", content);
        nButton[i].setVisibility(View.GONE);
        countMemo--;

        edit.commit();
        count();
    }
    private void Rollback_f(int i){
        int countMemo_m;
        SharedPreferences sharedPreferences = getSharedPreferences("Save", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();

        countMemo_m=sharedPreferences.getInt("countMemo",-1);
        countMemo_m++;

        edit.putString("button_name"+countMemo_m,value_save_f[i]);
        edit.putInt("button_id"+countMemo_m,nButtonId_f[i]);

        edit.putInt("countMemo", countMemo_m);
        edit.commit();
        SharedPreferences folder_name = getSharedPreferences("folder"+nButtonId_f[i], Context.MODE_PRIVATE);
        SharedPreferences.Editor edit1 = folder_name.edit();

        edit1.putInt("del",0);
        edit1.putInt("countMemo",pref.getInt("save_countMemo"+(i+1),0));
        nButton_f[i].setVisibility(View.GONE);
        countMemoFolder--;
        edit1.commit();

        count();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(check == 0) {
            Intent intent;
            intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent;
            if(backFolderTitle.equals(""))
                intent = new Intent(getApplicationContext(), AllFile.class);
            else
                intent = new Intent(getApplicationContext(), NewFolder.class);
            intent.putExtra("folderTitle",backFolderTitle);
            startActivity(intent);
            finish();
        }
    }
}