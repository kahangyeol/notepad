package com.example.memo.recycle.edit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memo.ItemTouchHelperListener;
import com.example.memo.MainActivity;
import com.example.memo.NewFolder;
import com.example.memo.R;
import com.example.memo.Room.AppDatabase;
import com.example.memo.Room.User;

import java.util.ArrayList;

//원본 196
public class EditRecyclerAdapter extends RecyclerView.Adapter<EditRecyclerAdapter.MyViewHolder> implements ItemTouchHelperListener {

    AppDatabase db;
    Context mContext = MainActivity.mContext;
    private ArrayList<User> userData = new ArrayList<>();

    @NonNull
    @Override
    public EditRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.free_align_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditRecyclerAdapter.MyViewHolder holder, int position) {
        holder.onBind(userData.get(position),position);
    }

    @Override
    public int getItemCount() {
        return userData.size();
    }

    @Override
    public boolean onItemMove(int from_postion, int to_postion) {
        return false;
    }

    public void addItem(User user){
        userData.add(user);
        notifyDataSetChanged();
    }

    @Override
    public void onItemSwipe(int postion) {

    }

    @Override
    public void onLeftClick(int position, RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private TextView title;
        private TextView memoCount;
        private ImageView bookmark;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnCreateContextMenuListener(this);
//            key = itemView.findViewById(R.id.key); -------------------id 체크
            title = itemView.findViewById(R.id.title);
            memoCount = itemView.findViewById(R.id.memo_count);
//            description = itemView.findViewById(R.id.memoTextView2);
            bookmark = itemView.findViewById(R.id.imageView);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }

        public void onBind(User user, int position) {
//            user.setId(position);
            String s = "" + (user.getId());
//            key.setText(s); //id
            title.setText(user.getFolderTitle()); //title
            bookmark.getContext().getResources().getDrawable(R.drawable.star);
            memoCount.setText(""+ AppDatabase.getInstance(mContext).userDao().getAllMemoRoot(user.folderTitle).size());

            int star = AppDatabase.getInstance(mContext).userDao().starFolder(getAdapterPosition());
            if(star == 0){
                bookmark.setImageResource(R.drawable.folder);
            } else if(star == 1){
                bookmark.setImageResource(R.drawable.star);
            }

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), NewFolder.class);
                intent.putExtra("folderTitle", user.getFolderTitle());
                intent.putExtra("folderId", user.getId());
                itemView.getContext().startActivity(intent);
                ((Activity) itemView.getContext()).finish();
            });

            itemView.setOnCreateContextMenuListener(this);

        }

    }
}

