package com.example.android.calculator;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private CustomDraw drawView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawView = (CustomDraw)findViewById(R.id.drawing);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // COMPLETED (9) Within onCreateOptionsMenu, use getMenuInflater().inflate to inflate the menu
        getMenuInflater().inflate(R.menu.main, menu);
        // COMPLETED (10) Return true to display your menu
        return true;
    }
    public static double[] doGreyscale(Bitmap src) {
        // constant factors
        final double GS_RED = 0.299;
        final double GS_GREEN = 0.587;
        final double GS_BLUE = 0.114;

        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        // pixel information
        int A, R, G, B;
        int pixel;
        double intensity[] = new double[401];
        intensity[0] = 1;
        // get image size
        int width = src.getWidth();
        int height = src.getHeight();

        // scan through every single pixel
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get one pixel color
                pixel = src.getPixel(x, y);
                // retrieve color of all channels
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // take conversion up to one single value
                R = G = B = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }

    // COMPLETED (11) Override onOptionsItemSelected
    // COMPLETED (12) Within onOptionsItemSelected, get the ID of the item that was selected
    // COMPLETED (13) If the item's ID is R.id.action_search, show a Toast and return true to tell Android that you've handled this menu click
    // COMPLETED (14) Don't forgot to call .show() on your Toast
    // COMPLETED (15) If you do NOT handle the menu click, return super.onOptionsItemSelected to let Android handle the menu click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {


            drawView.setDrawingCacheEnabled(true);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            //attempt to save
            Bitmap img = Bitmap.createScaledBitmap(drawView.getDrawingCache(),20,20,false);
            String imgSaved = MediaStore.Images.Media.insertImage(
                    getContentResolver(), img,"hardik.png", "drawing");
            if(imgSaved!=null){
                int width = img.getWidth();
                int height = img.getHeight();
                Toast savedToast = Toast.makeText(getApplicationContext(),
                        width+" "+height+"You got a bitmap!", Toast.LENGTH_SHORT);
                savedToast.show();
            }
            else{
                Toast unsavedToast = Toast.makeText(getApplicationContext(),
                        "Oops! Bitmap is null", Toast.LENGTH_SHORT);
                unsavedToast.show();
            }
            drawView.destroyDrawingCache();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

