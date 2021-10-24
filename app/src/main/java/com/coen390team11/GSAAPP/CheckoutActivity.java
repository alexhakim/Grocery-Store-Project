package com.coen390team11.GSAAPP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class CheckoutActivity extends AppCompatActivity {

    EditText randomEditText;
    private ImageView barcode;
    Button tempButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        ActionBar actionBar = getSupportActionBar();
        // changing color of action bar
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#344398"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);

        barcode = findViewById(R.id.barcode);
        randomEditText = findViewById(R.id.randomEditText);
        tempButton = findViewById(R.id.tempButton);

        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBarcode();
            }
        });


    }

    private void getBarcode(){
        try{
            setBarcode();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
    public void setBarcode() throws WriterException {

        BitMatrix bitMatrix = multiFormatWriter.encode(randomEditText.getText().toString(), BarcodeFormat.CODE_128,400,170,null);
        // first variable in .encode is what we want to receive, means the total price from
        // bag fragment, for now it is set to random edit text

        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

        barcode.setImageBitmap(bitmap);
        // set generated barcode (bitmap) to image view (barcode)
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }
}