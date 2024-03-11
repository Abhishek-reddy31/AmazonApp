package com.example.amazonclone.MenuFiles;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.amazonclone.HomeActivity;
import com.example.amazonclone.R;

public class BaseActivity extends AppCompatActivity {
    RadioGroup radioGroup1;
    RadioButton home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        radioGroup1 = findViewById(R.id.radioGroup1);
        home = findViewById(R.id.bottom_home);

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Intent in;

                if (i == R.id.bottom_home) {
                    in = new Intent(getBaseContext(), HomeActivity.class);
                } else if (i == R.id.bottom_addprod) {
                    in = new Intent(getBaseContext(), AddProduct.class);
                } else if (i == R.id.bottom_search) {
                    in = new Intent(getBaseContext(), SearchActivity.class);
                } else if (i == R.id.bottom_cart) {
                    in = new Intent(getBaseContext(), CartActivity.class);
                } else if (i == R.id.bottom_profile) {
                    in = new Intent(getBaseContext(), ProfileActivity.class);
                } else {
                    return;
                }

                startActivity(in);
                overridePendingTransition(0, 0);

            }
        });
    }
}