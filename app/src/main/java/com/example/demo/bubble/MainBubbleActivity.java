package com.example.demo.bubble;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.demo.MainActivity;
import com.example.demo.widget.R;

public class MainBubbleActivity extends Activity {
    /**
     * 全局屏幕的高和宽
     */
    private static int SCREEN_WIDTH = 0;
    private static int SCREEN_HEIGHT = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getDimension();

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] location = new int[2];
                btn.getLocationInWindow(location);
                int x = location[0]; // view 距离 window 左边的距离（即 x 轴方向）
                int y = location[1]; // view 距离 window 顶边的距离（即 y 轴方向）

                show(-x, -(y + btn.getHeight()));
            }
        });

        findViewById(R.id.happyBubble).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainBubbleActivity.this, HappyBubbleActivity.class);
                startActivity(intent);
            }
        });
    }

    private void show(int x, int y) {
        Dialog bubbleAlert = new Dialog(this);

        View bubbleView = getLayoutInflater().inflate(R.layout.overlay_pop, null);
        TextView tvKnow = (TextView) bubbleView.findViewById(R.id.bubble_btn);
        tvKnow.setText(Html.fromHtml("<u>" + "www.baidu.com" + "</u>"));
        TextView tvBubContent = (TextView) bubbleView.findViewById(R.id.bubble_text);
        tvBubContent.setText("上次程序异常退出，正在传输历史数据...");
        tvKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bubbleAlert.cancel();
            }
        });
        int tmpWidth = SCREEN_WIDTH / 5 * 3;
        int tmpHeight = SCREEN_HEIGHT / 8;
        //设置TextView宽度
        tvKnow.setMinWidth(tmpWidth);
        tvBubContent.setMaxWidth(tmpWidth);
        //以指定的样式初始化dialog
        Window win = bubbleAlert.getWindow();//获取所在window
        WindowManager.LayoutParams params = win.getAttributes();//获取LayoutParams
        params.x = x;//设置x坐标
        params.y = y;//设置y坐标
        params.width = tmpWidth;
        win.setAttributes(params);//设置生效
        bubbleAlert.setCancelable(false);
        bubbleAlert.setContentView(bubbleView);
        bubbleAlert.show();
    }

    /**
     * 获取屏幕尺寸
     */
    private void getDimension() {
        /** 获取屏幕的宽和高 */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
    }
}