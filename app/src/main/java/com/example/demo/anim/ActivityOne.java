package com.example.demo.anim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.demo.R;
@SuppressLint("NewApi")
public class ActivityOne extends Activity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        intent = new Intent(ActivityOne.this, ActivityTwo.class);
    }


    public void explode(View view) {
        intent.putExtra("transition", "explode");//将原先的跳转改成如下方式
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ActivityOne.this).toBundle());
    }

    public void slide(View view) {
        intent.putExtra("transition", "slide");//将原先的跳转改成如下方式
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ActivityOne.this).toBundle());
    }

    public void fade(View view) {
        intent.putExtra("transition", "fade");//将原先的跳转改成如下方式
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ActivityOne.this).toBundle());
    }

    public void share(View view) {
        //共享元素
        Button share = (Button) findViewById(R.id.share);
        intent.putExtra("transition", "share");
        //将原先的跳转改成如下方式，注意这里面的第三个参数决定了ActivityTwo 布局中的android:transitionName的值，它们要保持一致
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ActivityOne.this, share, "shareTransition").toBundle());
    }
}