/*{

    AppDatabase db;
    Context mContext = MainActivity.mContext;
    private ArrayList<User> userData = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.free_align_item,parent, false);
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

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        //이동할 객체 저장
        User user = userData.get(from_position);
        //이동할 객체 삭제
        userData.remove(from_position);
        //이동하고 싶은 position에 추가
        userData.add(to_position, user);

        //Adapter에 데이터 이동알림
        notifyItemMoved(from_position, to_position);
        return true;
    }

    @Override
    public void onItemSwipe(int position) {
        userData.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

//        private TextView key;
        private TextView title;
        //            private TextView description;
        private CheckBox checkBox;
        private ImageView bookmark;
        private ImageView lineUp;

        public MyViewHolder(@NonNull View itemView) { //item 저장
            super(itemView);

//                key = itemView.findViewById(R.id.key);
            title = itemView.findViewById(R.id.memoTextView);
            checkBox = itemView.findViewById(R.id.checkbox);
//            description = itemView.findViewById(R.id.memoTextView2);
            bookmark = itemView.findViewById(R.id.imageView);
            lineUp = itemView.findViewById(R.id.lineUp);

        }

        public void del(User user, int position) {
            userData.remove(user); //----------- 삭제용
            *//*int trashId = AppDatabase.getInstance(mContext).userDao().trashCount()+1000; // 휴지통갯수 + 1000
            AppDatabase.getInstance(mContext).userDao().updateTrashId(true,trashId,user.getId());// 아이디를 휴지통 전용으로(휴지통갯수 + 1000) 변경*//*
            AppDatabase.getInstance(mContext).userDao().delMemo(user.getFolderTitle());
            AppDatabase.getInstance(mContext).userDao().delFolder(user.getFolderTitle());

            *//*user.setId(user.getId()+1000);
            user.setTrash(true);*//*
            notifyItemRemoved(position);
            for (int i = user.getId(); i < getItemCount(); i++) {
                String temp = AppDatabase.getInstance(mContext).userDao().loadFolderTitle(i + 1);
                AppDatabase.getInstance(mContext).userDao().updateId(i, temp);
                User user1 = AppDatabase.getInstance(mContext).userDao().selectFolder(userData.get(i).getFolderTitle());
                userData.set(i, user1);
            }
            db.getInstance(itemView.getContext()).userDao().delete(user);

            notifyDataSetChanged();
        }

        public void onBind(User user, int position) {
//            user.setId(position);
*//*                String s = "" + (user.getId());
                key.setText(s); //id*//*
            title.setText(user.getFolderTitle()); //title
            bookmark.getContext().getResources().getDrawable(R.drawable.star);

            int star = AppDatabase.getInstance(mContext).userDao().starFolder(getAdapterPosition());
            if (star == 0) {
                bookmark.setImageResource(R.drawable.folder);
            } else if (star == 1) {
                bookmark.setImageResource(R.drawable.star);
            }

            itemView.setOnClickListener(v -> {

                Intent intent = new Intent(itemView.getContext(), NewFolder.class);
                intent.putExtra("folderTitle", user.getFolderTitle());
                intent.putExtra("folderId", user.getId());
                itemView.getContext().startActivity(intent);

            });
        }


    }

    *//*
    private ArrayList<User> userData = new ArrayList<>();
    private Map<User,Boolean> mCheckedMap = new HashMap<>();
    private List<User> mCheckedDataList = new ArrayList<>();
    private int[]a=new int[100000];
    int i=0;
    public void removeItems(){
        userData.removeAll(mCheckedDataList);
        notifyDataSetChanged();
    }

    boolean isSelectedAll = false;
    public void selectAll(){
        isSelectedAll=true;
        notifyDataSetChanged();
    }
    public void unselectall(){
        isSelectedAll=false;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.free_align_item,parent, false);

        final ItemViewHolder ItemViewHolder= new ItemViewHolder(view);

        ItemViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                User user = userData.get(ItemViewHolder.getAdapterPosition());
                mCheckedMap.put(user, isChecked);

                if(isChecked){

                    mCheckedDataList.add(user);

                }else{
                    mCheckedDataList.remove(user);
                }
            }
        });

//        return new ItemViewHolder(view);
        return ItemViewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

            User user = userData.get(position);

            //holder.list_image.setImageResource(R.mipmap.ic_launcher);
            holder.list_name.setText(user.getFolderTitle());
            // holder.list_name.setText();
            *//**//*holder.onBind(userData.get(position));*//**//*
            if(!isSelectedAll){
                holder.checkBox.setChecked(false);
            }else{
                holder.checkBox.setChecked(true);
            }
            boolean isChecked = mCheckedMap.get(user) ==  null
                    ? false
                    : mCheckedMap.get(user);
            *//**//*if(isChecked){
                a[i] = position;
                i++;
            }*//**//*
            holder.checkBox.setChecked(isChecked);

        *//**//*if(mCheckedMap.get(data)){
            holder.list_name.setChecked(true);
        }else{
            holder.list_name.setChecked(false);
        }
        holder.list_name.setChecked(isChecked);*//**//*

         holder.onBind(userData.get(position));
    }

    @Override
    public int getItemCount() {
        return userData.size();
    }
    public void addItem(User user){
        userData.add(user);
    }

    public void setAllChecked(final boolean ischeked) {

    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        //이동할 객체 저장
        User user = userData.get(from_position);
        //이동할 객체 삭제
        userData.remove(from_position);
        //이동하고 싶은 position에 추가
        userData.add(to_position,user);

        //Adapter에 데이터 이동알림
        notifyItemMoved(from_position,to_position);
        return true;
    }

    @Override
    public void onItemSwipe(int position) {
        userData.remove(position);
        notifyItemRemoved(position);
    }

    // item 설정(?)
    class ItemViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView list_name;
        ImageView list_image;


        public ItemViewHolder(View itemView) {
            super(itemView);
            list_name = itemView.findViewById(R.id.textView);
            list_image = itemView.findViewById(R.id.imageView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }

        public int onBind(User user *//**//*DATA person*//**//*) {
            list_name.setText(user.getFolderTitle());
//            list_image.setImageResource(person.getImage());
                return 0;
        }
    }
}*//*

*//*
    @Override
    public boolean onItemMove(int from_postion, int to_position){
        User user = userData.get(from_postion);
        userData.remove(from_postion);
        userData.add(to_position,data);
        notifyItemMoved(from_postion,to_position);
        return true;
    }



    class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
//            textView = (TextView) itemView.findViewById(R.id.title);
            textView = itemView.findViewById(R.id.title);
        }
        void onBind(User user){
            textView.setText(data.gettitle());
        }

    }
*//*
}*/















