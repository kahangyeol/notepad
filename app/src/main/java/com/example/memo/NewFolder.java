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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memo.Room.AppDatabase;
import com.example.memo.Room.User;
import com.example.memo.recycle.memo.MemoRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class NewFolder extends AppCompatActivity {
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
    boolean passWodrCheck;
    int count = 0;
    int folderId;
    String value, folderTitle;
    String[] value_save = new String[100000];
    SharedPreferences pref;
    AppDatabase db;
    private AdView mAdView;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private androidx.biometric.BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_folder);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mContext = this;

        Intent intent = getIntent();
        folderTitle = intent.getExtras().getString("folderTitle");
        folderId = intent.getExtras().getInt("folderId");

        initialized(folderTitle);
        count();

        TextView folderTitleTextView = findViewById(R.id.title); //폴더 이름 텍스트뷰에 입력
        folderTitleTextView.setText(folderTitle);
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
                intent.putExtra("folderTitle", folderTitle);//폴더이름 전송
                intent.putExtra("memoId", adapter.getItemCount());
                intent.putExtra("check", false);
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
    }

    public void initialized(String folderTitle) {
        recyclerView = (RecyclerView) ((Activity) mContext).findViewById(R.id.memoRecyclerView);
        adapter = new MemoRecyclerAdapter(this, recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        users = AppDatabase.getInstance(this).userDao().getAllMemoRoot(folderTitle);

        for (int i = 0; i < users.size(); i++) {
            adapter.addItem(users.get(i));
            nButtonId.add(i);
        }
        /*RecyclerDecoration spaceDecoration = new RecyclerDecoration(-30);
        //리싸이클러뷰 간격조절
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(this).getOrientation());

        //리싸이클러뷰 구분선 생성
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addItemDecoration(spaceDecoration);*/
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }

    public boolean passWord(int id, int check ) { //check = 1 잠금설정 0 파일열기 2 휴지통에서 삭제
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new androidx.biometric.BiometricPrompt(this, executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                passWodrCheck = fun_PassWordCheck("error",false);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                int passWord = AppDatabase.getInstance(mContext).userDao().loadPassWord(id, folderTitle);

                if(check == 0){
                    Intent intent = new Intent(getApplicationContext(), create_memo.class);
//                intent.putExtra("root",user.getRoot());
//                intent.putExtra("memoId",user.getId());
                    intent.putExtra("check", true);
                    intent.putExtra("data", users.get(id));
                    startActivity(intent);
                    finish();
                } else if(check == 1) {
                    if (passWord == 0) {
                        passWodrCheck = fun_PassWordCheck("잠금 설정이 완료되었습니다.", true);
                        AppDatabase.getInstance(mContext).userDao().updatePassWordOn(id, folderTitle);
                    } else if (passWord == 1) {
                        passWodrCheck = fun_PassWordCheck("잠금 설정이 해제되었습니다.", true);
                        AppDatabase.getInstance(mContext).userDao().updatePassWordOff(id, folderTitle);
                    }
                    ((MemoRecyclerAdapter)recyclerView.getAdapter()).notifyDataSetChanged();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                passWodrCheck = fun_PassWordCheck("fail",false);
            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("지문인증").setSubtitle("인증해").setNegativeButtonText("취소").setDeviceCredentialAllowed(false).build();

        biometricPrompt.authenticate(promptInfo);

        return true;
    }
    public boolean fun_PassWordCheck(String message, boolean passWordCheck){
        Toast.makeText(NewFolder.this, message, Toast.LENGTH_SHORT).show();
        return passWordCheck;
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}