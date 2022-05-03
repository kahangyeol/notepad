package com.example.memo.recycle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.core.content.ContextCompat;

import com.example.memo.R;
import com.example.memo.Room.AppDatabase;
import com.example.memo.Room.User;

public class ItemView extends LinearLayout{
//    public TextView key;
    public TextView title;
    public TextView memoCount;
    public ImageView bookmark;
    public ImageView pin;
    public ImageButton lineUp;
    public RadioButton checkbox;
    public Context mContext;
    public ItemView(Context context, @LayoutRes int resource) {
        super(context);
        mContext = context;

        LayoutInflater inflate = LayoutInflater.from(context);

        View v = inflate.inflate(resource, this, true);

        title = v.findViewById(R.id.title);
        bookmark = v.findViewById(R.id.imageView);
        lineUp = v.findViewById(R.id.lineUp);
        memoCount = v.findViewById(R.id.memo_count);
        pin = v.findViewById(R.id.pin);
        checkbox = v.findViewById(R.id.checkbox);
        v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    void setContents(User user, boolean check) {
        title.setText(user.getFolderTitle()); //title
        String countSet = Integer.toString(AppDatabase.getInstance(mContext).userDao().getAllMemoRoot(user.folderTitle).size());
        memoCount.setText(countSet);
        checkbox.setChecked(check);
        int color;
        if (check) {
            color = ContextCompat.getColor(mContext, R.color.gray);
        }else{
            color = ContextCompat.getColor(mContext, R.color.white);
        }
        findViewById(R.id.root).setBackgroundColor(color);
        pin.setBackgroundColor(color);
        lineUp.setBackgroundColor(color);
        int star = AppDatabase.getInstance(mContext).userDao().starFolder(user.id);
        if(star == 0){
            bookmark.setImageResource(R.drawable.folder);
        } else if(star == 1){
            bookmark.setImageResource(R.drawable.star);
        }

        int i_pin = AppDatabase.getInstance(mContext).userDao().loadPin(user.id);
        if(i_pin == 0){
            pin.setImageResource(R.drawable.gone);
        } else if(i_pin == 1){
            pin.setImageResource(R.drawable.pin);
        }
    }
}
