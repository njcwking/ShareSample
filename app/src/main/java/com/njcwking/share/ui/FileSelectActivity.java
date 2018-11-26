package com.njcwking.share.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.njcwking.share.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 文件选择页面
 *
 * @author njcwking
 * @date 2018/11/23 15:17
 */
public class FileSelectActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;

    /**
     * 顶级目录
     */
    private String topDirectory = null;

    /**
     * 当前目录
     */
    private File currentDirctory = null;

    /**
     * 当前目录下的文件
     */
    private List<File> allFiles = new ArrayList<>();
    private FileAdapter mFileAdapter;

    public static final String EXTRA_ROOT_DIRECTORY = "ROOT_DIRECTORY";

    public static final String EXTRA_GET_FILE = "GET_FILE";

    public static Intent createIntent(Context context, String rootDirectory) {
        Intent intent = new Intent(context, FileSelectActivity.class);
        intent.putExtra(EXTRA_ROOT_DIRECTORY, rootDirectory);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("选择文件");
        setContentView(R.layout.activity_file_select);
        listView = findViewById(R.id.listView);
        mFileAdapter = new FileAdapter();
        listView.setAdapter(mFileAdapter);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null && extras.containsKey(EXTRA_ROOT_DIRECTORY))
            topDirectory = intent.getStringExtra(EXTRA_ROOT_DIRECTORY);
        if (TextUtils.isEmpty(topDirectory)) {
            topDirectory = "/";  //默认系统根目录
        }
        currentDirctory = new File(topDirectory);

        updateCurrentDirectory(currentDirctory);
        listView.setOnItemClickListener(this);
    }

    /**
     * 更新当前目录
     */
    private void updateCurrentDirectory(File currentDirctory) {
        allFiles.clear();
        if (!topDirectory.equals(currentDirctory.getAbsolutePath())) {
            allFiles.add(currentDirctory);
        }
        File[] files = currentDirctory.listFiles();
        List temporaryFiles = new ArrayList();
        temporaryFiles.addAll(Arrays.asList(files));
        Collections.sort(temporaryFiles, new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                int value1 = file1.isDirectory() ? 0 : 1;
                int value2 = file2.isDirectory() ? 0 : 1;
                return value1 == value2 ? file1.getName().compareToIgnoreCase(file2.getName()) : value1 - value2;
            }
        });
        allFiles.addAll(temporaryFiles);
        temporaryFiles.clear();
        mFileAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> view, View view1, int i, long l) {
        File clickFile = allFiles.get(i);
        if (clickFile.isDirectory()) {
            if (clickFile.canRead()) {
                if (clickFile.getAbsolutePath().equals(currentDirctory.getAbsolutePath())) {
                    currentDirctory = currentDirctory.getParentFile();
                } else {
                    currentDirctory = clickFile;
                }
                updateCurrentDirectory(currentDirctory);
            } else {
                Toast.makeText(this, "无权限访问该文件夹", Toast.LENGTH_SHORT).show();
            }
        } else if (clickFile.isFile()) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_GET_FILE, clickFile.getAbsolutePath());
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (currentDirctory.getAbsolutePath().equals(topDirectory)) {
            super.onBackPressed();
        } else {
            currentDirctory = currentDirctory.getParentFile();
            updateCurrentDirectory(currentDirctory);
        }
    }

    class FileAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return allFiles.size();
        }

        @Override
        public File getItem(int i) {
            return allFiles.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup group) {
            ViewHolder holder = null;
            if (view != null) {
                holder = (ViewHolder) view.getTag();
            } else {
                view = LayoutInflater.from(group.getContext()).inflate(R.layout.list_file_select, null, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            }
            File file = allFiles.get(i);
            if (file.getAbsolutePath().equals(currentDirctory.getAbsolutePath())) {
                holder.tvFileName.setText(String.format("..上级目录（%s）",file.getName()));
                holder.ivIcon.setImageResource(R.drawable.ic_folder_black_24dp);
            } else if (file.isDirectory()) {
                holder.tvFileName.setText(file.getName());
                holder.ivIcon.setImageResource(R.drawable.ic_folder_black_24dp);
            } else if (file.isFile()) {
                holder.tvFileName.setText(file.getName());
                holder.ivIcon.setImageResource(R.drawable.ic_description_black_24dp);
            }
            return view;
        }

        class ViewHolder {
            TextView tvFileName;
            ImageView ivIcon;

            public ViewHolder(View view) {
                tvFileName = view.findViewById(R.id.tvFileName);
                ivIcon = view.findViewById(R.id.ivIcon);
            }
        }

    }
}
