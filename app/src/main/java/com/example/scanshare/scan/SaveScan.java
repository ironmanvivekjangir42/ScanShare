package com.example.scanshare.scan;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.scanshare.R;

import java.util.ArrayList;

public class SaveScan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_scan);

        ArrayList<Bitmap> bitmapListt = FragmentScan.bitmapList;
        Bitmap bitmap = bitmapListt.get(0);

        Toast.makeText(this, String.valueOf(bitmapListt.size()), Toast.LENGTH_SHORT).show();

        ImageView imageView = findViewById(R.id.imageViewSave);
        imageView.setImageBitmap(bitmap);
    }
}