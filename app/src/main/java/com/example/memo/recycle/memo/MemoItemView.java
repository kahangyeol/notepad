package com.example.memo.recycle.memo;

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

public class MemoItemView extends LinearLayout{
    public TextView title, time;
    public ImageView bookmark, ivLeft, ivRight;
    public ImageButton lineUp;
    public RadioButton checkbox;
    public Context mContext;
    public MemoItemView(Context context, @LayoutRes int resource) {
        super(context);
        mContext = context;

        LayoutInflater inflate = LayoutInflater.from(context);

        View v = inflate.inflate(resource, this, true);

        title = v.findViewById(R.id.title);
        time = v.findViewById(R.id.time);
        bookmark = v.findViewById(R.id.imageView);
        lineUp = v.findViewById(R.id.lineUp);
        ivLeft = v.findViewById(R.id.iv_left);
        ivRight = v.findViewById(R.id.iv_right);
        checkbox = v.findViewById(R.id.checkbox);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    void setContents(User user, boolean check) {
        title.setText(user.getFolderTitle()); //title

        checkbox.setChecked(check);
        int color;
        if (check) {
            color = ContextCompat.getColor(mContext, R.color.gray);
        }else{
            color = ContextCompat.getColor(mContext, R.color.white);
        }
        findViewById(R.id.root).setBackgroundColor(color);
        ivLeft.setBackgroundColor(color);
        ivRight.setBackgroundColor(color);
        lineUp.setBackgroundColor(color);

        int star = AppDatabase.getInstance(mContext).userDao().starFolder(user.id);
        if(star == 0){
            bookmark.setImageResource(R.drawable.file);
        } else if(star == 1){
            bookmark.setImageResource(R.drawable.star);
        }

        int pin = AppDatabase.getInstance(mContext).userDao().loadPinMemo(user.id, user.root);
        int password = AppDatabase.getInstance(mContext).userDao().loadPassWord(user.id, user.root);
        if(pin == 1 && password == 1){
            ivLeft.setImageResource(R.drawable.lock);
            ivRight.setImageResource(R.drawable.pin);
        }else if(pin == 1 && password == 0){
            ivRight.setImageResource(R.drawable.pin);
            ivLeft .setImageResource(R.drawable.gone);
        }else if(pin == 0 && password == 1){
            ivRight.setImageResource(R.drawable.lock);
            ivLeft.setImageResource(R.drawable.gone);
        }else if(pin == 0 && password == 0){
            ivLeft.setImageResource(R.drawable.gone);
            ivRight.setImageResource(R.drawable.gone);
        }
    }
}
