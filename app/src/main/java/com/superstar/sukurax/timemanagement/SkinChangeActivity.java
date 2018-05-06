package com.superstar.sukurax.timemanagement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

public class SkinChangeActivity extends Activity{
    ImageView back_toolbar_pic,skin1_pic,skin2_pic,skin3_pic;
    TextView back_toolbar_text;
    CardView cardView1,cardView2,cardView3;
    android.support.v7.widget.Toolbar back_toolbar;
    SharedPreferences sp;
    @SuppressLint("ResourceAsColor")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp= getSharedPreferences("TimeManagement", Context.MODE_PRIVATE);
        switch (sp.getInt("skin_num", 1)){
            case 1:
                setTheme(R.style.AppTheme);
                break;
            case 2:
                setTheme(R.style.AppTheme2);
                break;
            case 3:
                setTheme(R.style.AppTheme3);
                break;
            default:
                break;
        }
        setContentView(R.layout.skin_change);

        back_toolbar_pic=(ImageView)findViewById(R.id.back_toolbar_pic);
        back_toolbar_text=(TextView) findViewById(R.id.back_toolbar_text);
        back_toolbar=(android.support.v7.widget.Toolbar) findViewById(R.id.back_toolbar);

        back_toolbar_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cardView1=(CardView)findViewById(R.id.skin1);
        cardView2=(CardView)findViewById(R.id.skin2);
        cardView3=(CardView)findViewById(R.id.skin3);

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("TimeManagement", Context.MODE_PRIVATE);
                sp.edit().putInt("skin_num", 1).apply();
                ThemeUtils.changeToTheme(SkinChangeActivity.this, 1);


            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("TimeManagement", Context.MODE_PRIVATE);
                sp.edit().putInt("skin_num", 2).apply();
                ThemeUtils.changeToTheme(SkinChangeActivity.this, 2);
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("TimeManagement", Context.MODE_PRIVATE);
                sp.edit().putInt("skin_num", 3).apply();
                ThemeUtils.changeToTheme(SkinChangeActivity.this, 3);
            }
        });

        skin1_pic=(ImageView)findViewById(R.id.skin1_pic);
        skin2_pic=(ImageView)findViewById(R.id.skin2_pic);
        skin3_pic=(ImageView)findViewById(R.id.skin3_pic);
    }


    @Override
    protected void onResume() {
        super.onResume();
        back_toolbar_text.setText("皮肤");

        switch (sp.getInt("skin_num", 1)){
            case 1:
                back_toolbar.setBackgroundColor(getResources().getColor(R.color.skinColor1_2));
                skin1_pic.setVisibility(View.VISIBLE);
                skin2_pic.setVisibility(View.GONE);
                skin3_pic.setVisibility(View.GONE);
                break;
            case 2:
                back_toolbar.setBackgroundColor(getResources().getColor(R.color.skinColor2_2));
                skin1_pic.setVisibility(View.GONE);
                skin2_pic.setVisibility(View.VISIBLE);
                skin3_pic.setVisibility(View.GONE);
                break;
            case 3:
                back_toolbar.setBackgroundColor(getResources().getColor(R.color.skinColor3_2));
                skin1_pic.setVisibility(View.GONE);
                skin2_pic.setVisibility(View.GONE);
                skin3_pic.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =  new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}
