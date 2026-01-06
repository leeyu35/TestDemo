package com.example.demo.editor;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.demo.R;

public class EditorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        CustomEditText editText1 = findViewById(R.id.editor1);
        editText1.setListener(new CustomEditText.Listener() {
            @Override
            public void onOverLimit() {
                Toast.makeText(EditorActivity.this, "最多只能输入2行", Toast.LENGTH_SHORT).show();
            }
        });

        CustomEditText editText = findViewById(R.id.editor2);
        editText.limitWordsFirst();
        editText.setMaxWords(15);
        editText.setListener(new CustomEditText.Listener() {
            @Override
            public void onOverLimit() {
                Toast.makeText(EditorActivity.this, "最多只能输入15个字", Toast.LENGTH_SHORT).show();
            }
        });
    }
}