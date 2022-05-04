package com.example.memo.recycle.trash;

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

import com.example.memo.R;
import com.example.memo.Room.AppDatabase;
import com.example.memo.Room.User;
import com.example.memo.Create_memo;
import com.example.memo.Trash;

import java.util.ArrayList;

public class TrashRecyclerViewAdapter extends RecyclerView.Adapter<TrashRecyclerViewAdapter.MyViewHolder> {

    Context mContext = Trash.mContext;
    AppDatabase db = AppDatabase.getInstance(mContext);
    private ArrayList<User> userData = new ArrayList<>();

    @NonNull
    @Override
    public TrashRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.memorecycler_itmeview, parent, false);
        AdapterView.OnItemLongClickListener listener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return false;
            }
        };
        return new TrashRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrashRecyclerViewAdapter.MyViewHolder holder, int position) {
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

    public void allClear(){
        for(int i = 0; i < getItemCount();i++){
            User user = userData.get(i);
            AppDatabase.getInstance(mContext).userDao().delete(user);
        }
        userData.removeAll(userData);
        notifyDataSetChanged();
        count();
    }

    public void allRollback(){
        for(int i = 0; i< getItemCount(); i++){
            User user = userData.get(i);
            int memoId = db.userDao().rollBack(user.getRoot()); // root 파일 갯수 받아오기
            db.userDao().updateTrashId(0,memoId,user.getId(),user.getRoot());
        }
        userData.removeAll(userData);
        notifyDataSetChanged();
        count();
    }

    public void count(){
        TextView folderCount= ((Trash) Trash.mContext).findViewById(R.id.memo);
        folderCount.setText("메모: "+getItemCount());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private TextView key;
        private TextView title;
        private TextView description;
        private ImageView bookmark;

        public MyViewHolder(@NonNull View itemView) { //item 저장
            super(itemView);

            itemView.setOnCreateContextMenuListener(this);
//            key = itemView.findViewById(R.id.key);
            title = itemView.findViewById(R.id.title);

//            description = itemView.findViewById(R.id.memoTextView2);
            bookmark = itemView.findViewById(R.id.imageView);
        }

        public void del(User user, int position){
            userData.remove(user); //----------- 삭제용
            db.userDao().delete(user);
            notifyItemRemoved(position);
            int root = user.getRoot();
            for(int i = user.getId()-1000;i<getItemCount();i++) {
                String temp = AppDatabase.getInstance(mContext).userDao()
                        .loadMemoTitle(root, 1000 + i + 1); //id가 1001인 메모 삭제하면 1000 + (1001 -1000) + 1 불러옴
                AppDatabase.getInstance(mContext).userDao()
                        .updateMemoId(i + 1000, temp,root,1000+i+1);
                User user1 = AppDatabase.getInstance(mContext).userDao().selectTrash(temp);
                userData.set(i,user1);
            }
            notifyDataSetChanged();
        }

        public void rollBack(User user, int position){
            userData.remove(user); //----------- 삭제용
            notifyItemRemoved(position);
            int memoId = db.userDao().rollBack(user.getRoot()); // root 파일 갯수 받아오기
            db.userDao().updateTrashId(0,memoId,user.getId(),user.getRoot()); //
            int root = user.getRoot();
            for(int i = user.getId()-1000;i<getItemCount();i++) {
                String temp = AppDatabase.getInstance(mContext).userDao()
                        .loadMemoTitle(root, 1000 + i + 1); //id가 1001인 메모 복구하면 1000 + (1001 -1000) + 1 불러옴
                AppDatabase.getInstance(mContext).userDao()
                        .updateMemoId(i + 1000, temp,root,1000+i+1);
                User user1 = AppDatabase.getInstance(mContext).userDao().selectTrash(temp);
                userData.set(i,user1);
            }
        }

        public void onBind(User user, int position) {
//            user.setId(position);
/*            String s = "" + (user.getId());
            key.setText(s); //id*/
            title.setText(user.getMemoTitle()); //title
            bookmark.getContext().getResources().getDrawable(R.drawable.star);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), Create_memo.class);
//                intent.putExtra("root",user.getRoot());
//                intent.putExtra("memoId",user.getId());
                intent.putExtra("check",true);
                intent.putExtra("data", user);
                intent.putExtra("backHistory",1);
                itemView.getContext().startActivity(intent);
                ((Activity)itemView.getContext()).finish();
            });

            itemView.setOnCreateContextMenuListener(this);

        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//            ((Activity) v.getContext()).getMenuInflater().inflate(R.menu.list_menu,menu);
            MenuItem rollback = menu.add(Menu.NONE, 1001, 1, "복구");
            MenuItem delete = menu.add(Menu.NONE, 1002, 2, "삭제");
//            MenuItem pix = menu.add(Menu.NONE, 1003, 3, "상단에 고정");
//            MenuItem update = menu.add(Menu.NONE, 1004, 4, "이름변경");
            rollback.setOnMenuItemClickListener(onEditMenu);
            delete.setOnMenuItemClickListener(onEditMenu);
            //pix.setOnMenuItemClickListener(onEditMenu);
//            update.setOnMenuItemClickListener(onEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1001:
                        int position = getAdapterPosition();
                        rollBack(userData.get(position),position);
                        notifyDataSetChanged();
                        count();
                        break;
                    case 1002:
                        position = getAdapterPosition();
                        del(userData.get(position),position);
                        notifyDataSetChanged();
                        count();
                        break;
                    case 1003:
                        break;
                    case 1004:
                        break;
                }
                return false;
            }

        };

    }
}


