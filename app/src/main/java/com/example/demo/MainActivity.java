package com.example.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.demo.anim.ActivityOne;
import com.example.demo.editor.EditorActivity;
import com.example.demo.guide.GuideActivity;
import com.example.demo.module.fastscrooll.FastScrollFragment;
import com.example.demo.pay.WxPay;


public class MainActivity extends AppCompatActivity {

    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
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

        findViewById(R.id.pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WxPay wxPay = new WxPay(activity);

                wxPay.pay("{\"timeStamp\":1723012380,\"package\":\"Sign=WXPay\",\"appId\":\"wxbe3521a0fad2fe85\",\"sign\":\"xjjNB1UF8d2qe61tSXuzbTmIBr2ykl44IcPQ3FzH7neAiKYwBoyBdRHVWkOsgyBUAQiqi1JBGeJCMoc/10fqeL07Q0q5RBQf+uOSyu5XBOXxMPRBd18TLzdzWHLX1pL5YgZl/4XCLOxTF2rj9Vohq5LxhBZrJtjFTAmsJme7SbmcmQWWhLXmmo9H9JFOMXVjPqc2opjU6LbMtjK6aAS5VoFlbF6taWypxYW4xtt1nvR5mpWB9VyneUHpK+LkBCpCGGpslRF6rwPvFhVz/Z9GnRPLcRKap44RaF45Jb5aizypYfQ/PoxK2Px8lg6na6+ZAw54oL5a5OkA24lQj7/QgA==\",\"signType\":\"RSA\",\"prepayid\":\"up_wx07143300018161a368069caf7be4bf0001\",\"partnerid\":\"1680558744\",\"nonceStr\":\"zjsdcvzjzSpiAZsNytDhviQUSGOuLQEB\"}");
            }
        });

        findViewById(R.id.editor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
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