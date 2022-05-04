package com.example.memo.recycle.memo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memo.Create_memo;
import com.example.memo.NewFolder;
import com.example.memo.R;
import com.example.memo.Room.AppDatabase;
import com.example.memo.Room.User;

import java.util.ArrayList;
import java.util.Collections;

public class MemoRecyclerAdapter extends RecyclerView.Adapter<MemoRecyclerAdapter.MyViewHolder> {

    public static final int VIEWTYPE_NORMAL = 0;
    public static final int VIEWTYPE_EDIT = 1;

    public boolean isSelectedAll = false;
    AppDatabase db;
    Context mContext;
    RecyclerView recyclerView;
    public ArrayList<View> keepItemView;
    public ArrayList<User> userData = new ArrayList<>();
    public ArrayList<Integer> selectCheckBox = new ArrayList<>();
    public int mItemViewType;

    public MemoRecyclerAdapter(Context context, RecyclerView recyclerView) {
        this.mContext = context;
        this.recyclerView = recyclerView;
        this.mItemViewType = VIEWTYPE_NORMAL;
    }


    public void addItem(User user) {
        userData.add(user);
        notifyDataSetChanged();
        if (isSelectedAll) {
            isSelectedAll = false;
        }
    }

    public void setItemViewType(int viewType) {
        mItemViewType = viewType;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mItemViewType;
    }

