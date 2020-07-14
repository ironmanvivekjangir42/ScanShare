package com.example.scanshare.scan;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.scanshare.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SaveScan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_scan);

        final ArrayList<Bitmap> bitmapListt = FragmentScan.bitmapList;

        final EditText editText = findViewById(R.id.editText);
        Button save = findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();

                if(text!=null){
                    createPDF(bitmapListt,text);
                }
            }
        });


    }

    void createPDF(ArrayList<Bitmap> bitmaps,String name){
        ArrayList<Bitmap> bitmapListt = bitmaps;

        PdfDocument pdfDocument = new PdfDocument();

        for(Bitmap bitmap:bitmapListt) {

            PdfDocument.PageInfo pi = new PdfDocument.PageInfo.Builder(bitmap.getWidth(),bitmap.getHeight(),1).create();

            PdfDocument.Page page = pdfDocument.startPage(pi);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#FFFFFF"));
            canvas.drawPaint(paint);

            Bitmap bitmapp = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),true);
            paint.setColor(Color.BLUE);
            canvas.drawBitmap(bitmapp,0,0,null);

            pdfDocument.finishPage(page);

        }

        //save page

        File root = new File(Environment.getExternalStorageDirectory(),"/scanShare/scan");
        if(!root.exists()){
            root.mkdir();
        }

        String fileName = name+".pdf";

        File file = new File(root,fileName);

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            pdfDocument.writeTo(fileOutputStream);
        } catch (IOException e) {
            Toast.makeText(this, "Unable to save", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        pdfDocument.close();

        Toast.makeText(this, "File saved successfully", Toast.LENGTH_SHORT).show();

    }

}