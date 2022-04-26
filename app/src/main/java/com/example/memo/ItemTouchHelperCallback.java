package com.example.memo;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memo.recycle.RecyclerAdapter;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private RecyclerAdapter adapter;
    static int dragFlag = 0;
    static int swipeFlag = 0;
    private ItemTouchHelperListener listener;
    private Context mContext;

    public ItemTouchHelperCallback(RecyclerAdapter adapter,Context mContext){
        this.adapter = adapter;
        this.mContext = mContext;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //삼선 클릭시에만 이동
        viewHolder.itemView.findViewById(R.id.lineUp).setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
                    dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                }else dragFlag = 0;
                return false;
            }
        });
/*
        viewHolder.itemView.findViewById(R.id.lineUp).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                return false;
            }
        });
*/
       /* if (((RecyclerAdapter) recyclerView.getAdapter()).mItemViewType == 1)
            dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        else
            dragFlag = 0;*/

        if (((RecyclerAdapter) recyclerView.getAdapter()).mItemViewType == 0)
            swipeFlag = ItemTouchHelper.START | ItemTouchHelper.END;
        else
            swipeFlag = 0;

        return makeMovementFlags(dragFlag, swipeFlag);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }



    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//        onItemMoveListener.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
//        return listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onItemSwipe(viewHolder.getAdapterPosition());
    }
}
