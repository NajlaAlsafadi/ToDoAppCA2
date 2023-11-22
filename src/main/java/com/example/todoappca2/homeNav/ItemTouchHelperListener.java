package com.example.todoappca2.homeNav;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemTouchHelperListener {
    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction);
    void onUpdate(int position);

    void onDelete(int position);
}