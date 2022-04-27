package com.example.memo;

//import kotlinx.android.synthetic.main.activity_main.*;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memo.Room.AppDatabase;
import com.example.memo.Room.User;
import com.example.memo.databinding.ActivityMainBinding;
import com.example.memo.recycle.RecyclerAdapter;
import com.example.memo.recycle.edit.EditRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    ActivityMainBinding binding;
    boolean editCheck = true, fabCheck = false;
    RecyclerView recyclerView;
    List<User> users;
    Button[] nButton;//버튼 생성관련
    LinearLayout layout;
    int count = 0;
    TextView folderCount; //폴더 개수
    Button btn_allfile; // btn_allfile 모든파일, bookmark 즐겨찾기
    ImageButton trash, btn01, btn_edit, search; //trash 휴지통, btn01 새로만들기, btn_edit 편집

    String value;
    String value_save[] = new String[10000];
    SharedPreferences pref;
    LinearLayoutManager linearLayoutManager;
    AppDatabase db;
    private RecyclerAdapter adapter;
    private EditRecyclerAdapter mAdapter; //edit전용
    public static Context mContext;
    ItemTouchHelper itemTouchHelper;
    private AdView mAdView;
    @Override
    public void onBackPressed() {
        if(editCheck) {
            finish();
        }else {
            initialized();
            editCheck = true;
            Animation ani = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);
            RadioButton checkBox = findViewById(R.id.allcheck);
            recyclerView.startAnimation(ani);

            binding.fabMain.setVisibility(View.GONE);
            binding.fabMain.setImageResource(R.drawable.ic_baseline_add_24);
            binding.fabPin.setVisibility(View.INVISIBLE);
            binding.fabStar.setVisibility(View.INVISIBLE);
            checkBox.setVisibility(View.GONE);

            trash.setImageResource(R.drawable.trash);
            trash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), trash.class);
                    intent.putExtra("check", 0);
                    startActivity(intent);
                    finish();
                }
            });
            setId();
            editCheck = true;
            fabCheck = false;
            binding.allcheck.setChecked(false);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        initialized();
        setOnClickListener();
        mContext = this;
        db = AppDatabase.getInstance(this);

        ItemTouchHelperCallback callback = new ItemTouchHelperCallback(adapter,this);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
       /* RecyclerDecoration spaceDecoration = new RecyclerDecoration(-30);
        //리싸이클러뷰 간격조절
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.
                getContext(), new LinearLayoutManager(mContext).getOrientation());
        //리싸이클러뷰 구분선 생성
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addItemDecoration(spaceDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);*/

        layout = findViewById(R.id.linearLayout3);
        nButton = new Button[10000];
        pref = getSharedPreferences("Save", Context.MODE_PRIVATE);

//-------------------버튼 카운트(갯수) 불러오기-------------------
//        count = db.userDao().getAll().size();

        count();
        Button button = new Button(this);//버튼 생성관련
        //-------------새로만들기 클릭-------------
        search = (ImageButton)findViewById(R.id.search);


        btn01 = (ImageButton) findViewById(R.id.newFolder);
        btn01.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.newfolder_dialog);
                showDialogNewFolder(dialog);
            }
        });
        count();
        //-------------모든파일 클릭-------------
        btn_allfile = (Button) findViewById(R.id.Button1);
        btn_allfile.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), allFile.class);
                        Button folder = (Button) findViewById(R.id.Button1);
                        String folder_name = (folder.getText().toString());
                        intent.putExtra("folder_name", folder_name);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        //-------------------휴지통-------------------
        trash = (ImageButton) findViewById(R.id.trash);
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), trash.class);
                intent.putExtra("check",0);
                startActivity(intent);
                finish();
            }
        });

    }

