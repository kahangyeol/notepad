package com.example.memo.recycle;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memo.ItemTouchHelperListener;
import com.example.memo.MainActivity;
import com.example.memo.NewFolder;
import com.example.memo.R;
import com.example.memo.Room.AppDatabase;
import com.example.memo.Room.User;

import java.util.ArrayList;
import java.util.Collections;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> implements ItemTouchHelperListener {

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

    public RecyclerAdapter(Context context, RecyclerView recyclerView) {
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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemView view = null;
        if (viewType == VIEWTYPE_NORMAL) {
            view = new ItemView(mContext, R.layout.folderrecycler_itemview);
        } else {
            view = new ItemView(mContext, R.layout.free_align_item);
        }
        MyViewHolder mh = new MyViewHolder(view);
        return mh;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
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

    @Override
    public boolean onItemMove(int formPosition, int toPosition) {
        int formPin = userData.get(formPosition).pin;
        int toPin = userData.get(toPosition).pin;
        if(formPin != toPin){
            return false;
        }

        /*User user = userData.get(formPosition);
        if (formPin == 0 && toPin == 0) {
            userData.remove(formPosition);
            userData.add(toPosition,user);
            setId();
            moveSet();
        }if (formPin == 1 && toPin == 1) {
            userData.remove(formPosition);
            userData.add(toPosition,user);
            setId();
            moveSet();
        }*/


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
        /* del(userData.get(position), position);

        TextView folderCount = ((MainActivity) MainActivity.mContext).findViewById(R.id.folder_count);
        folderCount.setText("폴더: " + getItemCount());
        notifyDataSetChanged();*/

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
    }

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
        TextView folderCount = ((MainActivity) MainActivity.mContext).findViewById(R.id.folder_count);
        folderCount.setText("폴더: " + getItemCount());
        selectCheckBox.removeAll(selectCheckBox);
        selectCount();
        notifyDataSetChanged();
    }

    public void selectStar() {
        int size = userData.size();
        for (int i = 0, j = 0; i < size && j < selectCheckBox.size(); i++) {
            if (selectCheckBox.get(j) == i) {
                int star = AppDatabase.getInstance(mContext).userDao().starFolder(i);
                if (star == 0) {
                    AppDatabase.getInstance(mContext).userDao().updateStarOnFolder(i);
                } else if (star == 1) {
                    AppDatabase.getInstance(mContext).userDao().updateStarOffFolder(i);
                }
                j++;
            }
        }
        selectCheckBox.removeAll(selectCheckBox);
        isSelectedAll = false;
        RadioButton allCheck = ((MainActivity) MainActivity.mContext).findViewById(R.id.allCheck);
        allCheck.setChecked(false);
        selectCount();
        notifyDataSetChanged();
    }

    public void selectPin() {
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
                    int pin = AppDatabase.getInstance(mContext).userDao().loadPin(i);
                    if (pin == 0) {
                        AppDatabase.getInstance(mContext).userDao().updatePinOnFolder(i);
                    } else if (pin == 1) {
                        AppDatabase.getInstance(mContext).userDao().updatePinOffFolder(i);
                    }
//                setPin(i);
                    j++;
                }
            }
            int pin = AppDatabase.getInstance(mContext).userDao().loadPin(i);
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
        RadioButton allCheck = ((MainActivity) MainActivity.mContext).findViewById(R.id.allCheck);
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

    private void showDialogNewFolder(Dialog dialog, int position){

        EditText et = dialog.findViewById(R.id.et_folderName);
        Button yesBtn = dialog.findViewById(R.id.yesBtn);
        TextView trashTxt = dialog.findViewById(R.id.newFolder_txt);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Spannable span = (Spannable) trashTxt.getText();
        span.setSpan(new StyleSpan(Typeface.BOLD),0,8, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        span.setSpan(new AbsoluteSizeSpan(70),0,8, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        yesBtn.setEnabled(true);
        et.setText(userData.get(position).folderTitle);
        et.selectAll();

        dialog.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editFolderName = et.getText().toString();
                if (!editFolderName.equals("")) {
                    int i = position;
                    User folderName = new User(i, editFolderName, 0, "", "", null, userData.get(i).star, userData.get(i).password, userData.get(i).pin,null,null);
                    //변경 이름 저장
                    AppDatabase.getInstance(mContext).userDao().updateFolderTitle(editFolderName, i);
                    AppDatabase.getInstance(mContext).userDao().updateRoot(editFolderName, userData.get(i).getFolderTitle());

                    userData.set(position, folderName);

                    notifyItemChanged(position);
                    dialog.dismiss();
                } else {
                    androidx.appcompat.app.AlertDialog.Builder noname = new androidx.appcompat.app.AlertDialog.Builder(mContext);
                    noname.setTitle("폴더 이름을 입력하세요");
                    //noname.setMessage("제목없는 폴더는 만들수 없습니다");
                    noname.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    noname.show();
                }
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


    private void showKeyboard(EditText et){
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        et.requestFocus();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener/*implements View.OnCreateContextMenuListener*/ {
        public ItemView mItemView;
        public ImageButton drag;
        public RadioButton checkBox;
        TextView key;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            if (mItemViewType == VIEWTYPE_EDIT)
                keepItemView.add(itemView);
            this.mItemView = (ItemView) itemView;
            key = itemView.findViewById(R.id.key);
            drag = itemView.findViewById(R.id.lineUp);
            checkBox = itemView.findViewById(R.id.checkbox);
            RadioButton allCheck = ((MainActivity) MainActivity.mContext).findViewById(R.id.allCheck);

            /*checkBox.setOnClickListener(v -> {
                setColor(allCheck, getAdapterPosition());
            });*/
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                User user = userData.get(position);

                if (mItemViewType == 0) {
                    Intent intent = new Intent(itemView.getContext(), NewFolder.class);
                    intent.putExtra("folderTitle", user.getFolderTitle());
                    intent.putExtra("folderId", user.getId());
                    itemView.getContext().startActivity(intent);
                    ((Activity) itemView.getContext()).finish();
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
//            PopupMenu popup = new PopupMenu(mContext, v);
            popup.setOnMenuItemClickListener(this);// to implement on click event on items of menu
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.list_menu, popup.getMenu());
            popup.show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                popup.setForceShowIcon(true);
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int getId = item.getItemId();
            int position = getAdapterPosition();
            switch (getId) {
                case R.id.star:
                    int star = AppDatabase.getInstance(mContext).userDao().starFolder(position);
                    if (star == 0) {
                        AppDatabase.getInstance(mContext).userDao().updateStarOnFolder(position);
                    } else if (star == 1) {
                        AppDatabase.getInstance(mContext).userDao().updateStarOffFolder(position);
                    }
                    notifyDataSetChanged();
                    break;
                case R.id.delete:
                    del(userData.get(position), position);
                    notifyDataSetChanged();

                    TextView folderCount = ((MainActivity) MainActivity.mContext).findViewById(R.id.folder_count);
                    folderCount.setText("폴더: " + getItemCount());
                    break;
                case R.id.pin:
                    setPin(position);
                    notifyDataSetChanged();
                    break;
                case R.id.update:
                    mContext = MainActivity.mContext;
                    Dialog dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.newfolder_dialog);
                    showDialogNewFolder(dialog, position);
                    break;
            }
            return false;
        }

        public void onBind(User user, int position) {
            String s = "" + user.getId();
            key.setText(s);


        }
    }

    public void setPin(int position) {
        int pin = AppDatabase.getInstance(mContext).userDao().loadPin(position);
        if (pin == 0) {
            int pinCount = AppDatabase.getInstance(mContext).userDao().pinCount();
            AppDatabase.getInstance(mContext).userDao().updatePinOnFolder(position);
            AppDatabase.getInstance(mContext).userDao().updateId(pinCount, userData.get(position).getFolderTitle());
            Collections.swap(userData, position, pinCount);
            for(int i = 0; i<selectCheckBox.size(); i++) {
                if(position == selectCheckBox.get(i)){
                    selectCheckBox.set(i, pinCount);
                }
            }
        } else if (pin == 1) {
            AppDatabase.getInstance(mContext).userDao().updatePinOffFolder(position);
            int pinCount = AppDatabase.getInstance(mContext).userDao().pinCount();
            AppDatabase.getInstance(mContext).userDao().updateId(pinCount, userData.get(position).getFolderTitle());
            Collections.swap(userData, position, pinCount);
        }

        setId();
    }

    public void setId() {
        for (int i = 0; i < userData.size(); i++) {   // id 초기화
            User user = userData.get(i);
            String folderTitle = user.getFolderTitle();
            AppDatabase.getInstance(mContext).userDao().updateId(i, folderTitle);
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
        itemView.findViewById(R.id.pin).setBackgroundColor(color);
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

    public void selectCount(){
        TextView selectCount = ((MainActivity) MainActivity.mContext).findViewById(R.id.selectFolder);
        selectCount.setText("선택된 폴더 " + selectCheckBox.size());
    }

    public void del(User user, int position) {
        userData.remove(user); //----------- 삭제용
        AppDatabase.getInstance(mContext).userDao().delMemo(user.getFolderTitle());
        AppDatabase.getInstance(mContext).userDao().delFolder(user.getFolderTitle(), position);

            /*user.setId(user.getId()+1000);
            user.setTrash(true);*/
        setId();
        /*notifyItemRemoved(position);
        for (int i = user.getId(); i < getItemCount(); i++) {
            String temp = AppDatabase.getInstance(mContext).userDao().loadFolderTitle(i + 1);
            AppDatabase.getInstance(mContext).userDao().updateId(i, temp);
            User user1 = AppDatabase.getInstance(mContext).userDao().selectFolder(userData.get(i).getFolderTitle());
            userData.set(i, user1);
        }*/
        db.getInstance(mContext).userDao().delete(user);

        notifyDataSetChanged();
    }
}
