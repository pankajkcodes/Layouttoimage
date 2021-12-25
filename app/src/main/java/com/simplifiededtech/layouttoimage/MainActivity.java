package com.simplifiededtech.layouttoimage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    LinearLayout layout_view;
    ImageView imgResultImage, imageView;
    Button btnConvertToImage, sharBtn;

    Drawable bgDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout_view = findViewById(R.id.linerLayout);
        imgResultImage = findViewById(R.id.imgResultImage);
        btnConvertToImage = findViewById(R.id.btnConvertToimage);
        sharBtn = findViewById(R.id.shareBtn);



        btnConvertToImage.setOnClickListener(v -> {

            Bitmap image = getBitmapFromView(layout_view);
            imgResultImage.setImageBitmap(image);

        });

        sharBtn.setOnClickListener((v -> {

            shareBitmap();
        }));
    }

    @SuppressLint("SetWorldReadable")
    private void shareBitmap() {

        Bitmap bitmap = getBitmapFromView(layout_view);

        try {
            File file = new File(getApplicationContext().getExternalCacheDir(),
                    "img.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);

            intent.putExtra(Intent.EXTRA_STREAM, photoURI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");

            startActivity(Intent.createChooser(intent, "Share image via"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

}