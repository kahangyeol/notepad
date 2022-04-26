package com.example.memo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Arrays;

public class memo1 extends AppCompatActivity {
    TextView mFile_name, mFolder_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo1);
        mFile_name = (TextView) findViewById(R.id.file_name);
        mFolder_name = (TextView) findViewById(R.id.folder_name);
        Intent intent = getIntent();
        String file_name = intent.getExtras().getString("file_name");
        String folder_name = intent.getExtras().getString("folder_name");

        if (file_name.length() >= 5) {
            file_name=file_name.substring(0, 4);
            file_name = file_name + "...";
        }
        if (folder_name.length() >= 5) {
            folder_name=folder_name.substring(0, 4);
            folder_name = folder_name + "...";
        }


        mFolder_name.setText(folder_name);
        mFile_name.setText(file_name);

        /*String[] tFolder_name = new String[]{folder_name};
        String[] tFile_name = new String[]{file_name};


        if (tFolder_name.length <= 4) {
            for (int i = 3; i <= tFolder_name.length; i++) {
                tFolder_name[i] = ".";
                if (i >= 5) {
                    tFolder_name[i] = "";
                }
            }
        }
        if (tFile_name.length <= 5) {
            for (int i = 4; i <= tFolder_name.length; i++) {
                tFolder_name[i] = ".";
                if (i >= 6) {
                    tFolder_name[i] = "";
                }
            }*/


    }
}
