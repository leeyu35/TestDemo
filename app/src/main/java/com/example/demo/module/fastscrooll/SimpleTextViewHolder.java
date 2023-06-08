package com.example.demo.module.fastscrooll;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * author: baiiu
 * date: on 16/6/27 17:38
 * description:
 */
public class SimpleTextViewHolder extends RecyclerView.ViewHolder {

    public SimpleTextViewHolder(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context)
                      .inflate(android.R.layout.simple_list_item_1, parent, false));

        itemView.setPadding(0, 100, 0, 100);

    }


    public void bind(Integer position) {

        ((TextView) itemView).setText("position: " + String.valueOf(position));


        //if (position % 2 == 0) {
        //    itemView.setBackgroundResource(android.R.color.darker_gray);
        //} else {
        //    itemView.setBackgroundResource(android.R.color.background_light);
        //}
    }

    public void bind(String text) {
        ((TextView) itemView).setText(text);
    }

}
