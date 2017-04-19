package com.example.baiyuanwei.linewhirl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.baiyuanwei.linewhirl.view.WhirlSquareLineView;

public class MainActivity extends AppCompatActivity {

    private WhirlSquareLineView simpleLineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simpleLineView = (WhirlSquareLineView) findViewById(R.id.simple_line_view);
        findViewById(R.id.anim_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                simpleLineView.startAnim();
            }
        });
    }
}
