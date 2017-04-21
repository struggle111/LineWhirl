package com.example.baiyuanwei.linewhirl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.baiyuanwei.linewhirl.view.WhirlSquareLineViewV2;

public class SecondActivity extends AppCompatActivity {


    private WhirlSquareLineViewV2 whirlSquareLineView;
    private EditText offsetEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);

        whirlSquareLineView = (WhirlSquareLineViewV2) findViewById(R.id.whirl_square_line_view);
        offsetEdit = (EditText) findViewById(R.id.offset_edit);

        findViewById(R.id.sure_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String offset = offsetEdit.getText().toString();
                float off;
                if (TextUtils.isEmpty(offset)) {
                    off = 0;
                } else {
                    off = Float.valueOf(offset);
                }

                whirlSquareLineView.setStartOffset(off);
                offsetEdit.setText(null);
                whirlSquareLineView.startAnim();
            }
        });

        findViewById(R.id.start_anim_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whirlSquareLineView.startAnim();
            }
        });

        findViewById(R.id.cancel_anim_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whirlSquareLineView.cancelAnim();
            }
        });

        findViewById(R.id.end_anim_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whirlSquareLineView.endAnim();
            }
        });

        findViewById(R.id.lt_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whirlSquareLineView.setStartOffsetType(WhirlSquareLineViewV2.LEFT_TOP_START);
                whirlSquareLineView.startAnim();
            }
        });
        findViewById(R.id.rt_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whirlSquareLineView.setStartOffsetType(WhirlSquareLineViewV2.RIGHT_TOP_START);
                whirlSquareLineView.startAnim();
            }
        });
        findViewById(R.id.rb_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whirlSquareLineView.setStartOffsetType(WhirlSquareLineViewV2.RIGHT_BOTTOM_START);
                whirlSquareLineView.startAnim();
            }
        });
        findViewById(R.id.lb_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whirlSquareLineView.setStartOffsetType(WhirlSquareLineViewV2.LEFT_BOTTOM_START);
                whirlSquareLineView.startAnim();
            }
        });
    }
}
