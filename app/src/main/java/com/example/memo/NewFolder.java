package com.example.memo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RadioButton;
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
import com.example.memo.databinding.ActivityNewFolderBinding;
import com.example.memo.recycle.memo.MemoRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class NewFolder extends AppCompatActivity implements View.OnClickListener {
    private ActivityNewFolderBinding binding;
    public static Context mContext;
    List<User> users;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    MemoRecyclerAdapter adapter;
    ImageButton newMemo;// ID = newMemo
    ImageButton trash;
    List<Integer> nButtonId = new ArrayList<>();
    boolean passWordCheck, editCheck = true, fabCheck = false;
    int folderId;
    String folderTitle;
    SharedPreferences pref;
    private AdView mAdView;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private androidx.biometric.BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewFolderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        setOnClickListener();

        Intent intent = getIntent();
        folderTitle = intent.getExtras().getString("folderTitle");
        folderId = intent.getExtras().getInt("folderId");

        initialized(folderTitle);
        count();

        TextView folderTitleTextView = findViewById(R.id.title); //폴더 이름 텍스트뷰에 입력
        folderTitleTextView.setText(folderTitle);
        //-----------------뒤로가기(꺽쇠괄호)클릭----------------------
        ImageButton imageButton10 = (ImageButton) findViewById(R.id.btnBack);
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
                Intent intent = new Intent(getApplicationContext(), Create_memo.class);
                intent.putExtra("folderTitle", folderTitle);//폴더이름 전송
                intent.putExtra("memoId", adapter.getItemCount());
                intent.putExtra("check", false);
//                save();
                startActivity(intent);
                finish();
            }
        });

        trash = (ImageButton) findViewById(R.id.trash);
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Trash.class);
                intent.putExtra("check",1);
                intent.putExtra("folderTitle", folderTitle);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public void onClick(View view) {
        int getId = view.getId();
        if(getId == binding.fabMain.getId()){
            if(!fabCheck) {
                Animation fab_open = AnimationUtils.loadAnimation(mContext, R.anim.fab_open);
                binding.fabMain.setImageResource(R.drawable.ic_baseline_close_24);
                binding.fabStar.startAnimation(fab_open);
                binding.fabPin.startAnimation(fab_open);
                binding.fabLock.startAnimation(fab_open);
                binding.fabStar.setVisibility(View.VISIBLE);
                binding.fabPin.setVisibility(View.VISIBLE);
                binding.fabLock.setVisibility(View.VISIBLE);

                fabCheck = true;
            }else if(fabCheck){
                Animation fab_close = AnimationUtils.loadAnimation(mContext,R.anim.fab_close);
                binding.fabMain.setImageResource(R.drawable.ic_baseline_add_24);
                binding.fabStar.startAnimation(fab_close);
                binding.fabPin.startAnimation(fab_close);
                binding.fabLock.startAnimation(fab_close);
                binding.fabStar.setVisibility(View.INVISIBLE);
                binding.fabPin.setVisibility(View.INVISIBLE);
                binding.fabLock.setVisibility(View.INVISIBLE);

                fabCheck = false;
            }

        }else if(getId == binding.fabPin.getId()){
            ((MemoRecyclerAdapter)recyclerView.getAdapter()).selectPin(folderTitle);
        }else if(getId == binding.fabStar.getId()) {
            ((MemoRecyclerAdapter)recyclerView.getAdapter()).selectStar(folderTitle);
        }else if(getId == binding.fabLock.getId()){

        } else if(getId == binding.allCheck.getId()){
            ((MemoRecyclerAdapter)recyclerView.getAdapter()).All();
            binding.allCheck.setChecked(((MemoRecyclerAdapter)recyclerView.getAdapter()).isSelectedAll);

        }/*else if(getId == binding.search.getId()){
            showMenu(view);
        }*/
    }

    private void setOnClickListener() {
        binding.fabMain.setOnClickListener(this);
        binding.fabPin.setOnClickListener(this);
        binding.fabLock.setOnClickListener(this);
        binding.fabStar.setOnClickListener(this);
        binding.allCheck.setOnClickListener(this);
    }

    private void count() {
        TextView folderCount = (TextView) findViewById(R.id.memo_count);
        folderCount.setText("메모: " + (adapter.getItemCount()));
    }

    public void initialized(String folderTitle) {
        mContext = this;

        recyclerView = (RecyclerView) ((Activity) mContext).findViewById(R.id.memoRecyclerView);
        adapter = new MemoRecyclerAdapter(this, recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        users = AppDatabase.getInstance(this).userDao().getAllMemoRoot(folderTitle);

        for (int i = 0; i < users.size(); i++) {
            adapter.addItem(users.get(i));
            nButtonId.add(i);
        }
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        ImageButton edit = findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation ani = AnimationUtils.loadAnimation(mContext,R.anim.slide_in_right);
                RadioButton checkBox = findViewById(R.id.allCheck);
                if (editCheck) {
                    adapter.setItemViewType(MemoRecyclerAdapter.VIEWTYPE_EDIT);
                    recyclerView.startAnimation(ani);
                    binding.fabMain.setVisibility(View.VISIBLE);
                    binding.edit.setImageResource(R.drawable.editing);
                    checkBox.setVisibility(View.VISIBLE);
                    binding.selectMemo.setText("선택된 폴더: 0");

                    trash.setImageResource(R.drawable.trash_edit);
                    trash.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    editCheck = false;
                    ((MemoRecyclerAdapter)recyclerView.getAdapter()).keepItemView = new ArrayList<>();

                } else if(!editCheck){
                    adapter.setItemViewType(MemoRecyclerAdapter.VIEWTYPE_NORMAL);
                    recyclerView.startAnimation(ani);
                    binding.edit.setImageResource(R.drawable.edit);
                    binding.fabMain.setVisibility(View.GONE);
                    binding.fabMain.setImageResource(R.drawable.ic_baseline_add_24);
                    binding.fabPin.setVisibility(View.INVISIBLE);
                    binding.fabLock.setVisibility(View.INVISIBLE);
                    binding.fabStar.setVisibility(View.INVISIBLE);
                    checkBox.setVisibility(View.GONE);
                    binding.selectMemo.setText("");

                    trash.setImageResource(R.drawable.trash);
                    trash.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), Trash.class);
                            intent.putExtra("check",0);
                            startActivity(intent);
                            finish();
                        }
                    });
                    setId();
                    editCheck = true;
                    fabCheck = false;
                    binding.allCheck.setChecked(false);
                }
            }
        });

    }

    public boolean passWord(int id, int check ) { //check = 1 잠금설정 0 파일열기 2 휴지통에서 삭제
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new androidx.biometric.BiometricPrompt(this, executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                passWordCheck = fun_PassWordCheck("error",false);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                int passWord = AppDatabase.getInstance(mContext).userDao().loadPassWord(id, folderTitle);

                if(check == 0){
                    Intent intent = new Intent(getApplicationContext(), Create_memo.class);
//                intent.putExtra("root",user.getRoot());
//                intent.putExtra("memoId",user.getId());
                    intent.putExtra("check", true);
                    intent.putExtra("data", users.get(id));
                    startActivity(intent);
                    finish();
                } else if(check == 1) {
                    if (passWord == 0) {
                        passWordCheck = fun_PassWordCheck("잠금 설정이 완료되었습니다.", true);
                        AppDatabase.getInstance(mContext).userDao().updatePassWordOn(id, folderTitle);
                    } else if (passWord == 1) {
                        passWordCheck = fun_PassWordCheck("잠금 설정이 해제되었습니다.", true);
                        AppDatabase.getInstance(mContext).userDao().updatePassWordOff(id, folderTitle);
                    }
                    ((MemoRecyclerAdapter)recyclerView.getAdapter()).notifyDataSetChanged();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                passWordCheck = fun_PassWordCheck("fail",false);
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

    public void setId(){
        for(int i = 0; i < recyclerView.getAdapter().getItemCount(); i++){
            User user = ((MemoRecyclerAdapter)recyclerView.getAdapter()).userData.get(i);
            String folderTitle = user.getFolderTitle();
            AppDatabase.getInstance(this).userDao().updateId(i,folderTitle);
        }
        initialized(folderTitle);
        ((MemoRecyclerAdapter)recyclerView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onBackPressed(){
        if(editCheck) {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            initialized(folderTitle);
            editCheck = true;
            Animation ani = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);
            RadioButton checkBox = findViewById(R.id.allCheck);
            recyclerView.startAnimation(ani);

            binding.fabMain.setVisibility(View.GONE);
            binding.fabMain.setImageResource(R.drawable.ic_baseline_add_24);
            binding.fabPin.setVisibility(View.INVISIBLE);
            binding.fabLock.setVisibility(View.INVISIBLE);
            binding.fabStar.setVisibility(View.INVISIBLE);
            checkBox.setVisibility(View.GONE);

            trash.setImageResource(R.drawable.trash);
            trash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Trash.class);
                    intent.putExtra("check", 0);
                    startActivity(intent);
                    finish();
                }
            });
            setId();
            editCheck = true;
            fabCheck = false;
            binding.allCheck.setChecked(false);
        }
    }
}