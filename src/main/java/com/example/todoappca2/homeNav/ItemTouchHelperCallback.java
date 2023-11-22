package com.example.todoappca2.homeNav;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoappca2.R;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private ItemTouchHelperListener listener;

    public ItemTouchHelperCallback(ItemTouchHelperListener listener) {
        this.listener = listener;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.LEFT) {
            listener.onUpdate(viewHolder.getAdapterPosition());
        } else if (direction == ItemTouchHelper.RIGHT) {
            listener.onDelete(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        Context context = viewHolder.itemView.getContext();
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            View itemView = viewHolder.itemView;

            Paint p = new Paint();
            p.setTextSize(20);
            p.setTypeface(Typeface.DEFAULT_BOLD);

            if (dX > 0) {
                p.setColor(Color.RED);
                c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                        (float) itemView.getBottom(), p);
                p.setColor(Color.WHITE);
                c.drawText("Delete", (float) itemView.getLeft() + 20, (float) itemView.getTop() + 70, p);
            } else {
                p.setColor(Color.GREEN);
                c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                        (float) itemView.getRight(), (float) itemView.getBottom(), p);
                p.setColor(Color.WHITE);
                c.drawText("Update", (float) itemView.getRight() + dX + 20, (float) itemView.getTop() + 70, p);
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }
}


