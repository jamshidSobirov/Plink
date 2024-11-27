package com.plink6746;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;


public class MyDialog extends Dialog {

    public Activity activity;
    public Dialog dialog;
    private DialogClick dialogClick;
    public FrameLayout menu, btn;
    public TextView msg, btnTxt;
    String desc;

    public MyDialog(Activity a, String desc, DialogClick dc) {
        super(a, R.style.full_screen_dialog);
        this.activity = a;
        this.dialogClick = dc;
        this.desc = desc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_dialog);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initViews();


    }

    private void initViews() {
        menu = findViewById(R.id.btnMenu);
        btn = findViewById(R.id.btn);
        btnTxt = findViewById(R.id.btnText);
        msg = findViewById(R.id.message);

        if (desc.equals("pause")) {
            msg.setText(R.string.pause);
            btnTxt.setText(R.string.continue1);
        }

        menu.setOnClickListener(v -> {
            dialogClick.onMenuClick();
            dismiss();
        });
        btn.setOnClickListener(v -> {
            dialogClick.onBtnClick();
            dismiss();
        });

    }

}

