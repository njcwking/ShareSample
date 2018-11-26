package com.njcwking.share.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.njcwking.share.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSystemShare;
    private Button btnActionBarShare;
    private Button btnCustomShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btnSystemShare = findViewById(R.id.btnSystemShare);
        btnActionBarShare = findViewById(R.id.btnActionBarShare);
        btnCustomShare = findViewById(R.id.btnCustomShare);
        btnSystemShare.setOnClickListener(this);
        btnActionBarShare.setOnClickListener(this);
        btnCustomShare.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSystemShare:
                startActivity(SystemShareActivity.createIntent(this));
                break;
            case R.id.btnActionBarShare:
                startActivity(MenuShareProviderActivity.createIntent(this));
                break;
            case R.id.btnCustomShare:
                startActivity(CustomShareActivity.createIntent(this));
                break;
        }
    }
}
