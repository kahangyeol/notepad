package com.example.memo.recycle.edit;

import androidx.appcompat.app.AppCompatActivity;

public class FreeAlign extends AppCompatActivity {

} /*{
    private ItemTouchHelper helper;
    private EditRecyclerAdapter mAdapter;
    CheckBox allcheck;
    Button succes;
    public static Context editContext;
    int count;
    *//*int checkbox_count;
    String btn_name[] = new String[checkbox_count];*//*


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_align);
        editContext = this;

        allcheck = (CheckBox) findViewById(R.id.allcheck);//모두선택 기능


        succes = (Button) findViewById(R.id.succes);
        succes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });


//-----------------뒤로가기(꺽쇠괄호)클릭----------------------
        ImageButton imageButton10 = (ImageButton) findViewById(R.id.imageButton10);
        imageButton10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler);

        init();
        getData();
        mAdapter.notifyDataSetChanged();

        //------------모두선택-----------------
        allcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(allcheck.isChecked()){
//                   mAdapter.selectAll();
               }else{

//                   mAdapter.unselectall();
               }
            }
        });
///--------------------완료----------------------
        Button succes = (Button)findViewById(R.id.succes);

        succes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
//--------------------삭제----------------------
        ImageButton trash = (ImageButton) findViewById(R.id.cut);

        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                trash();
//                mAdapter.removeItems();
            }
        });
    }
    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new EditRecyclerAdapter();
        recyclerView.setAdapter(mAdapter);
        helper = new ItemTouchHelper(new ItemTouchHelperCallback(mAdapter));
        helper.attachToRecyclerView(recyclerView);

    }

    private void getData() {
        Intent intent = getIntent();
//        List<String> listTitle = Arrays.asList("국화","사막","수국");
        int count = intent.getExtras().getInt("count");
        String btn_name[] = new String[count];
        List<String> listTitle = null;
        for (int i = 0; i < count; i++) {
            btn_name[i] = intent.getExtras().getString("nbtn_name"+i);
//            btn_name[i] = intent.getExtras().getString("btn_name" + i);
            listTitle = Arrays.asList(btn_name[i]);
            User user = new User(0,"",0,"","","",0,0,0);
            user.setFolderTitle(listTitle.get(i));
            mAdapter.addItem(user);
        }
        mAdapter.notifyDataSetChanged();
        *//*for(int i = 0;i<listTitle.size();i++){
            Data data = new Data();
            data.setTitle(listTitle.get(i));
            adapter.addItem(data);
        }*//*
    }
    public void trash(){
        SharedPreferences main = getSharedPreferences("Save", Context.MODE_PRIVATE);
        SharedPreferences trash = getSharedPreferences("trash", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = trash.edit();
        SharedPreferences.Editor edit = main.edit();
        for (int i = 0; i < main.getInt("count", -1); i++) {
//                            int a = main.getInt("button_id" + j, -1);
            Data data = new Data();
            if (data.gettitle().equals(main.getInt("button_id" + i, -1))) {

                edit.putInt("check_f" + i, 0);
                save();
                continue;
            }
        }
    }


    private void save(){
        SharedPreferences pref = getSharedPreferences("Save", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();

        int temp_id = pref.getInt("button_id"+0,0);
        String tmep_name = pref.getString("button_name"+0,"");

        for(int i = 0; i < count; i++){

        }
    }

}*/