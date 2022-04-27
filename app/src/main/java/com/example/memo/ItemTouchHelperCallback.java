package com.example.memo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memo.recycle.RecyclerAdapter;

enum ButtonsState{
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}


public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private RecyclerAdapter adapter;
    static int dragFlag = 0;
    static int swipeFlag = 0;
    private ItemTouchHelperListener listener;
    private Context mContext;
    private boolean swipeBack = false;
    private ButtonsState buttonsShowedState = ButtonsState.GONE;
    private static final float buttonWidth = 115;
    private RectF buttonInstance = null;
    private RecyclerView.ViewHolder currenrtItemViewHolder = null;

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

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            if(buttonsShowedState != ButtonsState.GONE){    //사라지지 않았을경우
                if(buttonsShowedState != ButtonsState.LEFT_VISIBLE){    //왼쪽
                    dX = Math.max(dX, buttonWidth);     //x축이 +
                }if(buttonsShowedState != ButtonsState.RIGHT_VISIBLE){  //오른쪽
                    dX = Math.min(dX, buttonWidth);     //x축이 -
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            } else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            if(buttonsShowedState == ButtonsState.GONE){
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
        currenrtItemViewHolder = viewHolder;
    }

    private void setTouchListener(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                if(swipeBack){
                    if(dX < -buttonWidth)       //오른쪽 스와이프시 x축은 -x
                        buttonsShowedState = ButtonsState.RIGHT_VISIBLE;
                    else if(dX > buttonWidth)   //왼쪽 스와이프시 x축은 +x
                        buttonsShowedState = ButtonsState.RIGHT_VISIBLE;
                }
                return false;
            }
        });
    }
}
