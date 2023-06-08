package com.example.demo.bubble;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.demo.R;


public class DialogFragmentApiUseDemoActivity extends AppCompatActivity implements View.OnClickListener
{


    private static final String TAG = "xp.chen";

    private Button btn_show_dialog_fragment;
    private Button btn_show_normal_dialog;


    private MyDialog mNormalDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_fragment_api_use_demo);
        initView();
        Log.i(TAG, "onCreate: ");
        /*if (savedInstanceState != null) {
            boolean is_shown = savedInstanceState.getBoolean("DIALOG_SHOWN");
            if (is_shown) {
                showNormalDialog();
            }
        } else {
            Toast.makeText(this, "savedInstanceState is NULL", Toast.LENGTH_SHORT).show();
        }*/
    }


    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState: ");
        /*if (mNormalDialog != null && mNormalDialog.isShowing()) {
            outState.putBoolean("DIALOG_SHOWN", true);
        }*/
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }


    @Override
    protected void onStop()
    {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }


    private void initView()
    {
        btn_show_dialog_fragment = findViewById(R.id.btn_show_dialog_fragment);
        btn_show_dialog_fragment.setOnClickListener(this);

        btn_show_normal_dialog = findViewById(R.id.btn_show_normal_dialog);
        btn_show_normal_dialog.setOnClickListener(this);
    }

    /**
     * Show a normal dialog use Dialog API.
     */
    private void showNormalDialog() {
        mNormalDialog = new MyDialog(DialogFragmentApiUseDemoActivity.this);
        mNormalDialog.show();
    }

    private void showDialogFragment() {
        MyDialogFragment dialogFragment = new MyDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "dialogFragment");
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.btn_show_dialog_fragment:
                showDialogFragment();
                break;
            case R.id.btn_show_normal_dialog:
                showNormalDialog();
                break;
            default:
                break;
        }
    }


    public static class MyDialog extends Dialog {

        private String TAG = "xp.chen-Dialog";

        public MyDialog(@NonNull Context context)
        {
            super(context);
            setContentView(R.layout.login);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            Log.i(TAG, "onCreate: MyDialog->onCreate()");
        }

        @Override
        protected void onStart()
        {
            super.onStart();
            Log.i(TAG, "onStart: MyDialog->onStart()");
        }

        @Override
        protected void onStop()
        {
            super.onStop();
            Log.i(TAG, "onStop: MyDialog->onStop()");
        }

        @Override
        public void cancel() {
            super.cancel();
            Log.i(TAG, "cancel: MyDialog->cancel()");
        }

        @Override
        public void dismiss()
        {
            super.dismiss();
            Log.i(TAG, "dismiss: MyDialog->dismiss()");
        }
    }


}