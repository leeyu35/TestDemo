package com.example.demo.bubble;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.demo.R;

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
//                int[] clickedViewLocation = new int[2];
//                btn.getLocationOnScreen(clickedViewLocation);
//                Rect  mClickedRect = new Rect(0, 0, view.getWidth(), view.getHeight());
//               int  x = clickedViewLocation[0] + mClickedRect.width() / 2;
//                int y = clickedViewLocation[1] + mClickedRect.height();

                //获取通知栏高度  重要的在这，获取到通知栏高度
                int notificationBar = Resources.getSystem().getDimensionPixelSize(Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
                //获取控件 textview 的绝对坐标,( y 轴坐标是控件上部到屏幕最顶部（不包括控件本身）)
                //location [0] 为x绝对坐标;location [1] 为y绝对坐标
                int[] location = new int[2];
                view.getLocationInWindow(location); //获取在当前窗体内的绝对坐标
                view.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标

                int x = 500; //对 dialog 设置 x 轴坐标
                int y = location[1] + view.getHeight() - notificationBar; //对dialog设置y轴坐标

                View bubbleView = getLayoutInflater().inflate(R.layout.overlay_pop, null);
                TextView tvKnow = (TextView) bubbleView.findViewById(R.id.bubble_btn);
                tvKnow.setText(Html.fromHtml("<u>" + "www.baidu.com" + "</u>"));
                TextView tvBubContent = (TextView) bubbleView.findViewById(R.id.bubble_text);
                tvBubContent.setText("上次程序异常退出，正在传输历史数据...");
                tvKnow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        bubbleAlert.cancel();
                    }
                });
                int tmpWidth = SCREEN_WIDTH / 5 * 3;
                int tmpHeight = SCREEN_HEIGHT / 8;
                //设置TextView宽度
                tvKnow.setMinWidth(tmpWidth);
                tvBubContent.setMaxWidth(tmpWidth);

                show(x, y, bubbleView);
            }
        });

        findViewById(R.id.happyBubble).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainBubbleActivity.this, HappyBubbleActivity.class);
                startActivityForResult(intent, 2000);
                finish();
            }
        });

        findViewById(R.id.dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View bubbleView = getLayoutInflater().inflate(R.layout.login, null);
                show(0, 0, bubbleView);
            }
        });


        findViewById(R.id.dialogFragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainBubbleActivity.this, DialogFragmentApiUseDemoActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void show(int x, int y, View view) {
        Dialog bubbleAlert = new Dialog(this);
        //以指定的样式初始化dialog
        Window win = bubbleAlert.getWindow();//获取所在window
        WindowManager.LayoutParams params = win.getAttributes();//获取LayoutParams
        params.x = x;//设置x坐标
        params.y = y;//设置y坐标
        params.gravity = Gravity.TOP;
//        win.setGravity(Gravity.LEFT | Gravity.TOP);
        win.setAttributes(params);//设置生效
        bubbleAlert.setCancelable(false);
        bubbleAlert.setContentView(view);
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