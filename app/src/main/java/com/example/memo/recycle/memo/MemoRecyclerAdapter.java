package com.example.memo.recycle.memo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memo.NewFolder;
import com.example.memo.R;
import com.example.memo.Room.AppDatabase;
import com.example.memo.Room.User;
import com.example.memo.create_memo;

import java.util.ArrayList;

public class MemoRecyclerAdapter extends RecyclerView.Adapter<MemoRecyclerAdapter.MyViewHolder>{
    boolean passWordCheck;
    Context mContext = NewFolder.mContext;
    AppDatabase db = AppDatabase.getInstance(mContext);
    private ArrayList<User> userData = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.memorecycler_itmeview, parent, false);
        AdapterView.OnItemLongClickListener listener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return false;
            }
        };
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.onBind(userData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return userData.size();
    }

    public void addItem(User user) {
        userData.add(user);
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<User> users) {
        userData = users;
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private TextView title;
        private ImageView iv_left;
        private ImageView iv_right;
        private ImageView bookmark;

        public MyViewHolder(@NonNull View itemView) { //item 저장
            super(itemView);

            itemView.setOnCreateContextMenuListener(this);
            title = itemView.findViewById(R.id.title);
            iv_left = itemView.findViewById(R.id.iv_left);
            iv_right = itemView.findViewById(R.id.iv_right);
            bookmark = itemView.findViewById(R.id.imageView);


        }

        public void del(User user, int position){
            userData.remove(user); //----------- 삭제용
            int trashId = db.userDao().trashCount()+1000; // 휴지통갯수 + 1000
            db.userDao().updateTrashId(1,trashId,user.getId(),user.getRoot());// 아이디를 휴지통 전용으로(휴지통갯수 + 1000) 변경
            notifyItemRemoved(position);
            String root = user.getRoot();
            for(int i = user.getId();i<getItemCount();i++) {
                String temp = db.userDao().loadMemoTitle(root,i + 1);
                System.out.println("바꿀 아이디: "+temp);
                db.userDao().updateMemoId(i, temp, root, i+1);
                System.out.println("바뀐 아이디:" + i);
                User user1 = db.userDao().selectMemo(user.getRoot(),temp,i);
                userData.set(i,user1);
            }

            /*userData.removeAll(userData);
            NewFolder newFolder = new NewFolder();
            newFolder.initialized(user.getRoot());*/

            notifyDataSetChanged();
        }

        public void onBind(User user, int position) {
//            user.setId(position);
            String s = "" + (user.getId());
//            key.setText(s); //id
            title.setText(user.getMemoTitle()); //title

            int star = AppDatabase.getInstance(mContext).userDao().loadStarMemo(user.getId(),user.getRoot());
            int password = AppDatabase.getInstance(mContext).userDao().loadPassWord(user.getId(),user.getRoot());
            int pin = AppDatabase.getInstance(mContext).userDao().loadPinMemo(user.getId(),user.getRoot());
            if(star == 0)
                bookmark.setImageResource(R.drawable.file);
            else if(star == 1)
                bookmark.setImageResource(R.drawable.star);

            pinPasswordArray(pin, password, iv_left, iv_right);

            itemView.setOnClickListener(v -> {
                int passWord = AppDatabase.getInstance(mContext).userDao().loadPassWord(user.getId(), user.getRoot());
                if(passWord == 0) {
                    Intent intent = new Intent(itemView.getContext(), create_memo.class);
//                intent.putExtra("root",user.getRoot());
//                intent.putExtra("memoId",user.getId());
                    intent.putExtra("check", true);
                    intent.putExtra("data", user);
                    intent.putExtra("backHistory",0);
                    itemView.getContext().startActivity(intent);
                    ((Activity) itemView.getContext()).finish();
                }else if(passWord == 1){
                    ((NewFolder)NewFolder.mContext).passWord(user.getId(),0);

                }
            });

            itemView.setOnCreateContextMenuListener(this);

        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//            ((Activity) v.getContext()).getMenuInflater().inflate(R.menu.list_menu,menu);
            int id = userData.get(getAdapterPosition()).getId();
            String root = userData.get(getAdapterPosition()).getRoot();
            int passWord = AppDatabase.getInstance(mContext).userDao().loadPassWord(id, root);
            int starCheck = AppDatabase.getInstance(mContext).userDao().loadStarMemo(id, root);
            int pinCheck = AppDatabase.getInstance(mContext).userDao().loadPinMemo(id,root);

            if(starCheck == 0) {
                MenuItem star = menu.add(Menu.NONE, 1001, 1, "즐겨찾기");
                star.setOnMenuItemClickListener(onEditMenu);
            }else if(starCheck == 1) {
                MenuItem star = menu.add(Menu.NONE, 1001, 1, "즐겨찾기 해제");
                star.setOnMenuItemClickListener(onEditMenu);
            }

            MenuItem delete = menu.add(Menu.NONE, 1002, 2, "휴지통");

            if(pinCheck == 0) {
                MenuItem pin = menu.add(Menu.NONE, 1003, 3, "상단에 고정");
                pin.setOnMenuItemClickListener(onEditMenu);
            } else if(pinCheck == 1){
                MenuItem pin = menu.add(Menu.NONE,1003,3,"상단에 고정 해제");
                pin.setOnMenuItemClickListener(onEditMenu);
            }

            if(passWord == 0) {
                MenuItem update = menu.add(Menu.NONE, 1004, 4, "잠금");
                update.setOnMenuItemClickListener(onEditMenu);
            }else if(passWord == 1) {
                MenuItem update = menu.add(Menu.NONE, 1004, 4, "잠금해제");
                update.setOnMenuItemClickListener(onEditMenu);
            }
            delete.setOnMenuItemClickListener(onEditMenu);
            //pix.setOnMenuItemClickListener(onEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = userData.get(getAdapterPosition()).getId();
                String root = userData.get(getAdapterPosition()).getRoot();

                switch (item.getItemId()) {
                    case 1001:
                        int star = AppDatabase.getInstance(mContext).userDao().loadStarMemo(id,root);
                        if(star == 0) {
                            bookmark.setImageResource(R.drawable.star);
                            AppDatabase.getInstance(mContext).userDao().updateStarOnMemo(id,root);
                        }
                        else if(star == 1){
                            bookmark.setImageResource(R.drawable.file);
                            AppDatabase.getInstance(mContext).userDao().updateStarOffMemo(id,root);
                        }
                        break;
                    case 1002:
                        del(userData.get(id),id);
                        notifyDataSetChanged();

                        TextView folderCount = ((NewFolder)NewFolder.mContext).findViewById(R.id.memo_count);
                        folderCount.setText("메모: "+getItemCount());

                        break;
                    case 1003:
                        int pin = AppDatabase.getInstance(mContext).userDao().loadPinMemo(id, root);
                        if(pin == 0) {
                            AppDatabase.getInstance(mContext).userDao().updatePinOn(id,root);
                        }
                        else if(pin == 1){
                            AppDatabase.getInstance(mContext).userDao().updatePinOff(id,root);
                        }
                        notifyDataSetChanged();
                        break;
                    case 1004:
                        ((NewFolder)NewFolder.mContext).passWord(id,1);
                        break;
                }
                return false;
            }

        };

    }

    private void pinPasswordArray(int pin, int password, ImageView iv_left, ImageView iv_right ) {
        if(pin == 1 && password == 1){
            iv_left.setImageResource(R.drawable.lock);
            iv_right.setImageResource(R.drawable.pin);
        }else if(pin == 1 && password == 0){
            iv_right.setImageResource(R.drawable.pin);
            iv_left .setImageResource(R.drawable.gone);
        }else if(pin == 0 && password == 1){
            iv_right.setImageResource(R.drawable.lock);
            iv_left.setImageResource(R.drawable.gone);
        }else if(pin == 0 && password == 0){
            iv_left.setImageResource(R.drawable.gone);
            iv_right.setImageResource(R.drawable.gone);
        }
    }
}


