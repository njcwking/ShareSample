package com.njcwking.share.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.njcwking.share.R;

import java.util.ArrayList;

/**
 * 分享接收页面
 *
 * @author
 * @date 2018/11/23 15:19
 */
public class ShareReceiveActivity extends AppCompatActivity {

    private TextView tvTextContent;
    private ImageView ivImage1;
    private ImageView ivImage2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_receive);
        initView();
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }


    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            tvTextContent.setText(sharedText);
            ivImage1.setVisibility(View.GONE);
            ivImage2.setVisibility(View.GONE);
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
            tvTextContent.setVisibility(View.GONE);
            ivImage1.setImageURI(imageUri);
            ivImage2.setVisibility(View.GONE);
        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
            tvTextContent.setVisibility(View.GONE);
            ivImage1.setImageURI(imageUris.get(0));
            ivImage2.setImageURI(imageUris.get(1));
        }
    }

    private void initView() {
        tvTextContent = findViewById(R.id.tvTextContent);
        ivImage1 = findViewById(R.id.ivImage1);
        ivImage2 = findViewById(R.id.ivImage2);
    }
}