/*
package com.example.memo.recycle.edit;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.example.memo.Data;
        import com.example.memo.ItemTouchHelperListener;
        import com.example.memo.R;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

public class EditRecyclerAdapter extends RecyclerView.Adapter<EditRecyclerAdapter.ItemViewHolder> implements ItemTouchHelperListener {
    private ArrayList<Data> userData = new ArrayList<>();
    private Map<Data,Boolean> mCheckedMap = new HashMap<>();
    private List<Data> mCheckedDataList = new ArrayList<>();
    private int[]a=new int[100000];
    int i=0;
    public void removeItems(){
        userData.removeAll(mCheckedDataList);
        notifyDataSetChanged();
    }

    public void star(){

    }

    boolean isSelectedAll = false;
    public void selectAll(){
        isSelectedAll=true;
        notifyDataSetChanged();
    }
    public void unselectall(){
        isSelectedAll=false;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.free_align_item,parent, false);

        final ItemViewHolder ItemViewHolder= new ItemViewHolder(view);

        ItemViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                User user = userData.get(ItemViewHolder.getAdapterPosition());
                mCheckedMap.put(data, isChecked);

                if(isChecked){

                    mCheckedDataList.add(data);

                }else{
                    mCheckedDataList.remove(data);
                }
            }
        });

//        return new ItemViewHolder(view);
        return ItemViewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        User user = userData.get(position);

        //holder.list_image.setImageResource(R.mipmap.ic_launcher);
        holder.list_name.setText(data.gettitle());
        // holder.list_name.setText();
        */
/*holder.onBind(userData.get(position));*//*

        if(!isSelectedAll){
            holder.checkBox.setChecked(false);
        }else{
            holder.checkBox.setChecked(true);
        }
        boolean isChecked = mCheckedMap.get(data) ==  null
                ? false
                : mCheckedMap.get(data);
            */
/*if(isChecked){
                a[i] = position;
                i++;
            }*//*

        holder.checkBox.setChecked(isChecked);

        */
/*if(mCheckedMap.get(data)){
            holder.list_name.setChecked(true);
        }else{
            holder.list_name.setChecked(false);
        }
        holder.list_name.setChecked(isChecked);*//*


        holder.onBind(userData.get(position));
    }

    @Override
    public int getItemCount() {
        return userData.size();
    }
    void addItem(User user){
        userData.add(data);
    }

    public void setAllChecked(final boolean ischeked) {

    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        //이동할 객체 저장
        Data person = userData.get(from_position);
        //이동할 객체 삭제
        userData.remove(from_position);
        //이동하고 싶은 position에 추가
        userData.add(to_position,person);

        //Adapter에 데이터 이동알림
        notifyItemMoved(from_position,to_position);
        return true;
    }

    @Override
    public void onItemSwipe(int position) {
        userData.remove(position);
        notifyItemRemoved(position);
    }

    // item 설정(?)
    class ItemViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView list_name;
        ImageView list_image;


        public ItemViewHolder(View itemView) {
            super(itemView);
            list_name = itemView.findViewById(R.id.textView);
            list_image = itemView.findViewById(R.id.imageView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }

        public int onBind(Data person) {
            list_name.setText(person.gettitle());
            list_image.setImageResource(person.getImage());
            return 0;
        }
    }
}

*/
/*
    @Override
    public boolean onItemMove(int from_postion, int to_position){
        User user = userData.get(from_postion);
        userData.remove(from_postion);
        userData.add(to_position,data);
        notifyItemMoved(from_postion,to_position);
        return true;
    }



    class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
//            textView = (TextView) itemView.findViewById(R.id.title);
            textView = itemView.findViewById(R.id.title);
        }
        void onBind(User user){
            textView.setText(data.gettitle());
        }

    }

}
*//*



*/
