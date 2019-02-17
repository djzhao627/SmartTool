package com.djzhao.smarttool.dialog.chooseproblem;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.djzhao.smarttool.R;

public class AddItemDialog extends Dialog {

    private Button cancelBtn;
    private Button addBtn;
    private EditText inputTxt;

    private onCancelClickedListener cancelListener;
    private onAddClickedListener addListener;


    public AddItemDialog(Context context) {
        super(context, R.style.dialog_custom);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_problem_add_item_dialog_layout);
        setCanceledOnTouchOutside(false);

        initView();
        initEvent();
    }

    private void initView() {
        cancelBtn = findViewById(R.id.choose_problem_add_item_cancel_btn);
        addBtn = findViewById(R.id.choose_problem_add_item_add_btn);
        inputTxt = findViewById(R.id.choose_problem_add_item_input_txt);
    }

    private void initEvent() {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cancelListener != null) {
                    cancelListener.onClick();
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (addListener != null) {
                    addListener.onClick(inputTxt.getText().toString().trim());
                }
            }
        });
    }

    public void setOnCancelClickedListener(onCancelClickedListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    public void setOnAddClickedListener(onAddClickedListener startListener) {
        this.addListener = startListener;
    }

    public interface onCancelClickedListener {
        public void onClick();
    }

    public interface onAddClickedListener {
        public void onClick(String title);
    }
}
