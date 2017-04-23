package com.example.baiyuanwei.linewhirl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.baiyuanwei.linewhirl.view.WhirlSquareLineViewV4;

public class FourthActivity extends AppCompatActivity {

    private WhirlSquareLineViewV4 whirlSquareLineView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fourth_layout);

        whirlSquareLineView = (WhirlSquareLineViewV4) findViewById(R.id.whirl_square_line_view);
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
    }
}