    @NonNull
    @Override
    public MemoRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MemoItemView view = null;
        if (viewType == VIEWTYPE_NORMAL) {
            view = new MemoItemView(mContext, R.layout.memorecycler_itmeview);
        } else {
            view = new MemoItemView(mContext, R.layout.edit_memorecycler_itmeview);
        }
        MemoRecyclerAdapter.MyViewHolder mh = new MemoRecyclerAdapter.MyViewHolder(view);
        return mh;
    }


    @Override
    public void onBindViewHolder(@NonNull MemoRecyclerAdapter.MyViewHolder holder, int position) {
        boolean check = false;
        for (int i = 0; i < selectCheckBox.size(); i++) {
            if (selectCheckBox.get(i) == position) {
                check = true;
                break;
            }
        }

        holder.mItemView.setContents(userData.get(position), check);

//        holder.checkBox.setChecked(isSelectedAll);
        holder.onBind(userData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return userData.size();
    }

    /*===========================ItemTouchHelper===========================*/
/*
    @Override
    public boolean onItemMove(int formPosition, int toPosition) {
        int formPin = userData.get(formPosition).pin;
        int toPin = userData.get(toPosition).pin;
        if(formPin != toPin){
            return false;
        }

        if (formPin == 0 && toPin == 0) {
            Collections.swap(userData, formPosition, toPosition);
            notifyItemMoved(formPosition, toPosition);
            setId();
            moveSet();
        } else if (formPin == 1 && toPin == 1) {
            Collections.swap(userData, formPosition, toPosition);
            notifyItemMoved(formPosition, toPosition);
            setId();
            moveSet();
        }


        return true;
    }

    @Override
    public void onItemSwipe(int position) {
//        userData.remove(position);
        userData.remove(position);
        notifyItemRemoved(position);
         del(userData.get(position), position);

        TextView folderCount = ((MainActivity) MainActivity.mContext).findViewById(R.id.folder_count);
        folderCount.setText("폴더: " + getItemCount());
        notifyDataSetChanged();

    }

    @Override
    public void onLeftClick(int position, RecyclerView.ViewHolder viewHolder) {
        setPin(position);
        notifyDataSetChanged();
    }

    @Override
    public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {
        del(userData.get(position), position);
        notifyDataSetChanged();
    }*/

    public void selectDel() {
        ArrayList<Integer> removeIndex = new ArrayList<>();
        int size = userData.size();
        for (int i = 0, j = 0; i < size && j < selectCheckBox.size(); i++) {
            if (selectCheckBox.get(j) == i) {
                AppDatabase.getInstance(mContext).userDao().selectDelFolder(i);
                removeIndex.add(i);
                j++;
            }
        }
        Collections.sort(removeIndex, Collections.reverseOrder());
        for (int i = 0; i < removeIndex.size(); i++) {
            int getIdx = removeIndex.get(i);
            userData.remove(getIdx);
        }
        setId();
        TextView folderCount = ((NewFolder) NewFolder.mContext).findViewById(R.id.memo_count);
        folderCount.setText("메모: " + getItemCount());
        selectCheckBox.removeAll(selectCheckBox);
        notifyDataSetChanged();
    }

    public void selectStar(String root) {
        int size = userData.size();
        for (int i = 0, j = 0; i < size && j < selectCheckBox.size(); i++) {
            if (selectCheckBox.get(j) == i) {
                int star = AppDatabase.getInstance(mContext).userDao().loadStarMemo(i,root);
                if (star == 0) {
                    AppDatabase.getInstance(mContext).userDao().updateStarOnMemo(i, root);
                } else if (star == 1) {
                    AppDatabase.getInstance(mContext).userDao().updateStarOffMemo(i, root);
                }
                j++;
            }
        }
        selectCheckBox.removeAll(selectCheckBox);
        isSelectedAll = false;
        RadioButton allCheck = ((NewFolder) NewFolder.mContext).findViewById(R.id.allCheck);
        allCheck.setChecked(false);
        notifyDataSetChanged();
    }

    public void selectPin(String root) {
        int size = userData.size();
        User user[] = new User[userData.size()];
        for (int i = 0; i < userData.size(); i++) {
            user[i] = userData.get(i);
        }
        ArrayList<Integer> pinCheckTrue = new ArrayList<>();
        ArrayList<Integer> pinCheckFalse = new ArrayList<>();
        for (int i = 0, j = 0; i < size /*&& j < selectCheckBox.size()*/; i++) {
            if (j < selectCheckBox.size()) {
                if (selectCheckBox.get(j) == i) {
                    int pin = AppDatabase.getInstance(mContext).userDao().loadPinMemo(i,root);
                    if (pin == 0) {
                        AppDatabase.getInstance(mContext).userDao().updatePinOn(i,root);
                    } else if (pin == 1) {
                        AppDatabase.getInstance(mContext).userDao().updatePinOff(i,root);
                    }
//                setPin(i);
                    j++;
                }
            }
            int pin = AppDatabase.getInstance(mContext).userDao().loadPinMemo(i,root);
            if (pin == 1) {
                pinCheckTrue.add(i);
            } else if (pin == 0) {
                pinCheckFalse.add(i);
            }
        }
        for (int i = 0; i < pinCheckTrue.size(); i++) {
            int position = pinCheckTrue.get(i);
            User insert = user[position];
            userData.set(i, insert);
        }

        for (int i = 0; i < pinCheckFalse.size(); i++) {
            int position = pinCheckFalse.get(i);
            User insert = user[position];
            userData.set(pinCheckTrue.size() + i, insert);
        }

        setId();
        selectCheckBox.removeAll(selectCheckBox);
        isSelectedAll = false;
        RadioButton allCheck = ((NewFolder) NewFolder.mContext).findViewById(R.id.allCheck);
        allCheck.setChecked(false);
        notifyDataSetChanged();
    }

    void moveSet() {
        ArrayList<String> folderName = new ArrayList<>();
        for (int i = 0; i < selectCheckBox.size(); i++) {
            String getName = userData.get(selectCheckBox.get(i)).folderTitle;
            folderName.add(getName);
        }
        for (int i = 0, j = 0; i < getItemCount() && j < selectCheckBox.size(); i++) {
//            String checkName = AppDatabase.getInstance(mContext).userDao().loadFolderTitle(i);
            String checkName = userData.get(i).folderTitle;
            if (folderName.get(j).equals(checkName)) {
                selectCheckBox.set(j, i);
                j++;
            }
        }
    }


    private void showKeyboard(EditText et) {
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        et.requestFocus();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener/*implements View.OnCreateContextMenuListener*/ {
        public MemoItemView mItemView;
        public ImageButton drag;
        public RadioButton checkBox;
        TextView key;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            if (mItemViewType == VIEWTYPE_EDIT)
                keepItemView.add(itemView);
            this.mItemView = (MemoItemView) itemView;
            key = itemView.findViewById(R.id.key);
            drag = itemView.findViewById(R.id.lineUp);
            checkBox = itemView.findViewById(R.id.checkbox);
            RadioButton allCheck = ((NewFolder) NewFolder.mContext).findViewById(R.id.allCheck);

            /*checkBox.setOnClickListener(v -> {
                setColor(allCheck, getAdapterPosition());
            });*/
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                int getId = userData.get(position).id;
                String root = userData.get(position).root;
                int passWord = AppDatabase.getInstance(mContext).userDao().loadPassWord(getId, root);
                if (mItemViewType == 0) {
                    if (passWord == 0) {
                        Intent intent = new Intent(itemView.getContext(), Create_memo.class);
//                intent.putExtra("root",user.getRoot());
//                intent.putExtra("memoId",user.getId());
                        intent.putExtra("check", true);
                        intent.putExtra("data", userData.get(position));
                        intent.putExtra("backHistory", 0);
                        itemView.getContext().startActivity(intent);
                        ((Activity) itemView.getContext()).finish();
                    } else if (passWord == 1) {
                        ((NewFolder) NewFolder.mContext).passWord(getId, 0);
                    }
                } else {
                    setColor(allCheck, position);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showMenu(v);
                    return false;
                }
            });
        }

        void setColor(RadioButton allCheck, int position) {
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
                Object removePosition = position;
                selectCheckBox.remove(removePosition);
                Collections.sort(selectCheckBox);
                if (allCheck.isChecked()) {
                    allCheck.setChecked(false);
                    isSelectedAll = false;
                }
                selectCount();
            } else {
                checkBox.setChecked(true);
                selectCheckBox.add(position);
                Collections.sort(selectCheckBox);
                if (selectCheckBox.size() == getItemCount()) {
                    allCheck.setChecked(true);
                    isSelectedAll = true;
                }
                selectCount();
            }
            setBackground(itemView, !(checkBox.isChecked()));

        }

        private void showMenu(View v) {
            Context wrapper = new ContextThemeWrapper(mContext,R.style.MyPopupMenu);
            PopupMenu popup = new PopupMenu(wrapper,v);
            popup.setOnMenuItemClickListener(this);//
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.memo_menu, popup.getMenu());
            popup.show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                popup.setForceShowIcon(true);
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int getId = item.getItemId();
            int position = getAdapterPosition();
            String root = userData.get(position).root;
            switch (getId) {
                case R.id.star:
                    int star = AppDatabase.getInstance(mContext).userDao().loadStarMemo(position, root);
                    if (star == 0) {
                        AppDatabase.getInstance(mContext).userDao().updateStarOnMemo(position, root);
                    } else if (star == 1) {
                        AppDatabase.getInstance(mContext).userDao().updateStarOffMemo(position, root);
                    }
                    notifyDataSetChanged();
                    break;
                case R.id.delete:
                    del(userData.get(getId), getId);
                    notifyDataSetChanged();

                    TextView memoCount = ((NewFolder) NewFolder.mContext).findViewById(R.id.memo_count);
                    memoCount.setText("메모: " + getItemCount());

                    break;
                case R.id.pin:
                    setPin(position, root);
                    notifyDataSetChanged();
                    break;
                case R.id.update:
                    ((NewFolder) NewFolder.mContext).passWord(getId, 1);
                    break;
            }
            return false;
        }

        public void onBind(User user, int position) {

        }
    }

    public void setPin(int position, String root) {
        db = AppDatabase.getInstance(mContext);
        User user = userData.get(position);
        int pin = AppDatabase.getInstance(mContext).userDao().loadPinMemo(position, root);
        if (pin == 0) {
            int pinCount = db.userDao().pinCountMemo(root);

            db.userDao().updatePinOn(position, root);
            db.userDao().updateMemoId(pinCount, user.getMemoTitle(),root,user.getId());
            Collections.swap(userData, position, pinCount);

            for (int i = 0; i < selectCheckBox.size(); i++) {
                if (position == selectCheckBox.get(i)) {
                    selectCheckBox.set(i, pinCount);
                }
            }
        } else if (pin == 1) {
            db.userDao().updatePinOff(position,root);
            int pinCount = db.userDao().pinCountMemo(root);
            db.userDao().updateMemoId(pinCount, user.getMemoTitle(), root, user.getId());
            Collections.swap(userData, position, pinCount);
        }

        setId();
    }

    public void setId() {
        for (int i = 0; i < userData.size(); i++) {   // id 초기화
            User user = userData.get(i);
            int id = user.getId();
            AppDatabase.getInstance(mContext).userDao().updateId(i, id);
            user.setId(i);
            userData.set(i, user);
        }
    }

    public void setBackground(View itemView, boolean checkBox) {
        int color;
        if (checkBox) {
            color = ContextCompat.getColor(mContext, R.color.white);
        } else {
            color = ContextCompat.getColor(mContext, R.color.gray);
        }
//        itemView.findViewById(R.id.imageView).setBackgroundColor(color);
        itemView.findViewById(R.id.iv_left).setBackgroundColor(color);
        itemView.findViewById(R.id.iv_right).setBackgroundColor(color);
        itemView.findViewById(R.id.lineUp).setBackgroundColor(color);
        itemView.findViewById(R.id.root).setBackgroundColor(color);
    }

    public void All() {
        if (isSelectedAll) {
            selectCheckBox.removeAll(selectCheckBox);
            isSelectedAll = false;
        } else {
            isSelectedAll = true;
            selectCheckBox = new ArrayList<>();
            for (int i = 0; i < userData.size(); i++) {
                selectCheckBox.add(i);
            }
        }
        selectCount();
        notifyDataSetChanged();
    }

    public void selectCount() {
        TextView selectCount = ((NewFolder) NewFolder.mContext).findViewById(R.id.selectMemo);
        selectCount.setText("선택된 메모 " + selectCheckBox.size());
    }

    public void del(User user, int position) {
        userData.remove(user); //----------- 삭제용
        int trashId = db.userDao().trashCount() + 1000; // 휴지통갯수 + 1000
        db.userDao().updateTrashId(1, trashId, user.getId(), user.getRoot());// 아이디를 휴지통 전용으로(휴지통갯수 + 1000) 변경
        notifyItemRemoved(position);

        String root = user.getRoot();
        for (int i = user.getId(); i < getItemCount(); i++) {
            String temp = db.userDao().loadMemoTitle(root, i + 1);
            System.out.println("바꿀 아이디: " + temp);
            db.userDao().updateMemoId(i, temp, root, i + 1);
            System.out.println("바뀐 아이디:" + i);
            User user1 = db.userDao().selectMemo(user.getRoot(), temp, i);
            userData.set(i, user1);
        }
        notifyDataSetChanged();
    }

}
/*{
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

            *//*userData.removeAll(userData);
            NewFolder newFolder = new NewFolder();
            newFolder.initialized(user.getRoot());*//*

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
}*/


