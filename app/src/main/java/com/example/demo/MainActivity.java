package com.example.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.demo.anim.ActivityOne;
import com.example.demo.guide.GuideActivity;
import com.example.demo.module.fastscrooll.FastScrollFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bubble).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AutoMobiumActivity.class);
                startActivityForResult(intent,1000);
            }
        });

        findViewById(R.id.guide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GuideActivity.class);
                startActivityForResult(intent,1001);
            }
        });

        findViewById(R.id.anim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActivityOne.class);
                startActivityForResult(intent,1001);
            }
        });
//        getSupportFragmentManager().beginTransaction()
//                .replace(android.R.id.content, getFragment(), "mainFragemnt")
//                .commit();
    }





    private Fragment getFragment() {
        return

                new FastScrollFragment()

//                new PtrPageFra   gment()

//                new WatermarkFragment()

                //new JavaJsFragment()

                ;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}