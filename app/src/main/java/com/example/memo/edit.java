package com.example.memo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.memo.recycle.edit.freeAlign;

import java.util.ArrayList;

public class edit extends AppCompatActivity {
    ListView listView;
    ArrayList<String>list;
    ArrayAdapter<String>adapter;
    CheckBox allcheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        allcheck = (CheckBox) findViewById(R.id.allcheck);//모두선택 기능
        listView  = (ListView) findViewById(R.id.listView);

        Intent intent = getIntent();

        int chekcBox_count = intent.getExtras().getInt("checkBox_count");
        int count = intent.getExtras().getInt("count");

        String btn_name[] = new String[chekcBox_count];
        String nbtn_name[] = new String[count];

        list = new ArrayList<String>();

        /*for(int i = 1; i<chekcBox_count;i++) {
            btn_name[i] = intent.getExtras().getString("btn_name"+i);
            list.add(btn_name[i]);
        }*/
        for(int i = 0; i<count;i++) {
            nbtn_name[i] = intent.getExtras().getString("nbtn_name"+i);
            list.add(nbtn_name[i]);
        }

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,list);
        listView.setAdapter(adapter);
        listView.setDivider(new ColorDrawable(Color.BLACK));
        listView.setDividerHeight(3);

        //모두선택
        allcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allcheck.isChecked()) {
                    for (int i = 0; i < listView.getCount(); i++)
                        listView.setItemChecked(i, true);
                }else{
                    for (int i = 0; i < listView.getCount(); i++)
                        listView.setItemChecked(i, false);
                }
            }
        });

        Button btn_free = (Button) findViewById(R.id.free);
        btn_free.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), freeAlign.class);
//                        String btn_name[] = new String[chekcBox_count];
                        for(int i = 0; i<count; i++){
                            intent.putExtra("btn_name"+i,nbtn_name[i]);
                        }
                        intent.putExtra("count",count);
                        startActivity(intent);
                    }
                }
        );


    }
}