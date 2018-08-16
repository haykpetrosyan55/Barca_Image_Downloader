package com.BarcaImageDownloader.BarcaImageDownloader.BarcaImageDownloader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE = 1001;
    private Button button;
    private TextView text;
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.edit_text);
        button = findViewById(R.id.download_id);
        bar = findViewById(R.id.proBar);
        requestPermissions();
        initialisationRecycler(text);
    }
    private void initialisationRecycler(TextView editText) {
        final LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ImageAdapter imgAdapter = new ImageAdapter(this, ImageProvider.getList(this), editText);
        RecyclerView recyclerView = findViewById(R.id.rec_id);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(imgAdapter);
    }

    private void imageLoader(final Button button, final TextView editText) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String root = Environment.getExternalStoragePublicDirectory
                        (Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                bar.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            httpLoader(editText, root);
                        } catch (IOException e) {
                            e.printStackTrace();
                            bar.setVisibility(View.GONE);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bar.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, R.string.upload,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private void httpLoader(TextView editText, String root) throws IOException {
        InputStream in;
        String url = editText.getText().toString();
        URL ul = new URL(url);
        HttpURLConnection ur = (HttpURLConnection) ul.openConnection();
        ur.connect();
        in = ur.getInputStream();
        final Bitmap bitmap = BitmapFactory.decodeStream(in);
        File file = new File(root + getString(R.string.fraction) +
                ImageProvider.getPosition().getImgNum() + getString(R.string.format_jpg));
        ImageProvider.getPosition().setDownload(true);
        FileOutputStream out = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        byte[] buf = new byte[1024];
        int n;
        while (-1 != (n = in.read(buf.clone()))) {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imageLoader(button, text);
                } else {
                    requestPermissions();
                }
        }
    }
}
