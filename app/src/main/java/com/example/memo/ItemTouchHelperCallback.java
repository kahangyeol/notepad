package com.example.memo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memo.Room.User;
import com.example.memo.recycle.RecyclerAdapter;

import java.util.List;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
//    private RecyclerAdapter adapter;
    static int dragFlag = 0;
    static int swipeFlag = 0;
    private ItemTouchHelperListener listener;
    private Context mContext;
//    private ButtonsState buttonsState = ButtonsState.GONE;
//    public List<User> users;

/*    private boolean swipeBack = false;
    private ButtonsState buttonsShowedState = ButtonsState.GONE;
    private static final float buttonWidth = 300;
    private RectF buttonInstance = null;
    private RecyclerView.ViewHolder currenrtItemViewHolder = null;*/

    public ItemTouchHelperCallback(List<User> users, ItemTouchHelperListener listener, Context mContext){
        this.listener = listener;
        this.mContext = mContext;
//        this.users = users;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //삼선 클릭시에만 이동
        /*viewHolder.itemView.findViewById(R.id.lineUp).setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
                    dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    ((RecyclerAdapter)recyclerView.getAdapter()).notifyDataSetChanged();
                }else dragFlag = 0;
                return false;
            }
        });*/
/*
        viewHolder.itemView.findViewById(R.id.lineUp).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                return false;
            }
        });
*/
        /*if (((RecyclerAdapter) recyclerView.getAdapter()).mItemViewType == 1)
            dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        else
            dragFlag = 0;*/
        dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
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
        listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }



    /*@Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//        onItemMoveListener.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
//        return listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }*/

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onItemSwipe(viewHolder.getAdapterPosition());
    }

/*
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        Bitmap icon;
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            View itemView = viewHolder.itemView;
            float height = (float) itemView.getBottom() - (float) itemView.getTop();
            float width = height / 3;
            Paint p = new Paint();
            //왼쪽 스와이프
            if (dX > 0) {
                p.setColor(Color.parseColor("#3B82F7"));
                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom() - 5);
                c.drawRect(background, p);
                icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pin_white);
                RectF icon_dest = new RectF((float) itemView.getLeft() + width + width/3, (float) itemView.getTop() + width - width/5, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width + width/5);
                c.drawBitmap(icon, null, icon_dest, p); //그리기
                buttonsState = ButtonsState.LEFT_VISIBLE;
            //오른쪽 스와이프
            } else if (dX < 0) {
                p.setColor(Color.parseColor("#D32F2F"));
                RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom() - 5);
                c.drawRect(background, p);
                icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.trash_white);
                RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width - width/3, (float) itemView.getTop() + width - width/5, (float) itemView.getRight() - width - width/3, (float) itemView.getBottom() - width + width/5);
                c.drawBitmap(icon, null, icon_dest, p);//그리기
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
*/

   /* @Override
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
//        drawButtons(c, currenrtItemViewHolder);
        View itemView = viewHolder.itemView;
        if (dX > 0) {
            Drawable background;
            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + (int) dX, itemView.getBottom());
            background.Color.parseColor("#3cca59");
            background.draw(c);

            // Draw the delete icon
            editIcon!!.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            editIcon.draw(c);
        }
        // swiping from right to left
        else if (dX < 0) {
            background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom);
            background.color = Color.parseColor("#f44336");
            background.draw(c);

            // Draw the delete icon
            deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            deleteIcon.draw(c);
        }
    }*/

/*
    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
        float buttonWidthWithOutPadding = buttonWidth - 10;
        float corners = -5;

        View itemView = viewHolder.itemView;
        float height = (float) itemView.getBottom() - (float) itemView.getTop();
        float width = height / 3;
        Paint p = new Paint();
        Bitmap icon;

        buttonInstance = null;
        //오른쪽 스와이프(왼쪽에 버튼 생길때)
        if(buttonsShowedState == ButtonsState.LEFT_VISIBLE) {
            RectF  leftButton = new RectF();
            int color = ContextCompat.getColor(mContext,R.color.blue);
            p.setColor(color);
            RectF icon_dest = new RectF(itemView.getLeft()+10, itemView.getTop() + 10, itemView.getLeft()+buttonWidthWithOutPadding, itemView.getBottom() - 10);
            c.drawRoundRect(leftButton,corners, corners,p);
            icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pin);
            c.drawBitmap(icon,null,leftButton,p);
            buttonInstance = leftButton;

            //왼쪽 스와이프(오른쪽에 버튼 생길때)
        } else if (buttonsShowedState == ButtonsState.RIGHT_VISIBLE) {
            RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithOutPadding, itemView.getTop() + 10, itemView.getRight() -10, itemView.getBottom() - 10);
            int color = ContextCompat.getColor(mContext,R.color.red);
            p.setColor(color);
            RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
            c.drawRoundRect(rightButton,corners, corners,p);
            icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.trash);
            c.drawBitmap(icon,null,rightButton,p);
            buttonInstance = rightButton;
        }
    }
*/

    /*@Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = false;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }*/

/*    private void setTouchListener(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;   // 선택취소 또는 누른걸 땟을때
                if(swipeBack){
                    if(dX < -buttonWidth)       //오른쪽 스와이프시 x축은 -x
                        buttonsShowedState = ButtonsState.RIGHT_VISIBLE;
                    else if(dX > buttonWidth)   //왼쪽 스와이프시 x축은 +x
                        buttonsShowedState = ButtonsState.LEFT_VISIBLE;
                    
                    if(buttonsShowedState != ButtonsState.GONE) {
                        seTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }
                }
                return false;
            }
        });
    }

    private void seTouchDownListener(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)    //버튼을 눌렀을때
                    setTouchUpListener(c,recyclerView,viewHolder,dX, dY, actionState, isCurrentlyActive);
                return false;
            }
        });
    }

    private void setTouchUpListener(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ItemTouchHelperCallback.super.onChildDraw(c,recyclerView,viewHolder,0F, buttonWidth, actionState, isCurrentlyActive);
                recyclerView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });

                setItemsClickable(recyclerView, true);
                swipeBack = false;

                if(listener != null && buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())){
                    if(buttonsShowedState == ButtonsState.LEFT_VISIBLE){
                        listener.onLeftClick(viewHolder.getAdapterPosition(), viewHolder);
                    }else if (buttonsShowedState == ButtonsState.RIGHT_VISIBLE){
                        listener.onRightClick(viewHolder.getAdapterPosition(), viewHolder);
                    }
                }

                buttonsShowedState = ButtonsState.GONE;
                currenrtItemViewHolder = null;
                return false;
            }
        });
    }

    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
        for(int i = 0; i < recyclerView.getChildCount(); i++){
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }*/
}