/*
    private void initHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;
                Drawable mark, background;
                int markMargin;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    itemView = viewHolder.itemView;
                    mark = ContextCompat.getDrawable(mContext,R.drawable.pin);
                    mark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                    markMargin = (int) mContext.getResources().getDimension(R.dimen.ic_remove_margin);

                    //왼쪽 스와이프   dX = x축으로 얼마나 이동(스와이프)했는지
                    if(dX < 1){
                        background = new ColorDrawable(Color.parseColor("#3B82F7"));
                        background.setBounds(itemView.getRight()+(int) dX, itemView.getTop(),itemView.getRight(),itemView.getBottom());
                        background.draw(c);

                        int itemHeight = itemView.getBottom() - itemView.getTop(); // Item 높이
                        int markWidth = mark.getIntrinsicWidth(); // Intrinsic: 본질적 - xMark 의 실제 길이
                        int markHeight = mark.getIntrinsicHeight();

                        int markLeft = itemView.getRight() - markMargin - markWidth;
                        int markRight = itemView.getRight() - markMargin;
                        int markTop = itemView.getTop() + (itemHeight - markHeight) / 2;
                        int markBottom = markTop + markHeight;
                        mark.setBounds(markLeft, markTop, markRight, markBottom);
                        mark.draw(c);
                    } else {
                        background = new ColorDrawable(Color.parseColor("#FFFF1A1A"));
                        background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft()+(int)dX, itemView.getBottom());
                        background.draw(c);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }
*/


    //========================================================함수========================================================
    public void showMenu(View v)
    {
        //with icon
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(this);// to implement on click event on items of menu
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.list_menu, popup.getMenu());
        popup.show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popup.setForceShowIcon(true);
        }

        popup.getMenu().findItem(R.id.delete);

        /*MenuPopupHelper menuHelper = new MenuPopupHelper(this, (MenuBuilder) menu.getMenu(), overflowImageView);
        menuHelper.setForceShowIcon(true);
        menuHelper.show();*/
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
                binding.fabStar.setVisibility(View.VISIBLE);
                binding.fabPin.setVisibility(View.VISIBLE);

                fabCheck = true;
            }else if(fabCheck){
                Animation fab_close = AnimationUtils.loadAnimation(mContext,R.anim.fab_close);
                binding.fabMain.setImageResource(R.drawable.ic_baseline_add_24);
                binding.fabStar.startAnimation(fab_close);
                binding.fabPin.startAnimation(fab_close);
                binding.fabStar.setVisibility(View.INVISIBLE);
                binding.fabPin.setVisibility(View.INVISIBLE);

                fabCheck = false;
            }

        }else if(getId == binding.fabPin.getId()){
            ((RecyclerAdapter)recyclerView.getAdapter()).selectPin();
        }else if(getId == binding.fabStar.getId()) {
            ((RecyclerAdapter)recyclerView.getAdapter()).selectStar();
        }else if(getId == binding.allcheck.getId()){
            ((RecyclerAdapter)recyclerView.getAdapter()).All();
            binding.allcheck.setChecked(((RecyclerAdapter)recyclerView.getAdapter()).isSelectedAll);

        }/*else if(getId == binding.search.getId()){
            showMenu(view);
        }*/
    }





    private void initialized() {
        db = AppDatabase.getInstance(this);
        recyclerView = (RecyclerView) findViewById(R.id.mainRecyclerView);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        users = AppDatabase.getInstance(this).userDao().getAllFolder();

        adapter = new RecyclerAdapter(this,recyclerView);
        recyclerView.setAdapter(adapter);

        for(int i = 0; i < users.size(); i++) {
            adapter.addItem(users.get(i));
        }
        recyclerView.setBackgroundColor(Color.TRANSPARENT);



//        setId();
//        ItemTouchHelperCallback callback = new ItemTouchHelperCallback((ItemTouchHelperCallback.OnItemMoveListener) adapter);
//        itemTouchHelper = new ItemTouchHelper(callback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);


        btn_edit = (ImageButton) findViewById(R.id.edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation ani = AnimationUtils.loadAnimation(mContext,R.anim.slide_in_right);
                RadioButton checkBox = findViewById(R.id.allcheck);
                if (editCheck) {
                    adapter.setItemViewType(RecyclerAdapter.VIEWTYPE_EDIT);
                    recyclerView.startAnimation(ani);
                    checkBox.setVisibility(View.VISIBLE);
                    binding.fabMain.setVisibility(View.VISIBLE);
                    binding.edit.setImageResource(R.drawable.editing);

                    trash.setImageResource(R.drawable.trash_edit);
                    trash.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Dialog dialog = new Dialog(MainActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.trash_dialog);
                            showDialogTrash(dialog);
                            /*dialog.setTitle("폴더삭제");
                            dialog.setMessage("하위 파일까지 모두 영구 삭제되며\n되돌릴 수 없습니다.\n정말 삭제하시겠습니까?");
                            dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((RecyclerAdapter)recyclerView.getAdapter()).selectDel();
                                }
                            });
                            dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            dialog.show();*/
                        }
                    });
                    editCheck = false;
                    ((RecyclerAdapter)recyclerView.getAdapter()).keepItemView = new ArrayList<>();

                } else if(!editCheck){
                    adapter.setItemViewType(RecyclerAdapter.VIEWTYPE_NORMAL);
                    recyclerView.startAnimation(ani);
                    binding.edit.setImageResource(R.drawable.edit);
                    binding.fabMain.setVisibility(View.GONE);
                    binding.fabMain.setImageResource(R.drawable.ic_baseline_add_24);
                    binding.fabPin.setVisibility(View.INVISIBLE);
                    binding.fabStar.setVisibility(View.INVISIBLE);
                    checkBox.setVisibility(View.GONE);

                    trash.setImageResource(R.drawable.trash);
                    trash.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), trash.class);
                            intent.putExtra("check",0);
                            startActivity(intent);
                            finish();
                        }
                    });
                    setId();
                    editCheck = true;
                    fabCheck = false;
                    binding.allcheck.setChecked(false);
                }
            }
        });
    }

    void showDialogTrash(Dialog dialog){
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView trashTxt = dialog.findViewById(R.id.trash_txt);
        Spannable span = (Spannable) trashTxt.getText();
        span.setSpan(new StyleSpan(Typeface.BOLD),0,5, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        span.setSpan(new AbsoluteSizeSpan(70),0,5, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        dialog.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RecyclerAdapter)recyclerView.getAdapter()).selectDel();
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.noBtn).setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               dialog.dismiss();
           }
        });
    }

    private void showKeyboard(EditText et){
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        et.requestFocus();
    }

    private void showDialogNewFolder(Dialog dialog){

        EditText et = dialog.findViewById(R.id.et_folderName);
        Button yesBtn = dialog.findViewById(R.id.yesBtn);
        TextView trashTxt = dialog.findViewById(R.id.newFolder_txt);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Spannable span = (Spannable) trashTxt.getText();
        span.setSpan(new StyleSpan(Typeface.BOLD),0,8, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        span.setSpan(new AbsoluteSizeSpan(70),0,8, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        dialog.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value = et.getText().toString();
                if (!value.equals("")) {
                    String s = et.getText().toString();
                    System.out.println(s);
                    users = AppDatabase.getInstance(mContext).userDao().getAll();
//                             adapter.addItems((ArrayList)users);
                    pushButton();
                    count();
//                            save();
                }
                ((RecyclerAdapter) recyclerView.getAdapter()).isSelectedAll = false;
                binding.allcheck.setChecked(false);
                ((RecyclerAdapter) recyclerView.getAdapter()).notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();

        showKeyboard(et);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        dialog.findViewById(R.id.noBtn).setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               dialog.dismiss();
           }
        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                yesBtn.setEnabled(!(et.getText().toString().isEmpty()));

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setOnClickListener(){
        binding.fabMain.setOnClickListener(this);
        binding.fabPin.setOnClickListener(this);
        binding.fabStar.setOnClickListener(this);
        binding.allcheck.setOnClickListener(this);
//        binding.search.setOnClickListener(this);
    }

    private void pushButton() {
        db = AppDatabase.getInstance(this);

        Drawable img = getBaseContext().getResources().getDrawable(R.drawable.folder);
        img.setBounds(0, 0, 100, 100);

        User user = new User(adapter.getItemCount(),value,0,null,null,null,0,0,0);
        db.userDao().insertAll(user);
        adapter.addItem(user);
        count ++;
    }

    //------------폴더 삭제시 폴더내용 리셋-----------

    private void count() {
        folderCount = (TextView) findViewById(R.id.folder_count);
        folderCount.setText("폴더: " + (adapter.getItemCount()));
        count = adapter.getItemCount();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


    public void setId(){
        for(int i = 0; i < recyclerView.getAdapter().getItemCount(); i++){
            User user = ((RecyclerAdapter)recyclerView.getAdapter()).userData.get(i);
            String folderTitle = user.getFolderTitle();
            AppDatabase.getInstance(this).userDao().updateId(i,folderTitle);
        }
        initialized();
        ((RecyclerAdapter)recyclerView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int getId = item.getItemId();
        switch (getId){
            case R.id.star:
                break;
            case R.id.delete:
                break;
            case R.id.pin:
                break;
            case R.id.update:
                break;
        }
        return false;
    }
}
