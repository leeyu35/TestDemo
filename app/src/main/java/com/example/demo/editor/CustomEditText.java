package com.example.demo.editor;

import android.graphics.Color;
import android.support.v7.widget.AppCompatEditText;
import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

public class CustomEditText extends AppCompatEditText {
    private int maxLines = 2;
    private int maxWords = 20;

    private boolean limitLines = true;
    private Listener listener;

    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    public void setMaxWords(int maxWords) {
        this.maxWords = maxWords;
    }

    public void limitWordsFirst() {
        limitLines = false;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    interface Listener {
        void onOverLimit();
    }

    private void init() {
        setImeOptions(EditorInfo.IME_ACTION_DONE);
        setEllipsize(null);
        setBackgroundColor(0xffF3F6F8);
//        setLines(2);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "点击", Toast.LENGTH_SHORT).show();
//                requestFocus();
//                setFocusableInTouchMode(true);
//                setSelection(Math.min(getText().length(), Math.max(getSelectionStart(), getSelectionEnd())));
            }
        });
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                int lineCount = getLineCount();
                if (limitLines) {
                    if (lineCount > maxLines) {
                        if (listener != null) {
                            listener.onOverLimit();
                        }
                        getText().delete(getSelectionStart() - 1, getSelectionEnd());
//                        setFocusableInTouchMode(false);
//                        clearFocus();
                    } else {
                        setLines(lineCount);
                        // 允许编辑
//                        setFocusableInTouchMode(true);
                    }
                } else {
                    // 限制输入长度并显示省略号
                    if (charSequence.length() > maxWords) {
//                        String trimmedText = charSequence.subSequence(0, maxWords).toString() + "...";
//                        setText(trimmedText);
//                        setSelection(trimmedText.length()); // 将光标置于末尾
                        getText().delete(getSelectionStart() - 1, getSelectionEnd());
//                        setFocusableInTouchMode(false);
//                        clearFocus();
                        if (listener != null) {
                            listener.onOverLimit();
                        }
                    } else {
                        setLines(lineCount);
                    }
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {
                // Do nothing
            }
        });
    }
}