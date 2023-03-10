package com.example.demo.guide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.demo.R;
import com.example.demo.widget.guide.NewbieGuide;
import com.example.demo.widget.guide.core.Controller;
import com.example.demo.widget.guide.listener.OnLayoutInflatedListener;
import com.example.demo.widget.guide.model.GuidePage;

import java.util.ArrayList;

/**
 * Created by hubert
 * <p>
 * Created on 2017/9/23.
 */

public class RecyclerViewActivity extends AppCompatActivity {

    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recylerview);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            data.add("item " + i);
        }
        recyclerView.setAdapter(new Adapter(data));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    int targetPosition = 20;
                    if (firstVisibleItemPosition <= targetPosition
                            && targetPosition < lastVisibleItemPosition) {//??????????????????????????????
                        NewbieGuide.with(RecyclerViewActivity.this)
                                .setLabel("grid_view_guide")
                                .alwaysShow(true)
                                .addGuidePage(GuidePage.newInstance()
                                        //????????????position??????view????????????????????????getChildAt
                                        .addHighLight(layoutManager.findViewByPosition(targetPosition))
                                        .setLayoutRes(R.layout.view_guide_rv1)
                                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                            @Override
                                            public void onLayoutInflated(View view, Controller controller) {
                                                TextView tv = view.findViewById(R.id.tv);
                                                tv.setText("????????????????????????item????????????");
                                            }
                                        })
                                )
                                .show();
                    }
                }
            }
        });

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                NewbieGuide.with(RecyclerViewActivity.this)
                        .setLabel("grid_view_guide")
                        .alwaysShow(true)
                        .addGuidePage(GuidePage.newInstance()
                                //getChildAt???????????????????????????????????????????????????????????????position
                                .addHighLight(recyclerView.getChildAt(0))
                                .setLayoutRes(R.layout.view_guide_rv1)
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view, Controller controller) {
                                        TextView tv = view.findViewById(R.id.tv);
                                        tv.setText("??????????????????item????????????");
                                    }
                                })
                        )
                        .show();
            }
        });
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, RecyclerViewActivity.class));
    }

    public static class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        ArrayList<String> sourceData;

        static class ViewHolder extends RecyclerView.ViewHolder{
            TextView imageAppIcon;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageAppIcon = (TextView) itemView.findViewById(R.id.tv);
            }

            public void onBind(String data){
                imageAppIcon.setText(data);
            }
        }

        public Adapter(ArrayList<String> data) {
            this.sourceData = data;
        }

        @Override
        public Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,null,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(Adapter.ViewHolder viewHolder, int position) {
            if (null != sourceData && null != sourceData.get(position)) {
                String data = sourceData.get(position);
                viewHolder.onBind(data);
            }
        }

        @Override
        public int getItemCount() {
            return sourceData.size();
        }
    }

}
